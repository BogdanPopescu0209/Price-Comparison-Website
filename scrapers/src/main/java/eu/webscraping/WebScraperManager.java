package eu.webscraping;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * WebScraperManager class from where the
 * scrapers are controlled
 */
public class WebScraperManager {

    ArrayList<WebScraper> webScraperArrayList = getWebScraperArrayList();

    /**
     * Function from where scrapers are set active
     * and can be stopped if entered "stop"
     */
    //Start the threads
    public void startScraping() {

        for (WebScraper scraper : webScraperArrayList) {
                scraper.start();
        }

        //Read input from user until they type 'stop'
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();
        while (!userInput.equals("stop")) {
            userInput = scanner.nextLine();
        }

        //Stop threads
        for (WebScraper scraper : webScraperArrayList) {
            scraper.stopScraping();
        }

        //Wait for threads to finish - join can throw an InterruptedException
        try {
            for (WebScraper scraper : webScraperArrayList) {
                scraper.join();
            }
        } catch (InterruptedException ex) {
            System.out.println("Interrupted exception thrown: " + ex.getMessage());
        }

        if(!webScraperArrayList.isEmpty()){
            webScraperArrayList.get(0).getBikeDAO().getSessionFactory().close();
        }

        System.out.println("Web scraping complete");
    }

    /**
     * Get scrapers array
     * @return webScraperArrayList
     */
    public ArrayList<WebScraper> getWebScraperArrayList() {
        return webScraperArrayList;
    }

    /**
     * Set scrappers array
     * @param webScraperArrayList
     */
    public void setWebScraperArrayList(ArrayList<WebScraper> webScraperArrayList) {
        this.webScraperArrayList = webScraperArrayList;
    }
}
