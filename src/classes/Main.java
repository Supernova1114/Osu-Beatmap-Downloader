package classes;

import classes.controller.Controller;
import classes.controller.SettingsController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;

public class Main extends Application implements Runnable {

    static String [] argss;
    static Controller controller;
    static SettingsController settingsController;
    //static String downloadPath = "C:\\Users\\Fart\\Downloads\\OSU";
    static Stage mainStage;
    static Stage settingsStage;
    static Stage helpStage;
    static Parent primaryRoot;
    private static WebScraper scraper = new WebScraper();
    private static boolean isConnected;


    @Override
    public void start(Stage primaryStage) throws Exception{

        //test for connection
        try {
            URL url = new URL("http://www.google.com");
            URLConnection connection = url.openConnection();
            connection.connect();
            isConnected = true;
            System.out.println("Internet Connected");
        } catch (MalformedURLException e) {
            isConnected = false;
            System.out.println("No Internet");
        } catch (IOException e) {
            isConnected = false;
            System.out.println("No Internet");
        }

        if (isConnected == false){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("No Internet Connection Found!");
                alert.showAndWait();
                System.exit(0);
        }

        //make windows
        mainStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Window.fxml"));
        primaryRoot = loader.load();

        /*primaryRoot.prefHeight(700);
        primaryRoot.minHeight(700);
        primaryRoot.maxHeight(700);*/


        Scene scene = new Scene(primaryRoot);

        primaryStage.setResizable(false);


        FXMLLoader loader2 = new FXMLLoader(getClass().getResource("fxml/SettingsWindow.fxml"));
        Parent root2 = loader2.load();
        Scene scene2 = new Scene(root2);
        Stage sett = new Stage();
        sett.setScene(scene2);
        settingsStage = sett;

        settingsStage.setResizable(false);

        FXMLLoader loader3 = new FXMLLoader(getClass().getResource("fxml/HelpWindow.fxml"));
        Parent root3 = loader3.load();
        Scene scene3 = new Scene(root3);
        Stage help = new Stage();
        help.setScene(scene3);
        helpStage = help;

        helpStage.setResizable(false);

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
                startWebScraper(scraper);
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

    public static void startWebScraper(WebScraper scraper){

        if ( isConnected == true ){
            scraper.startChrome();
            startScraping();
        }

    }

    public static void startScraping(){
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                controller.getLoadMoreButton().setDisable(true);
                scraper.run();
                return null;
            }

            @Override
            protected void done() {
                controller.getLoadMoreButton().setDisable(false);
            }
        };
        worker.execute();
    }

}


