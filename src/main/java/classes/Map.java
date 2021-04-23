package classes;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import jfxtras.styles.jmetro.JMetro;
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
    private ArrayList<String> mapDifficulties = new ArrayList<>();
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

            //Quick copy
            //" + searchRow + "
            //" + column + "

            mapName = WebScraper.driver.findElement(By.xpath("//div[@class='beatmapsets__items']/div[@class='beatmapsets__items-row'][" + searchRow + "]/div[@class='beatmapsets__item'][" + column + "]/div[@class='beatmapset-panel js-audio--player']/div[@class='beatmapset-panel__content']/div[@class='beatmapset-panel__info']/div[@class='beatmapset-panel__info-row beatmapset-panel__info-row--title']/a[@class='beatmapset-panel__main-link u-ellipsis-overflow']")).getText();
        System.out.println(mapName);

            mapHeader = WebScraper.driver.findElement(By.xpath("//div[@class='beatmapsets__items']/div[@class='beatmapsets__items-row'][" + searchRow + "]/div[@class='beatmapsets__item'][" + column + "]/div[@class='beatmapset-panel js-audio--player']/div[@class='beatmapset-panel__content']/div[@class='beatmapset-panel__info']/div[@class='beatmapset-panel__info-row beatmapset-panel__info-row--artist']/a[@class='beatmapset-panel__main-link u-ellipsis-overflow']")).getText();

            //mapAuthor = WebScraper.driver.findElement(By.xpath("//div[@class='beatmapsets__items']/div[@class='beatmapsets__items-row'][" + searchRow + "]/div[@class='beatmapsets__item'][" + column + "]/div[@class='beatmapset-panel']/div[@class='beatmapset-panel__panel']/div[@class='beatmapset-panel__content']/div[@class='beatmapset-panel__row']/div[@class='beatmapset-panel__mapper-source-box']/div[@class='u-ellipsis-overflow'][1]/a[@class='beatmapset-panel__link js-usercard']")).getText();
                    //"//div[@class='beatmapsets__items']/div[@class='beatmapsets__items-row'][" + searchRow + "]/div[@class='beatmapsets__item'][" + column + "]/div[@class='beatmapset-panel']/div[@class='beatmapset-panel__panel']/div[@class='beatmapset-panel__content']/div[@class='beatmapset-panel__row']/div[@class='beatmapset-panel__mapper-source-box']/div[@class='u-ellipsis-overflow'][1]/a[@class='js-usercard']")).getText();


            mapImageLink = WebScraper.driver.findElement(By.xpath("//div[@class='beatmapsets__items']/div[@class='beatmapsets__items-row'][" + searchRow + "]/div[@class='beatmapsets__item'][" + column + "]/div[@class='beatmapset-panel js-audio--player']/a[@class='beatmapset-panel__cover-container']/div[@class='beatmapset-panel__cover-col beatmapset-panel__cover-col--info']/img[@class='beatmapset-panel__cover']")).getAttribute("src");


        // FIXME: 1/6/2020 get map sound

            //Get map difficulties
            mapDiffGetter();


        //Create and load Map Node

        mapPane.mapNumber = this.mapNumber;
        //System.out.println("Title: " + mapName);
        //System.out.println("Row: " + mapPane.row);
        mapPane.title.setText(mapName);
        //mapPane.title.setTextFill(Color.WHITE);


        mapPane.label.setText(mapHeader);
        //mapPane.label.setTextFill(Color.WHITE);
        //mapPane.label1.setText(mapAuthor);

        //mapImageLink = "https://assets.ppy.sh/beatmaps/1166156/covers/card.jpg?1603457154";
        Image image = new   Image(mapImageLink);
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

        //add to grid pane
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

    public ArrayList<String> getMapDifficulties() {
        return mapDifficulties;
    }

    /*public ArrayList<String> getMapGameTypes() {
        return mapGameTypes;
    }*/

    public int getColumn() { return column; }

    public void mapDiffGetter(){

        //WebScraper.driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        try {
            List<WebElement> temps = WebScraper.driver.findElements(By.xpath("//div[@class='beatmapsets__items']/div[@class='beatmapsets__items-row'][" + searchRow + "]/div[@class='beatmapsets__item'][" + column + "]/div[@class='beatmapset-panel js-audio--player']/div[@class='beatmapset-panel__content']/div[@class='beatmapset-panel__info']/a/div/div[@class='beatmapset-panel__beatmap-dot']"));
            //System.out.println("Important size: " + temps.size());

            //System.out.println(temps.size());
            /*for (int i = 0; i < temps.size(); i++) {
                if (temps.get(i).getAttribute("class").equals("beatmapset-panel__difficulty-icon")) {
                    mapDifficulties.add(Double.parseDouble(temps.get(i).findElement(By.tagName("div")).getAttribute("data-stars")));
                } else {
                    mapDifficulties.add(Double.parseDouble(temps.get(i).getAttribute("data-stars")));
                }
                //System.out.println(temps.get(i).getText());
            }
            System.out.println(mapName);
            System.out.println(mapDifficulties);
            System.out.println();*/

            for (int i=0; i<temps.size(); i++){
                String diff = temps.get(i).getAttribute("style");
                //diff = diff.substring(16, diff.indexOf(")")-1);
                mapDifficulties.add(diff);
            }

            //System.out.println(mapDifficulties);

            //--bg:var(--diff-hard); << Style Att


        }catch (Exception e){
            e.printStackTrace();
        }
        //https://osu.ppy.sh/help/wiki/Difficulties
        //WebScraper.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);// FIXME: 2/29/2020 should keep?????
    }//mapDiffGetter

    //Sets the colors of the difficulty circles
    public void setCircleColors(){

        //max 10 circles rn

        for (int i=0; i<10 && i<mapDifficulties.size(); i++){
            switch (mapDifficulties.get(i)){
                case "--bg:var(--diff-easy);":
                    circles.get(i).setStroke(Color.valueOf("81B70D"));
                    break;
                case "--bg:var(--diff-normal);":
                    circles.get(i).setStroke(Color.valueOf("55D7FF"));
                    break;
                case "--bg:var(--diff-hard);":
                    circles.get(i).setStroke(Color.valueOf("FFDD22"));
                    break;
                case "--bg:var(--diff-insane);":
                    circles.get(i).setStroke(Color.valueOf("FF59AD"));
                    break;
                case "--bg:var(--diff-expert);":
                    circles.get(i).setStroke(Color.valueOf("8358FF"));
                    break;
                case "--bg:var(--diff-expert-plus);":
                    circles.get(i).setStroke(Color.BLACK);
                    break;
            }
        }


        /*for ( int i=0; i<mapDifficulties.size(); i++ ){
            double diff = mapDifficulties.get(i);

            if ( diff >= 0.0 && diff <= 1.99 ){//easy
                circles.get(i).setStroke(Color.valueOf("81B70D"));
                continue;
            }
            if ( diff >= 2.0 && diff <= 2.69 ){//normal
                circles.get(i).setStroke(Color.valueOf("55D7FF"));
                continue;
            }
            if ( diff >= 2.7 && diff <= 3.99 ){//hard
                circles.get(i).setStroke(Color.valueOf("FFDD22"));
                continue;
            }
            if ( diff >= 4.0 && diff <= 5.29 ){//insane
                circles.get(i).setStroke(Color.valueOf("FF59AD"));
                continue;
            }
            if ( diff >= 5.3 && diff <= 6.49 ){//expert
                circles.get(i).setStroke(Color.valueOf("8358FF"));
                continue;
            }
            if ( diff >= 6.5 ){//expert plus
                circles.get(i).setStroke(Color.BLACK);
                continue;
            }

        }//for*/

        //System.out.println("diff size: " + mapDifficulties.size());

        if ( mapDifficulties.size() < 10 ){
            for (int i=mapDifficulties.size(); i < 10; i++){
                circles.get(i).setStroke(Color.TRANSPARENT);
            }
        }

    }//setCircleColors


}//class Map
