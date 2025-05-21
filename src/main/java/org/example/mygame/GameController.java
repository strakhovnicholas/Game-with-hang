package org.example.mygame;

import com.prutzkow.resourcer.ProjectResourcer;
import com.prutzkow.resourcer.Resourcer;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;

import java.util.Objects;
import java.util.Random;

public class GameController {
    private String buttonValue = null;
    private static Resourcer resourcer = ProjectResourcer.getInstance();
    private int lives = 0; // Текущее количество потраченных жизней
    private final int maxLives = 4;
    private String wordToGuess = null;
    private String prompt = null;

    private final ArrayList<Image> images = new ArrayList<Image>();
    private int wordSize = 0;
    int count = 0;
    private char[] wordFromField = null;

    @FXML
    private ImageView emptyHang;
    @FXML
    private GridPane gridPaneLetters;
    @FXML
    private GridPane gridPaneAlphabet;
    @FXML
    private Text textPrompt;
    @FXML
    private Text resultWords;
    @FXML
    private ProgressBar progressBar;

    @FXML
    void buttonClick(ActionEvent event) {
        if (lives < 4)
        {
            buttonValue = ((Button)event.getSource()).getText();
            CheckInput(buttonValue, event);
        }
    }

    @FXML
    public void initialize() {
        this.setButtonsActive();
        this.resultWords.setText("");
        setNodesInvisible(wordSize);
        lives = 0;

        this.emptyHang.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/hangNoLoop.png"))));

        String randomLine = GameController.getRandomWord(TopicsController.getWords());
        this.wordToGuess = randomLine.split(":")[0].toUpperCase();
        this.prompt = randomLine.split(":")[1];
        this.textPrompt.setText(prompt);

        this.wordSize = wordToGuess.length();
        this.wordFromField = new char[wordSize];

        System.out.println("\n");
        System.out.println(wordToGuess);

        setNodesVisible(wordSize);
        this.images.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/hang.png"))));
        this.images.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/hangHead.png"))));
        this.images.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/hangBody.png"))));
        this.images.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/hangFull.png"))));

        progressBar.setProgress(1.0);
    }

    private void setNodesVisible(int wordSize) {
        ObservableList<Node> nodes = this.gridPaneLetters.getChildren();
        for (Node node : nodes) {
            Integer columnIndex = GridPane.getColumnIndex(node);
            if (columnIndex != null && columnIndex < wordSize) {
                node.setVisible(true);
            }
        }
    }

    private void setNodesInvisible(int wordSize) {
        ObservableList<Node> nodes = this.gridPaneLetters.getChildren();
        for (Node node : nodes) {
            Integer columnIndex = GridPane.getColumnIndex(node);
            if (columnIndex != null && columnIndex < wordSize) {
                node.setVisible(false);
                ((TextField) node).setText("");
            }
        }
    }

    private void setButtonsActive()
    {
        ObservableList<Node> nodes = this.gridPaneAlphabet.getChildren();
        for (Node node : nodes) {
            node.setDisable(false);
            node.setStyle("-fx-background-color: linear-gradient(#ffffff,#ffffff);");
        }
    }


    private static String getRandomWord(ArrayList<String> wordList) {
        if (wordList.isEmpty()) {
            throw new IllegalArgumentException("Word list is empty");
        }
        Random random = new Random();
        int randomIndex = random.nextInt(wordList.size());
        return wordList.get(randomIndex);
    }

    public void CheckInput(String str, ActionEvent event){
        if (this.wordToGuess.contains(str)) {
            int index = 0;
            for(int i = 0; i < this.wordToGuess.length(); i++) {
                char c = this.wordToGuess.charAt(i);
                if (String.valueOf(c).equals(str)) {
                    setLetter(index, Character.toString(c));
                    this.wordFromField[index] = c;
                    ((Button)event.getSource()).setStyle("-fx-background-color: linear-gradient(#cfcfcf,#cfcfcf)");
                    ((Button)event.getSource()).setDisable(true);
                }
                index++;
            }
        }
        else
        {
            setNewImage();
            ((Button)event.getSource()).setStyle("-fx-text-fill: white; -fx-background-color: linear-gradient(#000000,#000000)");
            ((Button)event.getSource()).setDisable(true);
        }

        if (isFinished())
        {
//            this.resultWords.setText("You've won, save another one!");
//            PauseTransition pause = new PauseTransition(Duration.seconds(3));
//            pause.setOnFinished(event2 -> {
//                initialize();
//            });
//            pause.play();
            String victoryMessage = "You've won, save another one!";
            textGenerator(victoryMessage);
        }
    }

    private void textGenerator(String message) {
        final int[] currentIndex = {0};//неизменяемая ссылка с изменяемым значением, чтобы обойти лямбду

        AudioClip typeSound = new AudioClip(getClass().getResource("/key.wav").toString());

        Timeline typewriterEffect = new Timeline(new KeyFrame(Duration.millis(100), event -> {
            if (currentIndex[0] < message.length()) {
                resultWords.setText(resultWords.getText() + message.charAt(currentIndex[0]));
                typeSound .play();
                currentIndex[0]++;

            }
        }));
        typewriterEffect.setCycleCount(message.length());

        typewriterEffect.setOnFinished(event -> {
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(3), resultWords);
            fadeTransition.setFromValue(1.0);
            fadeTransition.setToValue(0.0);
            getResponse();
            fadeTransition.setOnFinished(fadeEvent -> {
                resultWords.setText("");
                initialize();
            });
            fadeTransition.play();
        });

        typewriterEffect.play(); // Запускаем эффект печатной машинки
    }

    public void setNewImage() {
        if (lives < maxLives) {
            // Установка нового изображения для виселицы
            this.emptyHang.setImage(this.images.get(lives));
            lives++;

            // Обновление ProgressBar с учетом количества оставшихся жизней
            double progress = 1.0 - (lives * 0.25); // Уменьшаем прогресс на 0.25 за каждую потерянную жизнь
            progressBar.setProgress(progress);

            System.out.println("Lives: " + lives + ", Progress: " + progress);
        }

        if (lives == maxLives) {
            // Если все попытки закончились
            String looseMessage = "You've lost, try to save another one!";
            textGenerator(looseMessage);
        }
    }


    public void setLetter(int index,String str){
        ObservableList<Node> nodes = this.gridPaneLetters.getChildren();
        for (Node node : nodes) {
            Integer columnIndex = GridPane.getColumnIndex(node);
            if (columnIndex != null && columnIndex == index) {
                ((TextField) node).setText(str);
                ((TextField) node).setEditable(false);
            }
        }
    }

    private void getResponse() {
        ObservableList<Node> nodes = this.gridPaneLetters.getChildren();
        int index = 0;
        for (Node node : nodes) {
            if (index < this.wordToGuess.length())
            {
                ((TextField) node).setText(String.valueOf(this.wordToGuess.charAt(index)));
                ((TextField) node).setEditable(false);
            }
            index++;
        }
    }


    @FXML
    void getBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("selectionScene.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1450, 1000));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isFinished()
    {
        String arrayWord = String.valueOf(this.wordFromField);
        return arrayWord.equals(this.wordToGuess);
    }


}
