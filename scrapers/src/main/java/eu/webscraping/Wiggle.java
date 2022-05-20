package eu.webscraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Wiggle class from where bike are loaded to database
 */
public class Wiggle extends WebScraper {

    /**
     * Function to run thread
     */
    @Override
    public void run() {

        //While loop will keep running until runThread is set to false
        while (this.setRunThread()) {

            try {

                //Array that contains all urls to be scraped
                String[] query = {"https://www.wiggle.co.uk/cycle/mountain-bikes?ps=96#breadcrumbs",
                        "https://www.wiggle.co.uk/cycle/mountain-bikes?g=97#breadcrumbs",
                        "https://www.wiggle.co.uk/cycle/road-bikes"};

                for (int i = 0; i < query.length; i++) {

                    WebDriver driver = new ChromeDriver();

                    driver.get(query[i]);

                    Document doc = Jsoup.parse(driver.getPageSource());

                    Elements products = doc.select("div .bem-product-list--grid .bem-product-list-item--grid");

                    for (int j = 0; j < products.size(); j++) {

                        Elements title = products.get(j).select("div .bem-product-thumb--grid .bem-product-thumb__image-link--grid");

                        Elements image = products.get(j).select("div .bem-product-thumb--grid .bem-product-thumb__image-link--grid .lazy");

                        Elements price = products.get(j).select("div .bem-product-thumb--grid .bem-product-price--grid .bem-product-price__unit--grid");

                        String description = title.attr("title");

                        String[] line = description.split("[(|:|)]");

                        String filter1 = line[0];

                        String[] filter2 = filter1.split(" ");

                        String brand = filter2[0];

                        String model = filter1.replaceAll(brand, "").replaceAll("Mountain Bike|Road Bike|Hardtail Bike" +
                                "|Carbon Bike|Kids Bike|Comp Bike|Alloy Bike|Jump Bike|Track Bike|Dirtjump Bike|Suspension Bike|Womens|" +
                                "Dirt Jump Bike|RS Bike|Comp|COMP|Factory|Race Bike|-|Grey|Blue|Bike|Hardtail bike|Silver", "").trim();

                        String url = title.attr("href");

                        String img = image.attr("data-original");

                        String outputImg = "https:" + img;

                        String filter = "";

                        String outputPrice = "";

                        if (price.text().length() > 10) {

                            String[] bits = price.text().split("-");

                            filter = bits[0];

                            outputPrice = filter.replaceAll("£|,", "").trim();

                        } else {

                            outputPrice = price.text().replaceAll("£|,", "").trim();

                        }

                        //Set and add bike model to database
                        BikeModel bikeModel = new BikeModel();
                        bikeModel.setManufacturer(brand);
                        bikeModel.setModel(model);
                        this.getBikeDAO().findBikeModel(bikeModel);

                        //Set and add bike instance to database
                        BikeInstance bikeInstance = new BikeInstance();
                        bikeInstance.setBikeModel(getBikeDAO().findBikeModel(bikeModel));
                        bikeInstance.setDescription(description);
                        bikeInstance.setSize("N/A");
                        bikeInstance.setColor("N/A");
                        bikeInstance.setImageURL(outputImg);
                        this.getBikeDAO().findBikeInstance(bikeInstance);

                        //Set and add bike comparison to database
                        BikeComparison bikeComparison = new BikeComparison();
                        bikeComparison.setBikeInstance(getBikeDAO().findBikeInstance(bikeInstance));
                        bikeComparison.setPrice(Float.parseFloat(outputPrice));
                        bikeComparison.setWebsiteURL(url);
                        this.getBikeDAO().findBikeComparison(bikeComparison);

                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            //Sleep for the crawl delay, which is in seconds
            try {
                sleep(this.getScrapeDelay());//Sleep is in milliseconds, so we need to multiply the crawl delay by 1000
            } catch (InterruptedException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
}
