package classes.controller;

import classes.Main;
import javafx.fxml.FXML;

public class SettingsController {

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





}
