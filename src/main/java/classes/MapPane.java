package classes;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import jfxtras.styles.jmetro.JMetro;

public class MapPane extends Pane {

    protected String mapLink;
    protected int mapNumber;
    protected final Rectangle rectangle;
    protected final Rectangle sensor;
    protected final ImageView imageView;
    //private ImageView exLink;
    protected final Rectangle rectangle0;
    public Label title;
    protected final DropShadow dropShadow;
    protected final Label label;
    protected final DropShadow dropShadow0;
    /*protected final Label label0;
    protected final Label label1;*/
    /*protected final Label label2;
    protected final Label label3;*/
    protected final Circle circle1;
    protected final Circle circle2;
    protected final Circle circle3;
    protected final Circle circle4;
    protected final Circle circle5;
    protected final Circle circle6;
    protected final Circle circle7;
    protected final Circle circle8;
    protected final Circle circle9;
    protected final Circle circle10;


    private double mapHeight = 110.0;//130.0
    private double circleLayoutY = 93;//114.0

    public MapPane() throws Exception {



        rectangle = new Rectangle();
        sensor = new Rectangle();
        imageView = new ImageView();
        rectangle0 = new Rectangle();
        title = new Label();
        dropShadow = new DropShadow();
        label = new Label();
        dropShadow0 = new DropShadow();
        /*label0 = new Label();
        label1 = new Label();*/
        /*label2 = new Label();
        label3 = new Label();*/
        circle1 = new Circle();
        circle2 = new Circle();
        circle3 = new Circle();
        circle4 = new Circle();
        circle5 = new Circle();
        circle6 = new Circle();
        circle7 = new Circle();
        circle8 = new Circle();
        circle9 = new Circle();
        circle10 = new Circle();


        setPrefHeight(mapHeight);
        setPrefWidth(277.0);

        rectangle.setFill(javafx.scene.paint.Color.WHITE);
        rectangle.setHeight(mapHeight);
        rectangle.setStroke(javafx.scene.paint.Color.BLACK);
        rectangle.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
        rectangle.setStrokeWidth(0.0);
        rectangle.setWidth(277.0);

        sensor.setFill(Color.TRANSPARENT);
        sensor.setHeight(mapHeight);
        sensor.setStroke(Color.TRANSPARENT);
        sensor.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
        sensor.setStrokeWidth(0.0);
        sensor.setWidth(277.0);

        imageView.setFitHeight(76.0);//76.0
        imageView.setFitWidth(277.0);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(false);
        //imageView.setImage(new Image(getClass().getResource("../card.jpg").toExternalForm()));

        //exLink = new ImageView(new Image(getClass().getResource("../Link.png").toExternalForm()));
        //exLink.setFitHeight(10);
        //exLink.setLayoutX(240);
        //exLink.setLayoutY(5);
        //exLink.setPreserveRatio(true);


        rectangle0.setHeight(76.0);//69.5
        rectangle0.setOpacity(1);
        rectangle0.setStroke(javafx.scene.paint.Color.BLACK);
        rectangle0.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
        rectangle0.setStrokeWidth(0.0);
        rectangle0.setWidth(277.0);
        Stop[] stops = new Stop[]{new Stop(0, Color.BLACK),new Stop(1,Color.TRANSPARENT)};
        LinearGradient gradient = new LinearGradient(0.5,1,0.5,0,true, CycleMethod.NO_CYCLE, stops);
        rectangle0.setFill(gradient);


        title.setLayoutX(4.0);
        title.setLayoutY(24.0);
        title.setText("Title");
        title.setTextFill(Color.WHITE);
        title.setFont(new Font("System Bold Italic", 18.0));
        title.setMaxWidth(270);

        title.setEffect(dropShadow);

        label.setLayoutX(4.0);
        label.setLayoutY(46.0);
        label.setText("Header");
        label.setTextFill(javafx.scene.paint.Color.WHITE);
        label.setFont(new Font("System Bold Italic", 13.0));
        label.setMaxWidth(270);

        label.setEffect(dropShadow0);

        /*label0.setLayoutX(5.0);
        label0.setLayoutY(70.0);
        label0.setText("mapped by:");
        label0.setFont(new Font("System Bold Italic", 12.0));

        label1.setLayoutX(74.0);
        label1.setLayoutY(70.0);
        label1.setText("mapper");
        label1.setFont(new Font("System Bold Italic", 12.0));*/

        /*label2.setLayoutX(4.0);
        label2.setLayoutY(84.0);
        label2.setText("Type:");
        label2.setFont(new Font("System Bold Italic", 12.0));

        label3.setLayoutX(38.0);
        label3.setLayoutY(84.0);
        label3.setText("osu!");
        label3.setFont(new Font("System Bold Italic", 12.0));*/

        circle1.setFill(javafx.scene.paint.Color.TRANSPARENT);
        circle1.setLayoutX(16.0);
        circle1.setLayoutY(circleLayoutY);
        circle1.setRadius(11.0);
        circle1.setStroke(javafx.scene.paint.Color.BLACK);
        circle1.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
        circle1.setStrokeWidth(4.0);

        circle2.setFill(javafx.scene.paint.Color.TRANSPARENT);
        circle2.setLayoutX(40.0);
        circle2.setLayoutY(circleLayoutY);
        circle2.setRadius(11.0);
        circle2.setStroke(javafx.scene.paint.Color.BLACK);
        circle2.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
        circle2.setStrokeWidth(4.0);

        circle3.setFill(javafx.scene.paint.Color.TRANSPARENT);
        circle3.setLayoutX(64.0);
        circle3.setLayoutY(circleLayoutY);
        circle3.setRadius(11.0);
        circle3.setStroke(javafx.scene.paint.Color.BLACK);
        circle3.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
        circle3.setStrokeWidth(4.0);

        circle4.setFill(javafx.scene.paint.Color.TRANSPARENT);
        circle4.setLayoutX(88.0);
        circle4.setLayoutY(circleLayoutY);
        circle4.setRadius(11.0);
        circle4.setStroke(javafx.scene.paint.Color.BLACK);
        circle4.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
        circle4.setStrokeWidth(4.0);

        circle5.setFill(javafx.scene.paint.Color.TRANSPARENT);
        circle5.setLayoutX(112.0);
        circle5.setLayoutY(circleLayoutY);
        circle5.setRadius(11.0);
        circle5.setStroke(javafx.scene.paint.Color.BLACK);
        circle5.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
        circle5.setStrokeWidth(4.0);

        circle6.setFill(javafx.scene.paint.Color.TRANSPARENT);
        circle6.setLayoutX(136.0);
        circle6.setLayoutY(circleLayoutY);
        circle6.setRadius(11.0);
        circle6.setStroke(javafx.scene.paint.Color.BLACK);
        circle6.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
        circle6.setStrokeWidth(4.0);

        circle7.setFill(javafx.scene.paint.Color.TRANSPARENT);
        circle7.setLayoutX(160.0);
        circle7.setLayoutY(circleLayoutY);
        circle7.setRadius(11.0);
        circle7.setStroke(javafx.scene.paint.Color.BLACK);
        circle7.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
        circle7.setStrokeWidth(4.0);

        circle8.setFill(javafx.scene.paint.Color.TRANSPARENT);
        circle8.setLayoutX(184.0);
        circle8.setLayoutY(circleLayoutY);
        circle8.setRadius(11.0);
        circle8.setStroke(javafx.scene.paint.Color.BLACK);
        circle8.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
        circle8.setStrokeWidth(4.0);

        circle9.setFill(javafx.scene.paint.Color.TRANSPARENT);
        circle9.setLayoutX(208.0);
        circle9.setLayoutY(circleLayoutY);
        circle9.setRadius(11.0);
        circle9.setStroke(javafx.scene.paint.Color.BLACK);
        circle9.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
        circle9.setStrokeWidth(4.0);

        circle10.setFill(javafx.scene.paint.Color.TRANSPARENT);
        circle10.setLayoutX(232.0);
        circle10.setLayoutY(circleLayoutY);
        circle10.setRadius(11.0);
        circle10.setStroke(javafx.scene.paint.Color.BLACK);
        circle10.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
        circle10.setStrokeWidth(4.0);


        getChildren().add(rectangle);
        getChildren().add(imageView);
        getChildren().add(rectangle0);
        //getChildren().add(exLink);
        getChildren().add(title);
        getChildren().add(label);
        /*getChildren().add(label0);
        getChildren().add(label1);*/
        /*getChildren().add(label2);
        getChildren().add(label3);*/
        getChildren().add(circle1);
        getChildren().add(circle2);
        getChildren().add(circle3);
        getChildren().add(circle4);
        getChildren().add(circle5);
        getChildren().add(circle6);
        getChildren().add(circle7);
        getChildren().add(circle8);
        getChildren().add(circle9);
        getChildren().add(circle10);

        getChildren().add(sensor);


        //Mouse click selection handler
        sensor.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                if ( event.isAltDown() == false && event.isShiftDown() == false) {
                    if (rectangle.getFill() != Color.YELLOW) {
                        rectangle.setFill(Color.YELLOW);
                        Main.controller.changeSelected(1);
                        Main.controller.toggleSelectedMaps(mapLink);
                    } else {
                        rectangle.setFill(Color.WHITE);
                        Main.controller.changeSelected(-1);
                        Main.controller.toggleSelectedMaps(mapLink);
                    }
                }
                else {
                    if (event.isAltDown() == true && event.isShiftDown() == false){
                        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                            try {
                                Desktop.getDesktop().browse(new URI(mapLink));
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }
                        }
                    }else {
                        if (event.isShiftDown() == true && event.isAltDown() == false ){
                            Main.controller.setShiftSelected(mapNumber, title);
                        }
                    }
                }
                event.consume();
            }
        });


    }

    //toggle selection
    public void selectToggle(){
        if (rectangle.getFill() != Color.YELLOW) {
            rectangle.setFill(Color.YELLOW);
            Main.controller.changeSelected(1);
            Main.controller.toggleSelectedMaps(mapLink);
        }else {
            if (rectangle.getFill() != Color.WHITE)
            rectangle.setFill(Color.WHITE);
            Main.controller.changeSelected(-1);
            Main.controller.toggleSelectedMaps(mapLink);
        }
    }

    public void deselect(){
        if (rectangle.getFill() != Color.WHITE) {
            rectangle.setFill(Color.WHITE);
            Main.controller.changeSelected(-1);
            Main.controller.toggleSelectedMaps(mapLink);
        }
    }

    //Select current map
    public void select(){
        if (rectangle.getFill() != Color.YELLOW) {
            rectangle.setFill(Color.YELLOW);
            Main.controller.changeSelected(1);
            Main.controller.toggleSelectedMaps(mapLink);
        }
    }

    //checks if it needs to be selected when switching tabs
    public void checkIfNeedSelection(){
        if ( Main.controller.getSelectedMaps().contains(mapLink) ){
            rectangle.setFill(Color.YELLOW);
        }
    }


}
