package classes.controller;

import classes.Main;
import classes.MapPane;
import classes.WebScraper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.openqa.selenium.By;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    @FXML
    RadioButton osuRadio;
    @FXML
    RadioButton taikoRadio;
    @FXML
    RadioButton catchRadio;
    @FXML
    RadioButton maniaRadio;
    @FXML
    Label progressLabel;


    private boolean osuStartup = true;
    private boolean taikoStartup = true;
    private boolean catchStartup = true;
    private boolean maniaStartup = true;

    private int numSelected;
    private Alert alert;

    private int selectFrom;
    private int selectTo;
    private boolean isFirstSelection;

    private ToggleGroup modeToggles = new ToggleGroup();

    private ArrayList<String> selectedMaps = new ArrayList<String>();//Maps that are selected

    @FXML
    void initialize(){
        loadMoreButton.setDisable(true);
        downloadButton.setDisable(true);
        loadingBar.setVisible(true);
        selectFrom = 0;
        selectTo = 0;
        isFirstSelection = true;

        osuRadio.setFocusTraversable(false);
        taikoRadio.setFocusTraversable(false);
        catchRadio.setFocusTraversable(false);
        maniaRadio.setFocusTraversable(false);

        modeToggles.getToggles().addAll(osuRadio, taikoRadio, catchRadio, maniaRadio);
        modeToggles.getToggles().get(0).setSelected(true);

        osuRadio.setDisable(true);
        taikoRadio.setDisable(true);
        catchRadio.setDisable(true);
        maniaRadio.setDisable(true);

        osuStartup = false;

    }

    @FXML
    public void deselectAll(){
        if ( selectedMaps.size() > 0 ) {
            for (int i = 0; i < gridPane.getChildren().size(); i++) {
                if (gridPane.getChildren().get(i) instanceof MapPane) {
                    ((MapPane) gridPane.getChildren().get(i)).deselect();
                }
            }
            selectedMaps.clear();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    numLabel.setText("0");
                }
            });
            numSelected = 0;
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

                    ArrayList<String> downloadListFull = new ArrayList<String>();
                    for (int i=0; i<selectedMaps.size(); i++){
                        downloadListFull.add(selectedMaps.get(i));
                    }
                /*System.out.println(Main.getSettingsController().getUsername());
                System.out.println(Main.getSettingsController().getPassword() + "PASSS");*/
                    if (Main.getSettingsController().getUsername() != null && Main.getSettingsController().getPassword() != null
                            && !Main.getSettingsController().getUsername().equals("") && !Main.getSettingsController().getPassword().equals("")) {

                        //switcher(4);//4 is download mode

                        //String maplink = WebScraper.driver.findElement(By.xpath("//a[@class='download']")).getAttribute("href");
                        /*int progressNum = downloadListFull.size();
                        int finalProgressNum = progressNum;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                progressLabel.setText(finalProgressNum + "");
                            }
                        });*/

                        ArrayList<ArrayList<String>> downloadListList = new ArrayList<>();

                        int maxThreads = 100;
                        for (int i=0; i < maxThreads; i++){
                            downloadListList.add(new ArrayList<>());
                        }

                        /*ArrayList<String> downloadListPart1 = new ArrayList<>();
                        ArrayList<String> downloadListPart2 = new ArrayList<>();
                        ArrayList<String> downloadListPart3 = new ArrayList<>();
                        ArrayList<String> downloadListPart4 = new ArrayList<>();
                        ArrayList<String> downloadListPart5 = new ArrayList<>();
                        ArrayList<String> downloadListPart6 = new ArrayList<>();*/
                        //System.out.println(downloadListList.size());

                        int j = 0;
                        for (int i=0; i<downloadListFull.size(); i++){

                            if (j >= downloadListList.size()) {
                                j = 0;
                            }
                            downloadListList.get(j).add(downloadListFull.get(i));
                            j++;


                            /*if (i < downloadListFull.size()){
                                downloadListPart1.add(downloadListFull.get(i));
                                i++;
                            }
                            if (i < downloadListFull.size()){
                                downloadListPart2.add(downloadListFull.get(i));
                                i++;
                            }
                            if (i < downloadListFull.size()){
                                downloadListPart3.add(downloadListFull.get(i));
                                i++;
                            }
                            if (i < downloadListFull.size()){
                                downloadListPart4.add(downloadListFull.get(i));
                                i++;
                            }
                            if (i < downloadListFull.size()){
                                downloadListPart5.add(downloadListFull.get(i));
                                i++;
                            }
                            if (i < downloadListFull.size()){
                                downloadListPart6.add(downloadListFull.get(i));
                                i++;
                            }*/
                        }
                        System.out.println(downloadListFull);
                        System.out.println();
                        System.out.println(downloadListList);
                        System.out.println();


                        //Asynchronous Downloads Workers
                        ArrayList<SwingWorker> workerList = new ArrayList<>();

                        for (int i=0; i<downloadListList.size(); i++){

                            ArrayList<String> downloadListPart = downloadListList.get(i);

                            if (downloadListPart.size() > 0){
                                workerList.add( new SwingWorker() {
                                    @Override
                                    protected Object doInBackground() throws Exception {

                                    /*ArrayList<String> downloadListPartTemp = new ArrayList<>();
                                    Collections.copy(downloadListPart, downloadListPartTemp);*/


                                        for (String map : downloadListPart) {
                                            //DOWNLOAD MAPS

                                            //WebScraper.driver.get(maplink);
                                            //WebScraper.driver.get("https://beatconnect.io/b/" + map.substring(map.indexOf("beatmapsets/") + 12));//DOWNLOAD LINK (orig "map + "/download"")
                                            String FILE_NAME = SettingsController.getDownloadDir() + map.substring(map.indexOf("beatmapsets/") + 12) + ".osz";
                                            System.out.println(FILE_NAME);
                                            String FILE_URL = "https://beatconnect.io/b/" + map.substring(map.indexOf("beatmapsets/") + 12);

                                            try (BufferedInputStream in = new BufferedInputStream(new URL(FILE_URL).openStream());
                                                 FileOutputStream fileOutputStream = new FileOutputStream(FILE_NAME)) {
                                                byte dataBuffer[] = new byte[1024];
                                                int bytesRead;
                                                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                                                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                                                }
                                            } catch (IOException e) {
                                                // handle exception
                                                e.printStackTrace();
                                            }
                                        }
                                        return null;
                                    }
                                } );
                            }


                        }

                        /*int n = 20; // Maximum number of threads*/
                        ExecutorService threadPool = Executors.newFixedThreadPool(maxThreads + 100);//Makes a max threads thread pool

                        System.out.println("Worker List Size: " + workerList.size());
                        for (int i=0; i < workerList.size(); i++){
                            threadPool.submit(workerList.get(i));
                            workerList.get(i).execute();
                        }



                        //Execute the download process
                        /*if (downloadListPart1.size() > 0)
                            downloadWorker1.execute();
                        if (downloadListPart2.size() > 0)
                            downloadWorker2.execute();
                        if (downloadListPart3.size() > 0)
                            downloadWorker3.execute();
                        if (downloadListPart4.size() > 0)
                            downloadWorker4.execute();
                        if (downloadListPart5.size() > 0)
                            downloadWorker5.execute();
                        if (downloadListPart6.size() > 0)
                            downloadWorker6.execute();*/



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


    @FXML
    public void modeRadioSwitcher(){
        osuRadio.setDisable(true);
        taikoRadio.setDisable(true);
        catchRadio.setDisable(true);
        maniaRadio.setDisable(true);
        
        RadioButton button = (RadioButton)modeToggles.getSelectedToggle();
        switch (button.getText()){// FIXME: 3/21/2020 Finish by adding the ability to make new tabs and switch between them in order to load maps for each diff mode using their necessary links.
            case "osu!":
                System.out.println(0);
                switcher(0);
                break;
            case "taiko":
                System.out.println(1);
                switcher(1);
                break;
            case "catch":
                System.out.println(2);
                switcher(2);
                break;
            case "mania":
                System.out.println(3);
                switcher(3);
                break;
        }
    }

    public void switcher(int mode){

        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {

                WebScraper.setSwitched(true);

                WebScraper.switchTabs(mode);


                WebScraper.setScraperStop(true);
                while (WebScraper.getScraperDone() == false){
                    //wait until scraper Thread Finished
                }
                System.out.println(WebScraper.getScraperDone());

                if ( mode == 1 && taikoStartup) {
                    WebScraper.driver.get("https://osu.ppy.sh/beatmapsets?m=" + mode + "&s=ranked");
                    taikoStartup = false;
                }
                if ( mode == 2 && catchStartup) {
                    WebScraper.driver.get("https://osu.ppy.sh/beatmapsets?m=" + mode + "&s=ranked");
                    catchStartup = false;
                }
                if ( mode == 3 && maniaStartup) {
                    WebScraper.driver.get("https://osu.ppy.sh/beatmapsets?m=" + mode + "&s=ranked");
                    maniaStartup = false;
                }



                WebScraper.numLoaded = 0;
                WebScraper.totalNumLoaded = 0;
                WebScraper.setIsFirstRun(true);
                WebScraper.resetMapNumber();

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        gridPane.getChildren().clear();
                        numLoaded.setText("0");
                        selectLoadLabel.setText("All: 0");
                    }
                });





                return null;
            }

            @Override
            protected void done() {
                WebScraper.setScraperStop(false);

                osuRadio.setDisable(false);
                taikoRadio.setDisable(false);
                catchRadio.setDisable(false);
                maniaRadio.setDisable(false);
                System.out.println("Success!!!!!");

                WebScraper.setSwitched(false);

                Main.startScraping();
            }
        };
        worker.execute();
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

    public RadioButton getOsuRadio() {
        return osuRadio;
    }

    public RadioButton getTaikoRadio() {
        return taikoRadio;
    }

    public RadioButton getCatchRadio() {
        return catchRadio;
    }

    public RadioButton getManiaRadio() {
        return maniaRadio;
    }


}