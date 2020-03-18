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
import java.io.IOException;
import java.lang.management.ManagementFactory;
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
    @FXML
    Button loadMoreButton;
    @FXML
    Label numLoaded;
    @FXML
    CheckMenuItem selectAllCheck;
    @FXML
    MenuItem selectLoadLabel;

    private int numSelected;
    private Alert alert;

    private int selectFrom;
    private int selectTo;
    private boolean isFirstSelection;

    private ArrayList<String> selectedMaps = new ArrayList<String>();

    @FXML
    void initialize(){
        loadMoreButton.setDisable(true);
        downloadButton.setDisable(true);
        loadingBar.setVisible(true);
        selectFrom = 0;
        selectTo = 0;
        isFirstSelection = true;

    }

    @FXML
    public void deselectAll(){
        if ( selectedMaps.size() > 0 ) {
            for (int i = 0; i < gridPane.getChildren().size(); i++) {
                if (gridPane.getChildren().get(i) instanceof MapPane) {
                    ((MapPane) gridPane.getChildren().get(i)).deselect();
                }
            }
            System.out.println("Selected maps after deselect: " + selectedMaps);
        }
    }

    @FXML
    public void selectLoad(){
        for (int i = 0; i < gridPane.getChildren().size(); i++) {
            if (gridPane.getChildren().get(i) instanceof MapPane) {
                ((MapPane) gridPane.getChildren().get(i)).select();
            }
        }
    }


    public void addChildren(int column, Node map)throws Exception{
        //for (int i =1; i<200; i++){
            gridPane.addColumn(column,map);
        //}
    }

    public void changeSelected(int add){
        numSelected += add;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                numLabel.setText(numSelected + "");
            }
        });


        if ( numSelected > 0 ){
            downloadButton.setDisable(false);
        }
        else{
            downloadButton.setDisable(true);
        }
    }

    public void exit(){
        try{
        WebScraper.driver.quit();
        } catch (Exception e) {
            try {
                Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
            } catch (IOException r) {
                e.printStackTrace();
            }

            String idTemp = ManagementFactory.getRuntimeMXBean().getName();
            int id = Integer.parseInt(idTemp.substring(0, idTemp.indexOf("@")));
            System.out.println(id);

            try {
                Runtime.getRuntime().exec("taskkill /f /fi \"PID eq " + id + "\" /t");
            } catch (IOException r) {
                e.printStackTrace();
            }
        }


        /*String idTemp = ManagementFactory.getRuntimeMXBean().getName();
        int id = Integer.parseInt(idTemp.substring(0, idTemp.indexOf("@")));
        System.out.println(id);*/

       /* try {
            Runtime.getRuntime().exec("taskkill /F /IM chrome.exe");
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        //Runtime.getRuntime().halt(0);
        //Runtime.getRuntime().exit(0);


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

        if ( WebScraper.getIsLoggedIn() == true ) {

            SwingWorker sw1 = new SwingWorker() {

                @Override
                protected String doInBackground() throws Exception {
                    // define what thread will do here
                    downloadButton.setDisable(true);

                    ArrayList<String> downloadList = new ArrayList<String>();
                    for (int i=0; i<selectedMaps.size(); i++){
                        downloadList.add(selectedMaps.get(i));
                    }
                /*System.out.println(Main.getSettingsController().getUsername());
                System.out.println(Main.getSettingsController().getPassword() + "PASSS");*/
                    if (Main.getSettingsController().getUsername() != null && Main.getSettingsController().getPassword() != null
                            && !Main.getSettingsController().getUsername().equals("") && !Main.getSettingsController().getPassword().equals("")) {
                        for (String map : downloadList) {
                            WebScraper.driver.get(map + "/download");
                            Thread.sleep(500);
                        }
                    } else {
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
                protected void done() {
                    // this method is called when the background
                    // thread finishes execution
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                alert.showAndWait();
                            } catch (Exception e) {
                            }
                            //toggleSettings();
                        }
                    });

                    downloadButton.setDisable(false);

                }
            };

            // executes the swingworker on worker thread
            sw1.execute();

        }//if
        else {

            Platform.runLater(new Runnable(){
                @Override
                public void run() {
                    Alert alert = new Alert(Alert.AlertType.NONE);
                    alert.getButtonTypes().addAll(ButtonType.OK);
                    alert.setHeaderText("You are not logged in!");

                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.setAlwaysOnTop(true);

                    alert.setX(Main.getMainStage().getX()+125);
                    alert.setY(Main.getMainStage().getY()+146);

                    alert.showAndWait();

                }
            });

        }

    }//download()

    public void setShiftSelected(int mapNumber){
        if ( isFirstSelection == true ){
            selectFrom = mapNumber;
            isFirstSelection = false;
            System.out.println(selectFrom);
        }else {
            selectTo = mapNumber;
            isFirstSelection = true;
            System.out.println(selectTo);
            selectMaps();
        }

    }

    public void selectMaps(){
        if ( selectFrom > selectTo ){
            for ( int i=selectTo; i<selectFrom+1; i++ ) {
                if (gridPane.getChildren().get(i+1) instanceof MapPane){
                    ((MapPane) gridPane.getChildren().get(i+1)).selectToggle();
                    System.out.println(i+1);
                    System.out.println(((MapPane) gridPane.getChildren().get(i+1)).title.getText());

                }
            }
        }

        if ( selectFrom < selectTo ){
            for ( int i=selectFrom; i<selectTo+1; i++ ){
                if (gridPane.getChildren().get(i+1) instanceof MapPane){
                    ((MapPane) gridPane.getChildren().get(i+1)).selectToggle();
                    System.out.println(i+1);
                    System.out.println(((MapPane) gridPane.getChildren().get(i+1)).title.getText());
                }
            }
        }


    }


    public void setNumLoadedLabel(int num){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                numLoaded.setText(String.valueOf(num));
            }
        });
    }

    public void setSelectMenuNum(int num){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                selectLoadLabel.setText("All: " + String.valueOf(num*2));
            }
        });
    }

    @FXML
    public void loadMore(){
        Main.startScraping();
    }


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

    public GridPane getGridPane() {
        return gridPane;
    }

    public Button getLoadMoreButton() {
        return loadMoreButton;
    }

    public CheckMenuItem getSelectAllCheck() {
        return selectAllCheck;
    }
}