package classes;

import classes.controller.SettingsController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class WebScraper extends Thread{

    public static ChromeDriver driver;
    public static JavascriptExecutor js;
    public static int loadLimit = 100;//Max rows of maps.
    public static int numLoaded;
    public static ArrayList<Map> maps = new ArrayList<>();

    private int mapNumber;

    private static boolean isLoggedIn = false;

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

        //options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200", "--ignore-certificate-errors");//make it headless
        driver = new ChromeDriver(options);


        Main.getMainStage().setOnCloseRequest(event -> {
            Main.getController().exit();
        });


        driver.manage().deleteAllCookies();
        driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);



        driver.navigate().to("https://www.google.com");

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
        }

        mapNumber = 0;

        js = (JavascriptExecutor) driver;


        login();


        Main.controller.setProgressBarVisibility(false);

        return 0;
    }//startChrome()


    public void run(){

        maps.clear();

        js.executeScript("window.scrollTo(0, 0);");

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



        String tempC1 = "";
        String tempC2 = "";


        for ( int i = 0; numLoaded<loadLimit; i++ ){

            if ( numLoaded == 0 ){
                mapLinkC1 = driver.findElement(By.xpath("//div[@class='beatmapsets__items']/div[@class='beatmapsets__items-row'][1]/div[@class='beatmapsets__item'][1]/div[@class='beatmapset-panel']/div[@class='beatmapset-panel__panel']/a")).getAttribute("href");
                mapLinkC2 = driver.findElement(By.xpath("//div[@class='beatmapsets__items']/div[@class='beatmapsets__items-row'][1]/div[@class='beatmapsets__item'][2]/div[@class='beatmapset-panel']/div[@class='beatmapset-panel__panel']/a")).getAttribute("href");

                System.out.println(mapLinkC1 + " " + mapLinkC2);
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

                /*for ( int j = 0; j<maps.size(); j++ ){

                    for ( int r = j+1; r<maps.size(); r++ ){

                        if ( maps.get(j).equals(maps.get(r)) ){
                            System.out.println("ERROR");
                            App.close();
                        }

                    }

                }*/

            }

            js.executeScript("window.scrollBy(0,100)");//keep at 15!100


        }//forloop

        /*String testThis = "";
        String againstThis = "Umareta Imi Nado Nakatta.HYDRA (TV Size)Ievan Polkka x Fubuki (Hamburgaga Remix)Classic PursuitLess Than Three (Ricardo Autobahn Remix)Akumu no Hate ni Miru YumeMelody of Heaven (Original Mix)Koi, Hitokuchi.BRING ME TO LIFE VOCAL COVER 2016 (OFFICIAL)Yomi yori Kikoyu, Koukoku no Tou to Honoo no Shoujo.Lights of MuseHappy Welus (Original Mix)Snow halation (NICO Mix)aureoleWe Won't Be Alone (feat. Laura Brehm)Netsujou no Spectrum (TV Size)Sacred Bones RiotSouhaku RebellionSanctus AbsurdusHisekai Harmonize feat. Kagamine RinTwin RocketStrike The Blood (TV Size)Especial de Natal da LunaMedicine (Sound Remedy Remix)DORNWALD ~Der Junge im Kaefig~KakehikiENDLESS QUEERWaifu Baby (Christm/a/s 2015)Sweet Cat DreamingSnow halation (REDSHiFT Remix)Satisfiction (uncut edition)A Whole New WorldHoshizora no Imawinter*giftTiny Christmas PartySeiza ga Koi Shita Shunkan o.carol of the circlesVanished truthMissing YouCherry BombKnowledge ParanoiaGardens Under A Spring SkyKishin Douji ZENKI (TV Size)Slow Motion (feat. DongGeyoung)Howling (TV Size)M:routineCaboSKY JOURNEYBABY STEP* Erm, could it be a Spatiotemporal ShockWAVE Syndrome...?REcorrectionRISE (TV Size)Otomodachi Film (TV Size)ConnexionFloating upHoly NightLAST STARDUSTKimagure StardomLOVING TRIPMirai PuzzleDistant StructuresKono Hoshi de....Setsunai Sandglassletter songSOLOTachiagare! (TV Size)-ERROR 2018ver.Rolling GirlQian Si XiPOTOMIRU PINERAMelon SodaOn My OwnTaroti:\\DRIVEFriend ShitaiRun The TrackKlangPrecious Wing (Short Ver.)SLOW DANCING IN THE DARKEtherRumbling MemoryVoices in My HeadCan We Fix It? (TV Size)Renai SyndromeHibiscusThe Sealer ~A Milia to Milia no Min~Chromatic LightsHighscoreGood Day feat. DJ Snake & Elliphantfastest crash (Camellia's ''paroxysmal'' Energetic Hitech Remix)Magical HolidayKiller QueenOceanusthe littlest things [Kesu+ Audio Scribble]our songRomeo to CinderellaTenohira de odoruTempeSTTokyo's StarlightRoad of Resistance";
        for ( int g=0; g<maps.size(); g++ ){
            testThis += maps.get(g).getMapName();
        }
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(testThis);
        System.out.println(againstThis);

        if ( testThis.equals(againstThis) ){
            System.out.println();
            System.out.println("Success!!!!!!!!!!!!!!!!");
            System.out.println("Success!!!!!!!!!!!!!!!!");
            System.out.println("Success!!!!!!!!!!!!!!!!");
        }
        else
        {
            System.out.println("Not same!");
            App.close();
        }*/

    }//run()


    public void login(){
        if ( !(Main.getSettingsController().getUsername() == null) && !(Main.getSettingsController().getPassword() == null) ) {
            driver.findElement(By.xpath("//button[@class='avatar avatar--nav2 js-current-user-avatar js-click-menu js-user-login--menu js-user-header avatar--guest']")).click();

            driver.findElement(By.xpath(userXpath)).sendKeys(Main.settingsController.getUsername());
            driver.findElement(By.xpath(passXpath)).sendKeys(Main.settingsController.getPassword());

            driver.findElement(By.xpath("//button[@class='btn-osu-big btn-osu-big--nav-popup']")).click();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);//makes error timeout 0
            try{
            String loginError = driver.findElement(By.xpath("//div[@class='login-box__row login-box__row--error js-login-form--error']")).getText();

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
                isLoggedIn = true;
                //e.printStackTrace();
            }



            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

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
}//class WebScraper
