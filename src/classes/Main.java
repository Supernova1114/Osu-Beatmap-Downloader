package classes;

import classes.controller.Controller;
import classes.controller.SettingsController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.*;

public class Main extends Application implements Runnable {
    static int startupErrorCode;
    static String [] argss;
    static Controller controller;
    static SettingsController settingsController;
    //static String downloadPath = "C:\\Users\\Fart\\Downloads\\OSU";
    static Stage mainStage;
    static Stage settingsStage;
    static Stage helpStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        mainStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Window.fxml"));
        Parent root = loader.load();

        if ( startupErrorCode == 1 )
            root.setDisable(true);

        Scene scene = new Scene(root);

        primaryStage.setResizable(false);

        FXMLLoader loader2 = new FXMLLoader(getClass().getResource("fxml/SettingsWindow.fxml"));
        Parent root2 = loader2.load();
        Scene scene2 = new Scene(root2);
        Stage sett = new Stage();
        sett.setScene(scene2);
        settingsStage = sett;

        FXMLLoader loader3 = new FXMLLoader(getClass().getResource("fxml/HelpWindow.fxml"));
        Parent root3 = loader3.load();
        Scene scene3 = new Scene(root3);
        Stage help = new Stage();
        help.setScene(scene3);
        helpStage = help;


        primaryStage.setTitle("Osu Beatmap Downloader");
        primaryStage.setScene(scene);
        primaryStage.show();

        controller = (Controller)loader.getController();
        settingsController = (SettingsController) loader2.getController();
        //settingsController.startThread();


            Platform.setImplicitExit(false);

            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    event.consume();
                }
            });



        settingsStage.setOnCloseRequest(event -> {
            settingsController.toggleSettings();
        });


        //System.out.println(controller.gridPane.getChildren().size());

        /*for ( int i=0; i<0; i++ ){
            controller.addButton();
        }*/

        //Start WebScraper
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                startWebScraper();
                return null;
            }
        };
        worker.execute();

    }


    public static void main(String[] args) throws InterruptedException {


        argss = args;
        //start window
        Thread t1 = new Thread(new Main ());
        t1.start();

        //start scraping
        //startWebScraper();

        //launch(argss);
    }


    @Override
    public void run() {
        launch(argss);
    }

   /* public static String getDownloadPath() {
        return downloadPath;
    }*/

    public static Stage getMainStage() {
        return mainStage;
    }

    public static Stage getSettingsStage() {
        return settingsStage;
    }

    public static Stage getHelpStage() {
        return helpStage;
    }

    public static Controller getController() {
        return controller;
    }

    public static SettingsController getSettingsController() {
        return settingsController;
    }

    public static void startWebScraper(){
        WebScraper scraper = new WebScraper();

        startupErrorCode = scraper.startChrome();
        if ( startupErrorCode == 0 )
            scraper.start();


        if ( startupErrorCode == 1 ){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            JOptionPane.showMessageDialog(null,"No Internet Connection!");
            controller.exit();
        }
    }

}


