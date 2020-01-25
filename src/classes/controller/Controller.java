package classes.controller;

import classes.Main;
import classes.MapPane;
import classes.WebScraper;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
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
    public void download() throws InterruptedException {
        if ( !(Main.getSettingsController().getUsername().equals("")) && !(Main.getSettingsController().getPassword().equals("")) ) {
            for (String map : selectedMaps) {
                WebScraper.driver.get(map + "/download");
                Thread.sleep(1000);
            }
        }
    }

    @FXML
    public void toggleSettings(){
        Main.getSettingsController().toggleSettings();
    }

    public void setProgressBarVisibility(boolean t){
        loadingBar.setVisible(t);
    }

    @FXML
    public void toggleAbout(){

    }

}