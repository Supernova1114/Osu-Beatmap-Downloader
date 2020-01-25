package classes.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;


public class Controller {

    @FXML
    GridPane gridPane;
    @FXML
    ScrollPane scrollPane;


    public void addChildren(int column, Node map)throws Exception{
        //for (int i =1; i<200; i++){
            gridPane.addColumn(column,map);
        //}
    }



}