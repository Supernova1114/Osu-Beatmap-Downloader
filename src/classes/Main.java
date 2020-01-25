package classes;

import classes.controller.Controller;
import classes.controller.SettingsController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;

public class Main extends Application implements Runnable {
    static int startupErrorCode;
    static String [] argss;
    static Controller controller;
    static SettingsController settingsController;
    static String downloadPath = "C:\\Users\\Fart\\Downloads\\OSU";
    static Stage mainStage;
    static Stage settingsStage;
    static Stage loadingStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        mainStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Window.fxml"));
        Parent root = loader.load();

        if ( startupErrorCode == 1 )
            root.setDisable(true);

        Scene scene = new Scene(root);


        FXMLLoader loader2 = new FXMLLoader(getClass().getResource("fxml/SettingsWindow.fxml"));
        Parent root2 = loader2.load();
        Scene scene1 = new Scene(root2);
        Stage sett = new Stage();
        sett.setScene(scene1);
        settingsStage = sett;




        controller = (Controller)loader.getController();
        settingsController = (SettingsController) loader2.getController();




        primaryStage.setOnCloseRequest(event ->{
            controller.exit();
        });

        settingsStage.setOnCloseRequest(event -> {
            settingsController.toggleSettings();
        });

        primaryStage.setTitle("Osu Beatmap Downloader");
        primaryStage.setScene(scene);
        primaryStage.show();




        //System.out.println(controller.gridPane.getChildren().size());

        /*for ( int i=0; i<0; i++ ){
            controller.addButton();
        }*/
    }


    public static void main(String[] args) throws InterruptedException {
        WebScraper scraper = new WebScraper();

        startupErrorCode = scraper.startChrome();
        if ( startupErrorCode == 0 )
            scraper.start();

        argss = args;

        Thread t1 = new Thread(new Main ());
        t1.start();


        if ( startupErrorCode == 1 ){
            Thread.sleep(2000);
            JOptionPane pane = new JOptionPane();
            JOptionPane.showMessageDialog(null,"No Internet Connection!");
            controller.exit();
        }

        //launch(argss);
    }


    @Override
    public void run() {
        launch(argss);
    }

    public static String getDownloadPath() {
        return downloadPath;
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static Stage getSettingsStage() {
        return settingsStage;
    }

    public static Controller getController() {
        return controller;
    }

    public static SettingsController getSettingsController() {
        return settingsController;
    }
}


