package classes;

import classes.controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    static Controller controller;
    static Map mapController;
    static String downloadPath;
    static FXMLLoader loader2;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Window.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);


        loader2 = new FXMLLoader(getClass().getResource("fxml/MapPane.fxml"));


        controller = (Controller)loader.getController();
        mapController = (Map) loader2.getController();






        primaryStage.setTitle("Osu Beatmap Downloader");
        primaryStage.setScene(scene);
        primaryStage.show();



        //System.out.println(controller.gridPane.getChildren().size());

        /*for ( int i=0; i<0; i++ ){
            controller.addButton();
        }*/
    }


    public static void main(String[] args) {
        WebScraper scraper = new WebScraper();
        scraper.startChrome();
        scraper.start();
        launch(args);
    }


    public static String getDownloadPath() {
        return downloadPath;
    }
}


