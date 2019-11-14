package fr.alasdiablo.mcextrator.controler;

import fr.alasdiablo.mcextrator.FileCopy;
import fr.alasdiablo.mcextrator.FileLocation;
import fr.alasdiablo.mcextrator.MCExtrator;
import fr.alasdiablo.mcextrator.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

public class Controler {

    @FXML
    public TextField intput;
    @FXML
    public TextField output;
    @FXML
    public Button start;
    @FXML
    public ProgressBar load;
    @FXML
    public TextArea logger;
    @FXML
    public ChoiceBox VERSION;

    @FXML
    private void initialize() {
        intput.setText(FileLocation.Companion.getMC_DIR());
        Main.addTextInLogger(String.format("Minecraft folder set to: %s\n", FileLocation.Companion.getMC_DIR()));
        logger.setText(Main.getLoggerText());
        output.setText(FileLocation.Companion.getOUT_DIR());
        Main.addTextInLogger(String.format("Output folder set to: %s\n", FileLocation.Companion.getOUT_DIR()));
        logger.setText(Main.getLoggerText());
        if (FileLocation.Companion.getMC_VERSION_LIST() == null) {
            VERSION.setItems(FXCollections.observableArrayList("No Version Found"));
            VERSION.getSelectionModel().selectFirst();
        } else {
            VERSION.setItems(FXCollections.observableArrayList(FileLocation.Companion.getMC_VERSION_LIST()));
            VERSION.getSelectionModel().selectFirst();
        }

        intput.textProperty().addListener((observable, oldValue, newValue) -> {
            FileLocation.Companion.setMC_DIR(newValue);
            Main.addTextInLogger(String.format("Minecraft folder set to: %s\n", FileLocation.Companion.getMC_DIR()));
            logger.setText(Main.getLoggerText());
            Main.updateVersion();
            if (FileLocation.Companion.getMC_VERSION_LIST() == null) {
                VERSION.setItems(FXCollections.observableArrayList("No Version Found"));
                VERSION.getSelectionModel().selectFirst();
            } else {
                VERSION.setItems(FXCollections.observableArrayList(FileLocation.Companion.getMC_VERSION_LIST()));
                VERSION.getSelectionModel().selectFirst();
            }
        });
        output.textProperty().addListener((observable, oldValue, newValue) -> {
            FileLocation.Companion.setOUT_DIR(newValue);
            Main.addTextInLogger(String.format("Output folder set to: %s\n", FileLocation.Companion.getOUT_DIR()));
            logger.setText(Main.getLoggerText());
        });

        start.setOnAction(event -> {
            Service service = new Service() {
                @Override
                protected Task createTask() {
                    return new FileCopy((String) VERSION.getSelectionModel().getSelectedItem()).copy();
                }
            };
            load.progressProperty().bind(service.progressProperty());
            logger.textProperty().bind(service.messageProperty());
            service.start();
        });
    }
}
