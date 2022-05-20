package eu.webscraping;

/**
 * WebScraper extends Thread and parent class from where the
 * scrapers extend
 */
public class WebScraper extends Thread{

    private int scrapeDelay;
    private BikeDAO bikeDAO;
    volatile private boolean runThread = true;

    public int getScrapeDelay() {
        return scrapeDelay;
    }

    public void setScrapeDelay(int scrapeDelay) {
        this.scrapeDelay = scrapeDelay;
    }

    public BikeDAO getBikeDAO() {
        return bikeDAO;
    }

    public void setBikeDAO(BikeDAO bikeDAO) {
        this.bikeDAO = bikeDAO;
    }

    public boolean setRunThread(){
        return runThread;
    }

    public void stopScraping(){
        this.runThread = false;
    }
}
