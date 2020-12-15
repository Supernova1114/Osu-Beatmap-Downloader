package classes.controller;

import classes.Main;
import classes.WebScraper;
import com.sun.javafx.collections.VetoableListDecorator;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SettingsController{

    private static String settDir = System.getProperty("user.dir") + File.separator +"settings.txt";
    private static ArrayList<String> settings;
    public static String downloadDir;
    public static String profileDir = "";
    private static String username;
    private static String password;
    private static boolean isHeadless = true;
    private static boolean useProfile = true;
    private static Alert alert;





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
    CheckBox headlessCBox;

    //for profile
    @FXML
    Label profileDirectoryLabel;
    @FXML
    Button changeButton1;
    @FXML
    Button openButton1;
    @FXML
    CheckBox profileCBox;
    @FXML
    Hyperlink helpLink;

    @FXML
    public void openHelp(){
        Main.getController().openHelp();
    }

    @FXML
    public void browseDLDir(){
        downloadDir = browseFiles();
        setDLdirText(downloadDir);
        fileWriter(false);
    }

    @FXML
    public void browseProfDir(){
        profileDir = browseFiles();
        setProfileDirText(profileDir);
        fileWriter(false);
    }

    //For browsing files
    public String browseFiles(){

        changeButton.setDisable(true);
        changeButton1.setDisable(true);

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

            changeButton.setDisable(false);
            changeButton1.setDisable(false);

            return dirString;
        }//if

        changeButton.setDisable(false);
        changeButton1.setDisable(false);

        return "";
    }


    @FXML
    public void openDLFolder(){
        openFolder(downloadDir);
    }

    @FXML
    public void openProfFolder(){
        openFolder(profileDir);
    }

    public void openFolder(String t){
        if ( !(t.equals("")) ) {
            try {
                Desktop.getDesktop().open(new File(t));
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

    @FXML
    public void SetHeadlessMode(){
        isHeadless = headlessCBox.isSelected();
        fileWriter(false);
    }

    @FXML
    public void SetProfileMode(){
        useProfile = profileCBox.isSelected();

        if (useProfile){
            usernameField.setDisable(true);
            passwordField.setDisable(true);
            saveButton.setDisable(true);
        }else {
            saveButton.setDisable(false);
            if (!saveButton.isSelected()){
                usernameField.setDisable(false);
                passwordField.setDisable(false);
            }
        }

        fileWriter(false);
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
                    alert = new Alert(Alert.AlertType.NONE);
                    alert.getButtonTypes().addAll(ButtonType.YES,ButtonType.NO);
                    alert.setHeaderText("Settings File Does Not Exist!");
                    alert.setContentText("Would you like to create one?");

                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.setAlwaysOnTop(false);


                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.YES) {
                        username = "";
                        password = "";
                        downloadDir = "";
                        fileWriter(true);



                        /*alert.getButtonTypes().remove(0);
                        alert.getButtonTypes().remove(0);
                        alert.getButtonTypes().addAll(ButtonType.YES,ButtonType.NO);
                        alert.setHeaderText("Use Profile? (Recommended)");
                        alert.setContentText("Using a chrome profile will allow access to\nOsu! website as long as you are logged into Osu!\n in your Chrome Browser. Otherwise you must login with Osu! in this app.");
                        alert.showAndWait();*/
                        toggleSettings();

                        Stage updateAlertStage = (Stage)Main.alert.getDialogPane().getScene().getWindow();
                        updateAlertStage.requestFocus();

                    }
                    else {
                        showCBox.setDisable(true);
                        changeButton.setDisable(true);
                        openButton.setDisable(true);
                        headlessCBox.setDisable(true);
                        profileCBox.setDisable(true);
                        openButton1.setDisable(true);
                        changeButton1.setDisable(true);

                    }

                    usernameField.setDisable(true);
                    passwordField.setDisable(true);
                    saveButton.setDisable(true);


                    /*alert.getButtonTypes().remove(0);
                    alert.getButtonTypes().remove(0);*/

                    //Shows driver alert
                    //showChromeDriverAlert();

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
                    isHeadless = Boolean.parseBoolean(settings.get(3));
                    useProfile = Boolean.parseBoolean(settings.get(4));
                    profileDir = settings.get(5);

                    WebScraper.setHeadless(isHeadless);

                    setDLdirText(downloadDir);
                    setProfileDirText(profileDir);
                    usernameField.setText(username);
                    passwordField.setText(password);
                    headlessCBox.setSelected(isHeadless);
                    profileCBox.setSelected(useProfile);

                        /*usernameField.setDisable(useProfile);
                        passwordField.setDisable(useProfile);*/
                        saveButton.setDisable(useProfile);



                    if ( !(username.equals("")) || !(password.equals("")) ){
                        saveButton.setText("Edit");
                        saveButton.setSelected(true);
                        showCBox.setDisable(true);
                    }

                    if (saveButton.isSelected() || useProfile) {
                        usernameField.setDisable(true);
                        passwordField.setDisable(true);
                    }

                    /*System.out.println(downloadDir);
                    System.out.println(username);
                    System.out.println(password);*/

                   //showChromeDriverAlert();
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
                    writer.newLine();
                    writer.write("Headless:true");
                    writer.newLine();
                    writer.write("UseProfile:true");
                    writer.newLine();
                    writer.write("ProfileDirectory:");
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
                writer.newLine();
                writer.write("Headless:");
                writer.write(isHeadless + "");
                writer.newLine();
                writer.write("UseProfile:");
                writer.write(useProfile + "");
                writer.newLine();
                writer.write("ProfileDirectory:");
                writer.write(profileDir);
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


    /*public static void showChromeDriverAlert(){
        alert.getButtonTypes().addAll(new ButtonType("Browser"), new ButtonType("Driver"), ButtonType.CLOSE);
        alert.setHeaderText("Important!");
        alert.setContentText("Make sure that Chrome browser and Chromedriver are\nthe same version.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == alert.getButtonTypes().get(0)) {
            //if browser
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(new URI("https://chrome://version/"));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }

    }*/


    public static boolean getUseProfile(){
        return useProfile;
    }

    public static String getDownloadDir() {
        return downloadDir;
    }
    public static String getProfileDir(){ return profileDir; }

    public void setDLdirText(String t){
        directoryLabel.setText(t);
    }
    public void setProfileDirText(String t){
        profileDirectoryLabel.setText(t);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public static Alert getAlert() {
        return alert;
    }
}
