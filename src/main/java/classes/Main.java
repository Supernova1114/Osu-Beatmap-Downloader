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
import javafx.stage.WindowEvent;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.*;
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
    public static JMetro jMetro;
    public static Alert alert;


    ////////////////////////
    //////Current Version of Application
    public static final double currentVersion = 1.5;
    ///////MAKE SURE TO CHANGE THIS WITH EVERY NEW UPDATE
    ///////////////////////


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
        else {
            try {
                tryUpdate();
            }catch (Exception e){
                e.printStackTrace();
            }
        }


        //make windows
        mainStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Window.fxml"));
        primaryRoot = loader.load();

        Scene scene = new Scene(primaryRoot);

        primaryStage.setResizable(false);


        FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/fxml/SettingsWindow.fxml"));
        Parent root2 = loader2.load();
        Scene scene2 = new Scene(root2);
        Stage sett = new Stage();
        sett.setScene(scene2);
        settingsStage = sett;

        settingsStage.setResizable(false);
        settingsStage.setTitle("Settings");

        FXMLLoader loader3 = new FXMLLoader(getClass().getResource("/fxml/HelpWindow.fxml"));
        Parent root3 = loader3.load();
        Scene scene3 = new Scene(root3);
        Stage help = new Stage();
        help.setScene(scene3);
        helpStage = help;

        helpStage.setResizable(false);
        helpStage.setTitle("Help");


        primaryStage.setTitle("Osu Beatmap Downloader");

        primaryStage.setScene(scene);

        //Set Main window to Edited light style (Text is white on maps instead of black)
        jMetro = new JMetro(Style.LIGHT);
        jMetro.setScene(scene);

        //Dark style is using original light style css so that text is black. (Settings and help window)
        JMetro jMetro2 = new JMetro(Style.DARK);
        jMetro2.setScene(scene2);
        jMetro2.setScene(scene3);


        primaryStage.show();



        controller = (Controller)loader.getController();
        settingsController = (SettingsController) loader2.getController();
        //settingsController.startThread();


            Platform.setImplicitExit(false);

            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    /*event.consume();*/
                    controller.exit();
                }
            });



        settingsStage.setOnCloseRequest(event -> {
            settingsController.toggleSettings();
        });



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

    public static WebScraper getScraper() {
        return scraper;
    }


    //Check if there is an update and tell user
    public void tryUpdate() throws IOException {
        String htmlLink = "https://pastebin.com/raw/M8jFMZ6j";

        Document doc = Jsoup.connect(htmlLink).get();
        double latestVersion = Double.parseDouble(doc.text());
        System.out.println("Latest Version: " + latestVersion);
        System.out.println("Current Version: " + currentVersion);
        
        if (latestVersion > currentVersion){
            Platform.runLater(new Runnable(){
                @Override
                public void run() {
                    alert = new Alert(Alert.AlertType.NONE);

                    alert.getButtonTypes().addAll(new ButtonType("Update"), ButtonType.CLOSE);
                    alert.setTitle("Update Available!");
                    alert.setHeaderText("A new Version is available!");
                    alert.setContentText("It is highly recommended to update otherwise\nthings may not work correctly.\nCurrent: v" + currentVersion + "\nLatest: v" + latestVersion);

                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.setAlwaysOnTop(false);

                    alert.setX(Main.getMainStage().getX()+125);
                    alert.setY(Main.getMainStage().getY()+146);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == alert.getButtonTypes().get(0)) {
                        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                            try {
                                Desktop.getDesktop().browse(new URI("https://github.com/Supernova1114/Osu-Beatmap-Downloader/releases"));
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            });
        }

    }



    //Start Chrome and then start the webscraping process
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
                if ( !WebScraper.isSwitched() )
                controller.getLoadMoreButton().setDisable(false);
            }
        };
        worker.execute();
    }

}


