package org.example.mygame;

import com.prutzkow.resourcer.ProjectResourcer;
import com.prutzkow.resourcer.Resourcer;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class MenuController {

    @FXML
    private ImageView man;

    private static Resourcer resourcer = ProjectResourcer.getInstance();

    @FXML
    public void initialize() {

        RotateTransition swingTransition = new RotateTransition(Duration.millis(2000), man);
        swingTransition.setFromAngle(-0.05);
        swingTransition.setToAngle(0.05);

        swingTransition.setInterpolator(Interpolator.EASE_BOTH);

        swingTransition.setCycleCount(RotateTransition.INDEFINITE);
        swingTransition.setAutoReverse(true);  // Раскачивание назад

        swingTransition.play();

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(2000), man);
        translateTransition.setFromY(-2);
        translateTransition.setToY(3);
        translateTransition.setInterpolator(Interpolator.EASE_BOTH);
        translateTransition.setCycleCount(TranslateTransition.INDEFINITE);
        translateTransition.setAutoReverse(true);
        translateTransition.play();
    }

    @FXML
    private void exit(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    private void goToSelection(ActionEvent event) {
        try {
//            Parent parent = FXMLLoader.load(getClass().getResource("/org/example/mygame/selectionScene.fxml"));
            Parent parent = FXMLLoader.load(getClass().getResource(resourcer.getString("selectionScene")));
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(parent, 1450, 1000));
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
