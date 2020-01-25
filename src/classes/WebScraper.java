package classes;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class WebScraper extends Thread{

    public static ChromeDriver driver;
    public static JavascriptExecutor js;
    public static int loadLimit = 25;//Max rows of maps.
    public static int numLoaded;
    public static ArrayList<Map> maps = new ArrayList<>();





    public void startChrome(){

        String chromeDriverDir = System.getProperty("user.dir") + "\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", chromeDriverDir);

        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.default_directory", Main.getDownloadPath());//DOWNLOAD DIRECTORY//
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", chromePrefs);
        options.addArguments("--disable-notifications");
        DesiredCapabilities cap = DesiredCapabilities.chrome();
        cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        cap.setCapability(ChromeOptions.CAPABILITY, options);
        System.setProperty("webdriver.chrome.driver", chromeDriverDir);

        //options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200", "--ignore-certificate-errors");//make it headless
        driver = new ChromeDriver(options);




        driver.manage().deleteAllCookies();
        driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        driver.get("https://osu.ppy.sh/beatmapsets");


        js = (JavascriptExecutor) driver;


        //login();
        // FIXME: 12/5/2019 Add login thing


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
                    maps.add(new Map(1, mapLinkC1));
                    maps.add(new Map(2, mapLinkC2));
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










}//class WebScraper
