package com.calcapp.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                App.class.getResource("main.fxml")
        );
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().addAll(
                Objects.requireNonNull(App.class.getResource("styles/base.css")).toExternalForm(),
                Objects.requireNonNull(App.class.getResource("styles/calculator.css")).toExternalForm(),
                Objects.requireNonNull(App.class.getResource("styles/scientific.css")).toExternalForm(),
                Objects.requireNonNull(App.class.getResource("styles/history.css")).toExternalForm()
        );

        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Calc");
        stage.setScene(scene);
        stage.setMinWidth(380);
        stage.setMinHeight(620);
        stage.setResizable(true);
        stage.show();

        MainController controller = loader.getController();
        controller.initKeyboardShortcuts(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}