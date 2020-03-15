package classes;

import classes.controller.SettingsController;
import com.google.common.collect.Maps;
import com.sun.xml.internal.ws.addressing.WsaActionUtil;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class WebScraper extends Thread{

    public static ChromeDriver driver;
    public static JavascriptExecutor js;
    public static int loadLimit = 50;//Max rows of maps.
    public static int numLoaded;
    public static int totalNumLoaded;
    public static ArrayList<Map> maps = new ArrayList<>();

    private int mapNumber;
    
    private boolean isFirstRun = true;

    private static boolean isLoggedIn = false;

    private String chromeDriverVersion;
    private String browserVersion;


    private String tempC1 = "";
    private String tempC2 = "";


    private String userXpath = "//input[@class='login-box__form-input js-login-form-input js-nav2--autofocus']";
    private String passXpath = "//input[@class='login-box__form-input js-login-form-input']";




    public int startChrome(){

        String chromeDriverDir = System.getProperty("user.dir") + "\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", chromeDriverDir);

        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.default_directory", SettingsController.getDownloadDir());//DOWNLOAD DIRECTORY//
        System.out.println(SettingsController.getDownloadDir());
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", chromePrefs);
        options.addArguments("--disable-notifications");
        DesiredCapabilities cap = DesiredCapabilities.chrome();
        cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        cap.setCapability(ChromeOptions.CAPABILITY, options);

        System.setProperty("webdriver.chrome.driver", chromeDriverDir);

        options.addArguments( "--headless","--disable-gpu", "--window-size=1920,1200", "--ignore-certificate-errors");//make it headless

        driver = new ChromeDriver(options);

//        System.out.println("Version: "+driver.getCapabilities().getVersion());
//        System.out.println("Version?: " + driver.getCapabilities().getCapability("chrome.userDataDir"));

        /*Main.getMainStage().setOnCloseRequest(event -> {
            Main.getController().exit();
        });*/


        driver.manage().deleteAllCookies();
        driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);


        /*String a = driver.getCapabilities().getCapability("chrome").toString();
        a = a.substring( a.indexOf("=") + 1, a.indexOf("(") - 1 );
        chromeDriverVersion = a;

        browserVersion = driver.getCapabilities().getVersion();

        System.out.println( "chromeDriverVersion: " + chromeDriverVersion);// FIXME: 2/17/2020 Add ChromeDriver update ability
        System.out.println("browserVersion: " + browserVersion);*/

        /*if ( !chromeDriverVersion.equals(browserVersion) )
            updateChromeDriver();
*/

        /*driver.navigate().to("https://www.google.com");

        String error = driver.findElement(By.xpath("//html")).getAttribute("class");

        if( error.equals("offline") ) {

            System.out.println("No Internet Connection");
            Main.controller.setProgressBarVisibility(false);

            //JOptionPane.showMessageDialog(null, "Error: Network Connection Not Found!");
            JOptionPane optionPane = new JOptionPane();
            optionPane.setMessage("Network Connection Not Found!");
            JDialog dialog = optionPane.createDialog("Error");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);

            return 1;
        } else {
            System.out.println("Internet Connected");
            driver.get("https://osu.ppy.sh/beatmapsets?m=0&s=ranked");// FIXME: 1/23/2020 Be able to switch to diff filters and stuff by using the link thing
        }*/

        driver.get("https://osu.ppy.sh/beatmapsets?m=0&s=ranked");

        mapNumber = 0;

        js = (JavascriptExecutor) driver;

        login();


        Main.controller.setProgressBarVisibility(false);

        return 0;
    }//startChrome()


    public void run(){

        /*String element = driver.findElement(By.xpath("//body")).getText();

        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.add(pane);
        frame.pack();*/

        //System.out.println("temp1: " + tempC1);
        //System.out.println("temp2: " + tempC2);

        if (isFirstRun) {
            maps.clear();

            js.executeScript("window.scrollTo(0, 0);");

            tempC1 = "";
            tempC2 = "";

        }else {
            numLoaded = 1;
            //System.out.println("temp1: " + tempC1);
            //System.out.println("temp2: " + tempC2);
        }

        //System.out.println("IS First Run?: " + isFirstRun);
        //System.out.println("numloaded: " + numLoaded);

        //vars
        String mapNameC1;//as in column 1
        String mapNameC2;//as in column 2

        String mapHeaderC1;
        String mapHeaderC2;

        String mapAuthorC1;
        String mapAuthorC2;

        String mapLinkC1 = "";
        String mapLinkC2 = "";

        String mapImageLinkC1;
        String mapImageLinkC2;

        ArrayList<Double> mapDifficultiesC1;
        ArrayList<Double> mapDifficultiesC2;

        ArrayList<String> mapGameTypesC1;
        ArrayList<String> mapGameTypesC2;



        int limit;

        if ( isFirstRun == false ){
            limit = loadLimit + 1;
        }else {
            limit = loadLimit;
        }


        for ( int i = 0; numLoaded<limit; i++ ){

                if (numLoaded == 0) {
                    mapLinkC1 = driver.findElement(By.xpath("//div[@class='beatmapsets__items']/div[@class='beatmapsets__items-row'][1]/div[@class='beatmapsets__item'][1]/div[@class='beatmapset-panel']/div[@class='beatmapset-panel__panel']/a")).getAttribute("href");
                    mapLinkC2 = driver.findElement(By.xpath("//div[@class='beatmapsets__items']/div[@class='beatmapsets__items-row'][1]/div[@class='beatmapsets__item'][2]/div[@class='beatmapset-panel']/div[@class='beatmapset-panel__panel']/a")).getAttribute("href");

                    //System.out.println(mapLinkC1 + " " + mapLinkC2);
                }


            if ( numLoaded != 0 ){
                mapLinkC1 = driver.findElement(By.xpath("//div[@class='beatmapsets__items']/div[@class='beatmapsets__items-row'][2]/div[@class='beatmapsets__item'][1]/div[@class='beatmapset-panel']/div[@class='beatmapset-panel__panel']/a")).getAttribute("href");
                mapLinkC2 = driver.findElement(By.xpath("//div[@class='beatmapsets__items']/div[@class='beatmapsets__items-row'][2]/div[@class='beatmapsets__item'][2]/div[@class='beatmapset-panel']/div[@class='beatmapset-panel__panel']/a")).getAttribute("href");

                //System.out.println(mapLinkC1 + " " + mapLinkC2);
            }



            if ( !(tempC1.equals(mapLinkC1)) && !(tempC2.equals(mapLinkC2)) ) {

                tempC1 = mapLinkC1;
                tempC2 = mapLinkC2;

                try {

                    maps.add(new Map(1, mapLinkC1, mapNumber));
                    mapNumber++;
                    maps.add(new Map(2, mapLinkC2, mapNumber));
                    mapNumber++;
                }catch (Exception e){e.printStackTrace();}

                numLoaded++;
                totalNumLoaded++;

                /*for ( int j = 0; j<maps.size(); j++ ){

                    for ( int r = j+1; r<maps.size(); r++ ){

                        if ( maps.get(j).equals(maps.get(r)) ){
                            System.out.println("ERROR");
                            App.close();
                        }

                    }

                }*/

            }

            js.executeScript("window.scrollBy(0,100)");


        }

    isFirstRun = false;

    }//run()


    public void login(){
        if ( !(Main.getSettingsController().getUsername() == null) && !(Main.getSettingsController().getPassword() == null) ) {
            driver.findElement(By.xpath("//button[@class='avatar avatar--nav2 js-current-user-avatar js-click-menu js-user-login--menu js-user-header avatar--guest']")).click();

            driver.findElement(By.xpath(userXpath)).sendKeys(Main.settingsController.getUsername());
            driver.findElement(By.xpath(passXpath)).sendKeys(Main.settingsController.getPassword());

            driver.findElement(By.xpath("//button[@class='btn-osu-big btn-osu-big--nav-popup']")).click();
            System.out.println("daasasassadasdassa 1");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("sadssadsaassd 2");

            //driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);//makes error timeout 0
            try{
                System.out.println("dadadasasdasdsa try error");
            String loginError = driver.findElement(By.xpath("//div[@class='login-box__row login-box__row--error js-login-form--error']")).getText();
                System.out.println("adaadaasasad erorr");
                isLoggedIn = false;

                Platform.runLater(new Runnable(){
                    @Override
                    public void run() {
                        Alert alert = new Alert(Alert.AlertType.NONE);
                        alert.getButtonTypes().addAll(ButtonType.OK);
                        alert.setHeaderText("Incorrect Login Info!");
                        //alert.setContentText("Login?");

                        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                        stage.setAlwaysOnTop(true);

                        alert.setX(Main.getMainStage().getX()+125);
                        alert.setY(Main.getMainStage().getY()+146);

                        alert.showAndWait();

                    }
                });
            }catch (Exception e){
                System.out.println("not error");
                boolean found = false;
                while (found == false){
                    try{
                        driver.findElements(By.className("beatmapset-panel__icon js-beatmapset-download-link"));
                        System.out.println("Login Found!");
                        found = true;
                    }catch (Exception r){
                        System.out.println("Searching For Login Success...");
                    }
                }
                System.out.println("done");
                isLoggedIn = true;
                //e.printStackTrace();
            }



            //driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

           /* try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {// FIXME: 2/12/2020 Dont really want to rely on timing
                e.printStackTrace();
            }*/

        }
        else {

            /*Platform.runLater(new Runnable(){
                @Override
                public void run() {
                    Alert alert = new Alert(Alert.AlertType.NONE);
                    alert.getButtonTypes().addAll(ButtonType.OK);
                    alert.setHeaderText("You are not logged in!");
                    //alert.setContentText("Login?");

                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.setAlwaysOnTop(true);

                }
            });*/


        }


    }


    public static boolean getIsLoggedIn() {
        return isLoggedIn;
    }

    public void updateChromeDriver(){

        /*try {
            Document doc = Jsoup.connect("http://google.com")
                    .data("query", "Java")
                    .userAgent("Mozilla")
                    .cookie("auth", "token")
                    .timeout(3000)
                    .post();

            *//*Elements versions = document.select("tr");
            System.out.println(versions.isEmpty());*//*
            System.out.println("Doc: ");

        } catch (IOException e) {
            e.printStackTrace();
        }*/



    }

}//class WebScraper
