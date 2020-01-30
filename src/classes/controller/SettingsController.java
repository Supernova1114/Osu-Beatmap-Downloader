package classes.controller;

import classes.Main;
import classes.WebScraper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class SettingsController{

    private static String settDir = System.getProperty("user.dir") + File.separator +"settings.txt";
    private static ArrayList<String> settings;
    public static String downloadDir;
    private static String username;
    private static String password;




    public SettingsController(){
        setup();
    }


    @FXML
    Label directoryLabel;
    @FXML
    Button changeButton;
    @FXML
    TextField usernameField;
    @FXML
    PasswordField passwordField;
    @FXML
    Label passwordLabel;
    @FXML
    CheckBox showCBox;
    @FXML
    ToggleButton saveButton;
    @FXML
    Button openButton;

    @FXML
    public void browseFiles(){

        changeButton.setDisable(true);
        DirectoryChooser chooser = new DirectoryChooser();
        File selectedDirectory = chooser.showDialog(Main.getMainStage());

        if ( selectedDirectory != null ) {
            String dirString = selectedDirectory.getAbsolutePath();
            System.out.println();
            //System.out.println("ORIGINAL PATH: " + dirString);

            if (!(dirString.substring(dirString.length() - 1).equals(File.separator))) {
                dirString += File.separator;
            }
            System.out.println();
            System.out.println("Folder Chosen: " + dirString);

            downloadDir = dirString;

            fileWriter(false);

            setDLdirText(downloadDir);

        }//if

        changeButton.setDisable(false);

    }


    @FXML
    public void openFolder(){
        if ( !(downloadDir.equals("")) ) {
            try {
                Desktop.getDesktop().open(new File(downloadDir));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void toggleSettings(){
        if ( Main.getMainStage().isShowing() ){
            Main.getMainStage().hide();
            Main.getSettingsStage().setX(Main.getMainStage().getX());
            Main.getSettingsStage().setY(Main.getMainStage().getY());
            Main.getSettingsStage().show();
        }
        else{
            Main.getSettingsStage().hide();
            Main.getMainStage().setX(Main.getSettingsStage().getX());
            Main.getMainStage().setY(Main.getSettingsStage().getY());
            Main.getMainStage().show();

        }
    }


    private void setup(){
        System.out.println("Settings Directory: " + settDir);

        //check if there is a settings file.
        if ( Files.exists(Paths.get(settDir)) == false ) {
            System.out.println("Settings File Does Not Exist!");
            Platform.runLater(new Runnable(){
                @Override
                public void run() {
                    //Alert
                    Alert alert = new Alert(Alert.AlertType.NONE);
                    alert.getButtonTypes().addAll(ButtonType.YES,ButtonType.NO);
                    alert.setHeaderText("Settings File Does Not Exist!");
                    alert.setContentText("Would you like to create one?\n*Needs app restart to apply");

                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.setAlwaysOnTop(true);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.YES) {
                        fileWriter(true);

                        alert.getButtonTypes().remove(0);
                        alert.getButtonTypes().remove(0);
                        alert.getButtonTypes().add(ButtonType.OK);
                        alert.setHeaderText("Change Settings.");
                        alert.setContentText("Set a download directory and enter your\nOsu! username and password.");
                        alert.showAndWait();
                        toggleSettings();


                    }else {
                        usernameField.setDisable(true);
                        passwordField.setDisable(true);
                        saveButton.setDisable(true);
                        showCBox.setDisable(true);
                        changeButton.setDisable(true);
                        openButton.setDisable(true);
                    }



                }
            });

        }
        else {
            System.out.println("Settings File Exists.");

            //read file and see if it has a download directory.
            Platform.runLater(new Runnable() {
                @Override
                public void run() {

                    settings = fileReader(false);

                    downloadDir = settings.get(0);
                    username = settings.get(1);
                    password = settings.get(2);

                    setDLdirText(downloadDir);
                    usernameField.setText(username);
                    passwordField.setText(password);

                    if ( !(username.equals("")) || !(password.equals("")) ){
                        saveButton.setText("Edit");
                        saveButton.setSelected(true);
                        usernameField.setDisable(true);
                        passwordField.setDisable(true);
                        showCBox.setDisable(true);
                    }

                    /*System.out.println(downloadDir);
                    System.out.println(username);
                    System.out.println(password);*/


                }
            });

        }



    }//setup()


    @FXML
    public void showPassword(){
        if ( showCBox.isSelected() ){
            passwordLabel.setText(passwordField.getText());
        }
        else {
            passwordLabel.setText("");
        }
    }

    @FXML
    public void saveDetails(){
        if ( saveButton.isSelected() ){
            saveButton.setText("Edit");
            usernameField.setDisable(true);
            passwordField.setDisable(true);
            passwordLabel.setText("");
            showCBox.setSelected(false);
            showCBox.setDisable(true);

            username = usernameField.getText();
            password = passwordField.getText();
            fileWriter(false);
        }
        else {
            saveButton.setText("Save");
            usernameField.setDisable(false);
            passwordField.setDisable(false);
            showCBox.setDisable(false);
        }
    }


    public static void fileWriter(boolean NEW){

        if ( NEW == true) {
            if (Files.exists(Paths.get(settDir)) == false) {
                try {
                    Files.createFile(Paths.get(settDir));
                    BufferedWriter writer = Files.newBufferedWriter(Paths.get(settDir), StandardCharsets.UTF_8);
                    writer.write("#Settings File For Osu Beatmap Downloader");
                    writer.newLine();
                    writer.newLine();
                    writer.write("DownloadDirectory:");
                    writer.newLine();
                    writer.write("Username:");
                    writer.newLine();
                    writer.write("Password:");
                    writer.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            //rewrite file
            try {
                Files.deleteIfExists(Paths.get(settDir));
                Files.createFile(Paths.get(settDir));
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(settDir), StandardCharsets.UTF_8);
                writer.write("#Settings File For Osu Beatmap Downloader");
                writer.newLine();
                writer.newLine();
                writer.write("DownloadDirectory:");
                writer.write(downloadDir);
                writer.newLine();
                writer.write("Username:");
                writer.write(username);
                writer.newLine();
                writer.write("Password:");
                writer.write(password);
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }//fileWriter()



        private ArrayList<String> fileReader(boolean raw)//raw means without removing anything from the array
        {

            ArrayList<String> settingsArray = new ArrayList<String>();


            // The name of the file to open.
            String fileName = settDir;

            // This will reference one line at a time
            String line = null;

            try {
                // FileReader reads text files in the default encoding.
                FileReader fileReader =
                        new FileReader(fileName);

                // Always wrap FileReader in BufferedReader.
                BufferedReader bufferedReader =
                        new BufferedReader(fileReader);

                while((line = bufferedReader.readLine()) != null) {
                    settingsArray.add(line);
                }

                // Always close files.
                bufferedReader.close();

            }
            catch(FileNotFoundException ex) {
                System.out.println(
                        "Unable to open file '" +
                                fileName + "'");
            }
            catch(IOException ex) {
                System.out.println(
                        "Error reading file '"
                                + fileName + "'");
            }

            //clean up array
            if (raw == false) {
                settingsArray.remove(0);
                settingsArray.remove(0);

                for (int i = 0; i < settingsArray.size(); i++) {
                    settingsArray.set(i, settingsArray.get(i).substring(settingsArray.get(i).indexOf(':') + 1));
                }
            }

            //copy settingsArray to settings
            return settingsArray;
        }//fileReader()



    public static String getDownloadDir() {
        return downloadDir;
    }

    public void setDLdirText(String t){
        directoryLabel.setText(t);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
