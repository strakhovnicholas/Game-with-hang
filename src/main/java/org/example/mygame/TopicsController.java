package org.example.mygame;

import com.prutzkow.resourcer.ProjectResourcer;
import com.prutzkow.resourcer.Resourcer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

import java.io.IOException;
import java.util.Random;

public class TopicsController {
    private static ArrayList<String> words = null;
    private static ArrayList<String> prompts = null;
    private String fileName = null;
    private static Resourcer resourcer = ProjectResourcer.getInstance();
    public String getFileName() {
        return this.fileName;
    }

    private void setFileName(String fileName) {
        this.fileName = fileName;
    }

    static ArrayList<String> getWords() {
        return words;
    }

    static ArrayList<String> getPrompts() {
        return prompts;
    }

    @FXML
    void allSelected(ActionEvent event)
    {
    }

    @FXML
    void casualSelected(ActionEvent event) {
        this.setFileName("Casual.txt");
        fillList(event);
    }

    @FXML
    void faunaSelected(ActionEvent event) {
        this.setFileName("Fauna.txt");
        fillList(event);
    }

    @FXML
    void geographySelected(ActionEvent event) {
        this.setFileName("Geography.txt");
        fillList(event);
    }

    @FXML
    void historySelected(ActionEvent event) {
        this.setFileName("History.txt");
        fillList(event);
    }

    @FXML
    void literatureSelected(ActionEvent event) {
        this.setFileName("Literature.txt");
        fillList(event);
    }

    @FXML
    void scienceSelected(ActionEvent event) {
        this.setFileName("Science.txt");
        fillList(event);
    }

    private void getWordsFromFile() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/" + this.getFileName());
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                this.words.add(line);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playGame(ActionEvent event)
    {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/org/example/mygame/gameScene.fxml"));
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(parent, 1450, 1000));
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fillList(ActionEvent event) {
        words = new ArrayList<String>();
        this.getWordsFromFile();

        for(String word: words)
            System.out.println(word);

        playGame(event);
    }

    @FXML
    void getBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1450, 1000));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
