package classes;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.openqa.selenium.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Map {

    private MapPane mapPane = new MapPane();
    private ArrayList<Circle> circles;

    private static String mapName;
    private static String mapHeader;
    //private static String mapAuthor;
    private static String mapLink;
    private static String mapImageLink;
    private static int mapNumber;
    private ArrayList<Double> mapDifficulties = new ArrayList<>();
    //private ArrayList<String> mapGameTypes;


    private int column;//1 or 2
    private int searchRow;//1 or 2
    @FXML
    Label title;


    public Map(int column, String mapLink, int mapNumber)throws Exception{

        this.mapLink = mapLink;
        this.column = column;
        this.mapNumber = mapNumber;

            if ( WebScraper.numLoaded == 0 ){
                searchRow = 1;
            }else{
                searchRow = 2;
            }

            mapName = WebScraper.driver.findElement(By.xpath("//div[@class='beatmapsets__items']/div[@class='beatmapsets__items-row'][" + searchRow + "]/div[@class='beatmapsets__item'][" + column + "]/div[@class='beatmapset-panel js-audio--player']/div[@class='beatmapset-panel__panel']/a[@class='beatmapset-panel__header']/div[@class='beatmapset-panel__title-artist-box']/div[@class='u-ellipsis-overflow beatmapset-panel__header-text beatmapset-panel__header-text--title']")).getText();

            mapHeader = WebScraper.driver.findElement(By.xpath("//div[@class='beatmapsets__items']/div[@class='beatmapsets__items-row'][" + searchRow + "]/div[@class='beatmapsets__item'][" + column + "]/div[@class='beatmapset-panel js-audio--player']/div[@class='beatmapset-panel__panel']/a[@class='beatmapset-panel__header']/div[@class='beatmapset-panel__title-artist-box']/div[@class='u-ellipsis-overflow beatmapset-panel__header-text']")).getText();

            //mapAuthor = WebScraper.driver.findElement(By.xpath("//div[@class='beatmapsets__items']/div[@class='beatmapsets__items-row'][" + searchRow + "]/div[@class='beatmapsets__item'][" + column + "]/div[@class='beatmapset-panel']/div[@class='beatmapset-panel__panel']/div[@class='beatmapset-panel__content']/div[@class='beatmapset-panel__row']/div[@class='beatmapset-panel__mapper-source-box']/div[@class='u-ellipsis-overflow'][1]/a[@class='beatmapset-panel__link js-usercard']")).getText();
                    //"//div[@class='beatmapsets__items']/div[@class='beatmapsets__items-row'][" + searchRow + "]/div[@class='beatmapsets__item'][" + column + "]/div[@class='beatmapset-panel']/div[@class='beatmapset-panel__panel']/div[@class='beatmapset-panel__content']/div[@class='beatmapset-panel__row']/div[@class='beatmapset-panel__mapper-source-box']/div[@class='u-ellipsis-overflow'][1]/a[@class='js-usercard']")).getText();


            mapImageLink = WebScraper.driver.findElement(By.xpath("//div[@class='beatmapsets__items']/div[@class='beatmapsets__items-row'][" + searchRow + "]/div[@class='beatmapsets__item'][" + column + "]/div[@class='beatmapset-panel js-audio--player']/div[@class='beatmapset-panel__panel']/a[@class='beatmapset-panel__header']/img[@class='beatmapset-panel__image']")).getAttribute("src");


            // FIXME: 1/4/2020 Get map difficulties, undefined difficulties, and gametypes
        // FIXME: 1/6/2020 get map sound

            //GetMapDifficuties

            mapDiffGetter();


        //System.out.print(mapName);

        //System.out.println("mapHeader: " + mapHeader);
        //System.out.println("mapAuthor: " + mapAuthor);
        //System.out.println("mapImageLink: " + mapImageLink);
        //System.out.println("mapLink: " + mapLink);

        //Create and load Map Node

        mapPane.mapNumber = this.mapNumber;
        //System.out.println("Title: " + mapName);
        //System.out.println("Row: " + mapPane.row);
        mapPane.title.setText(mapName);
        mapPane.label.setText(mapHeader);
        //mapPane.label1.setText(mapAuthor);

        //mapImageLink = "https://assets.ppy.sh/beatmaps/1166156/covers/card.jpg?1603457154";
        Image image = new Image(mapImageLink);
        mapPane.imageView.setImage(image);
        mapPane.mapLink = mapLink;
        mapPane.checkIfNeedSelection();

        circles = new ArrayList<>();
        circles.add(mapPane.circle1);
        circles.add(mapPane.circle2);
        circles.add(mapPane.circle3);
        circles.add(mapPane.circle4);
        circles.add(mapPane.circle5);
        circles.add(mapPane.circle6);
        circles.add(mapPane.circle7);
        circles.add(mapPane.circle8);
        circles.add(mapPane.circle9);
        circles.add(mapPane.circle10);


        setCircleColors();

        //add to gridpane
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



        //selectAll? Menu Item
        if ( Main.controller.getSelectAllCheck().isSelected() ){
            mapPane.select();
        }

    }


    public static String getMapName() {
        return mapName;
    }

    public static String getMapHeader() {
        return mapHeader;
    }

    /*public static String getMapAuthor() {
        return mapAuthor;
    }*/

    public static String getMapLink() {
        return mapLink;
    }

    public static String getMapImageLink() {
        return mapImageLink;
    }

    public ArrayList<Double> getMapDifficulties() {
        return mapDifficulties;
    }

    /*public ArrayList<String> getMapGameTypes() {
        return mapGameTypes;
    }*/

    public int getColumn() { return column; }

    public void mapDiffGetter(){

        //WebScraper.driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        List<WebElement> temps = WebScraper.driver.findElements(By.xpath("//div[@class='beatmapsets__items']/div[@class='beatmapsets__items-row'][" + searchRow + "]/div[@class='beatmapsets__item'][" + column + "]/div[@class='beatmapset-panel js-audio--player']/div[@class='beatmapset-panel__panel']/div[@class='beatmapset-panel__content']/div[@class='beatmapset-panel__difficulties']/div"));
        //System.out.println(temps.size());
        for (int i=0; i<temps.size(); i++){
            if ( temps.get(i).getAttribute("class").equals("beatmapset-panel__difficulty-icon") ){
                mapDifficulties.add( Double.parseDouble( temps.get(i).findElement(By.tagName("div")).getAttribute("data-stars") ) );
            }
            else {
                mapDifficulties.add( Double.parseDouble( temps.get(i).getAttribute("data-stars") ) );
            }
            //System.out.println(temps.get(i).getText());
        }
        System.out.println(mapName);
        System.out.println(mapDifficulties);
        System.out.println();

        //https://osu.ppy.sh/help/wiki/Difficulties
        //WebScraper.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);// FIXME: 2/29/2020 should keep?????
    }//mapDiffGetter

    public void setCircleColors(){

        for ( int i=0; i<mapDifficulties.size(); i++ ){
            double diff = mapDifficulties.get(i);

            if ( diff >= 0.0 && diff <= 1.99 ){
                circles.get(i).setStroke(Color.valueOf("81B70D"));
                continue;
            }
            if ( diff >= 2.0 && diff <= 2.69 ){
                circles.get(i).setStroke(Color.valueOf("55D7FF"));
                continue;
            }
            if ( diff >= 2.7 && diff <= 3.99 ){
                circles.get(i).setStroke(Color.valueOf("FFDD22"));
                continue;
            }
            if ( diff >= 4.0 && diff <= 5.29 ){
                circles.get(i).setStroke(Color.valueOf("FF59AD"));
                continue;
            }
            if ( diff >= 5.3 && diff <= 6.49 ){
                circles.get(i).setStroke(Color.valueOf("8358FF"));
                continue;
            }
            if ( diff >= 6.5 ){
                circles.get(i).setStroke(Color.BLACK);
                continue;
            }

        }//for

        //System.out.println("diff size: " + mapDifficulties.size());

        if ( mapDifficulties.size() < 10 ){
            for (int i=mapDifficulties.size(); i < 10; i++){
                circles.get(i).setStroke(Color.TRANSPARENT);
            }
        }

    }//setCircleColors


}//class Map
