package classes.controller;

import classes.Main;
import classes.MapPane;
import classes.WebScraper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


public class Controller{

    @FXML
    GridPane gridPane;
    @FXML
    ScrollPane scrollPane;
    @FXML
    Label numLabel;
    @FXML
    Button downloadButton;
    @FXML
    ProgressBar loadingBar;

    private int numSelected;
    private Alert alert;

    private ArrayList<String> selectedMaps = new ArrayList<String>();

    @FXML
    void initialize(){
        downloadButton.setDisable(true);
        loadingBar.setVisible(true);
    }


    public void addChildren(int column, Node map)throws Exception{
        //for (int i =1; i<200; i++){
            gridPane.addColumn(column,map);
        //}
    }

    public void changeSelected(int add){
        numSelected += add;
        numLabel.setText(numSelected + "");

        if ( numSelected > 0 ){
            downloadButton.setDisable(false);
        }
        else{
            downloadButton.setDisable(true);
        }
    }

    public void exit(){
        try{
        WebScraper.driver.quit();}
        catch (Exception e){e.printStackTrace();}

        Main.getMainStage().close();
        System.exit(0);

    }

    public int getNumSelected() {
        return numSelected;
    }

    public ArrayList<String> getSelectedMaps() {
        return selectedMaps;
    }

    public void toggleSelectedMaps(String map){
        if ( selectedMaps.contains(map) ){
            selectedMaps.remove(map);
        }
        else{
            selectedMaps.add(map);
        }
    }



    @FXML
    public void download(){

        SwingWorker sw1 = new SwingWorker()
        {

            @Override
            protected String doInBackground() throws Exception
            {
                // define what thread will do here
                downloadButton.setDisable(true);
                if ( !(Main.getSettingsController().getUsername().equals("")) && !(Main.getSettingsController().getPassword().equals("")) ) {
                    for (String map : selectedMaps) {
                        WebScraper.driver.get(map + "/download");
                        Thread.sleep(500);
                    }
                }
                else {
                    //Tell user to log in
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            alert = new Alert(Alert.AlertType.NONE);
                            alert.getButtonTypes().addAll(ButtonType.OK);
                            alert.setHeaderText("You are not logged in!");
                            alert.setContentText("You will not be able to download maps\nwithout logging in.");

                            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                            stage.setAlwaysOnTop(true);


                        }
                    });

                }



                return "";
            }

            @Override
            protected void done()
            {
                // this method is called when the background
                // thread finishes execution
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        alert.showAndWait();
                        toggleSettings();
                    }
                });

                downloadButton.setDisable(false);

            }
        };

        // executes the swingworker on worker thread
        sw1.execute();

    }//download()




    @FXML
    public void openHelp(){
        Main.getHelpStage().show();
    }

    @FXML
    public void toggleSettings(){
        Main.getSettingsController().toggleSettings();
    }

    public void setProgressBarVisibility(boolean t){
        loadingBar.setVisible(t);
    }


}