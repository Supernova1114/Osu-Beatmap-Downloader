package classes;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import org.openqa.selenium.By;

import java.util.ArrayList;

public class Map {

    private static String mapName;
    private static String mapHeader;
    private static String mapAuthor;
    private static String mapLink;
    private static String mapImageLink;
    private static ArrayList<Double> mapDifficulties;
    private static ArrayList<String> mapGameTypes;
    private int column;//1 or 2
    private int searchRow;//1 or 2
    @FXML
    Label title;


    public Map(int column, String mapLink)throws Exception{

        this.mapLink = mapLink;
        this.column = column;

            if ( WebScraper.numLoaded == 0 ){
                searchRow = 1;
            }else{
                searchRow = 2;
            }

            mapName = WebScraper.driver.findElement(By.xpath("//div[@class='beatmapsets__items']/div[@class='beatmapsets__items-row'][" + searchRow + "]/div[@class='beatmapsets__item'][" + column + "]/div[@class='beatmapset-panel']/div[@class='beatmapset-panel__panel']/a[@class='beatmapset-panel__header']/div[@class='beatmapset-panel__title-artist-box']/div[@class='u-ellipsis-overflow beatmapset-panel__header-text beatmapset-panel__header-text--title']")).getText();

            mapHeader = WebScraper.driver.findElement(By.xpath("//div[@class='beatmapsets__items']/div[@class='beatmapsets__items-row'][" + searchRow + "]/div[@class='beatmapsets__item'][" + column + "]/div[@class='beatmapset-panel']/div[@class='beatmapset-panel__panel']/a[@class='beatmapset-panel__header']/div[@class='beatmapset-panel__title-artist-box']/div[@class='beatmapset-panel__header-text']")).getText();

            mapAuthor = WebScraper.driver.findElement(By.xpath("//div[@class='beatmapsets__items']/div[@class='beatmapsets__items-row'][" + searchRow + "]/div[@class='beatmapsets__item'][" + column + "]/div[@class='beatmapset-panel']/div[@class='beatmapset-panel__panel']/div[@class='beatmapset-panel__content']/div[@class='beatmapset-panel__row']/div[@class='beatmapset-panel__mapper-source-box']/div[@class='u-ellipsis-overflow'][1]/a[@class='beatmapset-panel__link js-usercard']")).getText();

            mapImageLink = WebScraper.driver.findElement(By.xpath("//div[@class='beatmapsets__items']/div[@class='beatmapsets__items-row'][" + searchRow + "]/div[@class='beatmapsets__item'][" + column + "]/div[@class='beatmapset-panel']/div[@class='beatmapset-panel__panel']/a[@class='beatmapset-panel__header']/img")).getAttribute("src");

            // FIXME: 1/4/2020 Get map difficulties, undefined difficulties, and gametypes
        // FIXME: 1/6/2020 get map sound



        System.out.print(mapName);
        //System.out.println("mapHeader: " + mapHeader);
        //System.out.println("mapAuthor: " + mapAuthor);
        //System.out.println("mapImageLink: " + mapImageLink);
        //System.out.println("mapLink: " + mapLink);


        //Create and load Map Node

        MapPane mapPane = new MapPane();
        mapPane.title.setText(mapName);
        mapPane.label.setText(mapHeader);
        mapPane.label1.setText(mapAuthor);
        Image image = new Image(mapImageLink);
        mapPane.imageView.setImage(image);

        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                try {
                    Main.controller.addChildren(column-1,mapPane);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
// ...
        });


        /*Task<Void> task = new Task<Void>() {

            @Override protected Void call() throws Exception {

                    (Node)FXMLLoader.load(getClass().getResource("MapPane.fxml"))

                *//*for (int i=0; i<1; i++) {
                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            try {

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }*//*
                return null;
            }
        };*/




    }


    public static String getMapName() {
        return mapName;
    }

    public static String getMapHeader() {
        return mapHeader;
    }

    public static String getMapAuthor() {
        return mapAuthor;
    }

    public static String getMapLink() {
        return mapLink;
    }

    public static String getMapImageLink() {
        return mapImageLink;
    }

    public static ArrayList<Double> getMapDifficulties() {
        return mapDifficulties;
    }

    public static ArrayList<String> getMapGameTypes() {
        return mapGameTypes;
    }

    public int getColumn() { return column; }



}//class Map
