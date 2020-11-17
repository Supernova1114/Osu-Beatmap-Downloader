package classes;

import classes.controller.SettingsController;
//import com.google.common.collect.Maps;
import com.sun.xml.internal.ws.addressing.WsaActionUtil;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import org.jsoup.Connection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
    public static ArrayList<String> tabs;
    public static int loadLimit = 50;//Max rows of maps.
    public static int numLoaded;
    public static int totalNumLoaded;
    public static ArrayList<Map> maps = new ArrayList<>();

    private static int mapNumber;
    
    private static boolean isFirstRun = true;

    private static boolean isLoggedIn = false;

    private static volatile boolean switched = false;
    private static volatile boolean scraperStop = false;
    private static volatile boolean scraperDone = false;

    private String chromeDriverVersion;
    private String browserVersion;


    private String tempC1 = "";
    private String tempC2 = "";


    private String userXpath = "//input[@class='login-box__form-input js-login-form-input js-nav2--autofocus']";
    private String passXpath = "//input[@class='login-box__form-input js-login-form-input']";



    //Start Chrome, open tabs and connect to osu website
    public int startChrome(){

        String chromeDriverDir = System.getProperty("user.dir") + "\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", chromeDriverDir);

        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.default_directory", SettingsController.getDownloadDir());//DOWNLOAD DIRECTORY//
        chromePrefs.put("profile.content_settings.exceptions.automatic_downloads.*.setting", 1 );
        System.out.println(SettingsController.getDownloadDir());
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);//SET CHROME TO HEADLESS MODE
        options.setExperimentalOption("prefs", chromePrefs);
        options.addArguments("--disable-notifications");

        /*DesiredCapabilities cap = DesiredCapabilities.chrome();
        cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        cap.setCapability(ChromeOptions.CAPABILITY, options);*/

        options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        options.setCapability(ChromeOptions.CAPABILITY, options);

        //System.setProperty("webdriver.chrome.driver", chromeDriverDir);

        //Headless makes chrome invisible
        options.addArguments("--disable-gpu", "--window-size=1920,1200", "--ignore-certificate-errors");//make it headless "--headless",


        driver = new ChromeDriver(options);


        driver.manage().deleteAllCookies();
        driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);


        //Opens tabs for Osu, Taiko, Catch, and Mania

        ((JavascriptExecutor)driver).executeScript("window.open()");
        ((JavascriptExecutor)driver).executeScript("window.open()");
        ((JavascriptExecutor)driver).executeScript("window.open()");
        //((JavascriptExecutor)driver).executeScript("window.open()");

        tabs = new ArrayList<String>(driver.getWindowHandles());

        //driver.switchTo().window(tabs.get(4));
        //driver.get("https://beatconnect.io/");//osu download page

        driver.switchTo().window(tabs.get(0));

        driver.get("https://osu.ppy.sh/beatmapsets?m=0&s=ranked");//osu ranked


        mapNumber = 0;

        js = (JavascriptExecutor) driver;

        login();


        Main.controller.setProgressBarVisibility(false);

        Main.controller.getOsuRadio().setDisable(false);
        Main.controller.getTaikoRadio().setDisable(false);
        Main.controller.getCatchRadio().setDisable(false);
        Main.controller.getManiaRadio().setDisable(false);

        return 0;
    }//startChrome()


    //Run web scraper
    public void run(){
        System.out.println("Run Started");

        scraperDone = false;


        if (isFirstRun) {
            maps.clear();

            js.executeScript("window.scrollTo(0, 0);");

            tempC1 = "";
            tempC2 = "";

        }else {
            numLoaded = 1;
        }

        //vars

        String mapLinkC1 = "";
        String mapLinkC2 = "";


        int limit;

        if ( isFirstRun == false ){
            limit = loadLimit + 1;
        }else {
            limit = loadLimit;
        }


        while( numLoaded<limit && scraperStop == false ){

                if (numLoaded == 0) {
                    try {
                        mapLinkC1 = driver.findElement(By.xpath("//div[@class='beatmapsets__items']/div[@class='beatmapsets__items-row'][1]/div[@class='beatmapsets__item'][1]/div[@class='beatmapset-panel js-audio--player']/div[@class='beatmapset-panel__panel']/a")).getAttribute("href");
                        mapLinkC2 = driver.findElement(By.xpath("//div[@class='beatmapsets__items']/div[@class='beatmapsets__items-row'][1]/div[@class='beatmapsets__item'][2]/div[@class='beatmapset-panel js-audio--player']/div[@class='beatmapset-panel__panel']/a")).getAttribute("href");
                    }catch (Exception e){e.printStackTrace();}
                    //System.out.println(mapLinkC1 + " " + mapLinkC2);
                }


            if ( numLoaded != 0 ){
                try {
                    mapLinkC1 = driver.findElement(By.xpath("//div[@class='beatmapsets__items']/div[@class='beatmapsets__items-row'][2]/div[@class='beatmapsets__item'][1]/div[@class='beatmapset-panel js-audio--player']/div[@class='beatmapset-panel__panel']/a")).getAttribute("href");
                    mapLinkC2 = driver.findElement(By.xpath("//div[@class='beatmapsets__items']/div[@class='beatmapsets__items-row'][2]/div[@class='beatmapsets__item'][2]/div[@class='beatmapset-panel js-audio--player']/div[@class='beatmapset-panel__panel']/a")).getAttribute("href");
                }catch (Exception e){e.printStackTrace();}
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
                Main.controller.setNumLoadedLabel(totalNumLoaded);
                Main.controller.setSelectMenuNum(totalNumLoaded);

            }

            js.executeScript("window.scrollBy(0,100)");


        }

    isFirstRun = false;

        scraperDone = true;
        System.out.println("Finished Running!");
    }//run()


    //Login to osu website in order to have access to mode links
    public void login(){
        if ( !(Main.getSettingsController().getUsername() == null) && !(Main.getSettingsController().getPassword() == null) ) {
            driver.findElement(By.xpath("//button[@class='avatar avatar--nav2 js-current-user-avatar js-click-menu js-user-login--menu js-user-header avatar--guest']")).click();

            driver.findElement(By.xpath(userXpath)).sendKeys(Main.settingsController.getUsername());
            driver.findElement(By.xpath(passXpath)).sendKeys(Main.settingsController.getPassword());

            driver.findElement(By.xpath("//button[@class='btn-osu-big btn-osu-big--nav-popup js-captcha--submit-button']")).click();
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

            if (!loginError.equals("") && !loginError.equals(null)) {
                System.out.println("adaadaasasad erorr");
                isLoggedIn = false;

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Alert alert = new Alert(Alert.AlertType.NONE);
                        alert.getButtonTypes().addAll(ButtonType.OK);
                        alert.setHeaderText("Incorrect Login Info!");
                        //alert.setContentText("Login?");

                        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                        stage.setAlwaysOnTop(true);

                        alert.setX(Main.getMainStage().getX() + 125);
                        alert.setY(Main.getMainStage().getY() + 146);

                        alert.showAndWait();

                    }
                });
            }
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

        }


    }


    public static boolean getIsLoggedIn() {
        return isLoggedIn;
    }

    public static void switchTabs(int num){
        driver.switchTo().window(tabs.get(num));
    }

    public static void setIsFirstRun(boolean a) {
        isFirstRun = a;
    }

    public static void setScraperStop(boolean a){
        scraperStop = a;
    }

    public static boolean getScraperDone(){
        return scraperDone;
    }

    public static void resetMapNumber(){
        mapNumber = 0;
    }

    public static boolean isSwitched(){
        return switched;
    }

    public static void setSwitched(boolean s){
        switched = s;
    }

    /*public void updateChromeDriver(){

        browserVersion = driver.getCapabilities().getVersion();
        System.out.println("browserVersion = " + browserVersion);

        //System.out.println(driver.getCapabilities().getCapabilityNames());
        java.util.Map hash = (java.util.Map)driver.getCapabilities().asMap().get("chrome");
        String tempCV = (String)hash.get("chromedriverVersion");
        chromeDriverVersion = tempCV.substring(0, tempCV.indexOf(" "));
        System.out.println("driverVersion = " + chromeDriverVersion);
        System.out.println();

        // load page using HTML Unit and fire scripts
        *//*WebClient webClient = new WebClient();
        HtmlPage myPage = webClient.getPage(new File("page.html").toURI().toURL());

        // convert page to generated HTML and convert to document
        doc = Jsoup.parse(myPage.asXml());

        // iterate row and col
        for (Element row : doc.select("table#data > tbody > tr"))

            for (Element col : row.select("td"))

                // print results
                System.out.println(col.ownText());

        // clean up resources
        webClient.close();*//*




    }*/

}//class WebScraper
