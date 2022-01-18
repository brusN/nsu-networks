package org.nsu.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.nsu.model.utils.dataproviders.ImageProvider;

import java.io.IOException;
import java.util.Objects;

public class GUI extends Application {

    @Override
    public void start(Stage stage) {
        Stage appStage = new Stage();
        try {
            appStage.setTitle("lab3");
            appStage.getIcons().add(ImageProvider.APP_LOGO.getImage());
            appStage.setResizable(false);
            Parent root = FXMLLoader.load(Objects.requireNonNull(GUI.class.getClassLoader().getResource("fxml/gui.fxml")));
            appStage.setScene(new Scene(root));
            appStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
