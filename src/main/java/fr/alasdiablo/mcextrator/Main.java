package fr.alasdiablo.mcextrator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    private static MCExtrator mcExtrator;

    private static StringBuilder loggerText = new StringBuilder();

    public static void updateVersion() {
        mcExtrator.getVersion();
    }

    public void start(Stage primaryStage) throws Exception {
        mcExtrator = new MCExtrator();
        mcExtrator.getFolder();
        mcExtrator.getVersion();

        URL view = getClass().getClassLoader().getResource("View.fxml");
        assert view != null;
        Parent root = FXMLLoader.load(view);
        primaryStage.setTitle("MCExtrator by AlasDiablo");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public synchronized static void addTextInLogger(String text) {
        loggerText.insert(0, text);
    }

    public synchronized static String getLoggerText() {
        return loggerText.toString();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
