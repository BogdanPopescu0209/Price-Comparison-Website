package eu.webscraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * BuyABike class from where the bikes are loaded to database
 */
public class BuyABike extends WebScraper {

    /**
     * Function to run thread
     */
    @Override
    public void run() {

        //While loop will keep running until runThread is set to false
        while (this.setRunThread()) {

            try {

                //Array that contains all urls to be scraped
                String[] query = {"https://www.buyabike.co.uk/bikes/mountain-bikes",
                        "https://www.buyabike.co.uk/bikes/road-bikes"};

                for (int i = 0; i < query.length; i++) {

                    WebDriver driver = new ChromeDriver();

                    driver.get(query[i]);

                    Document doc = Jsoup.parse(driver.getPageSource());

                    Elements products = doc.select("div .productContainer .item");

                    for (int j = 0; j < products.size(); j++) {

                        Elements title = products.get(j).select("div .imageContainer");

                        Elements image = products.get(j).select("div .imageContainer img");

                        Element price = products.get(j).getElementById("lblNow");

                        String outputPrice = price.text().replaceAll("£|,", "").trim();

                        String description = title.attr("title");

                        String url = title.attr("href");

                        String[] bits = description.split(" ");

                        String brand = "";
                        String color = "N/A";

                        if (bits[0].matches("[0-9]+")) {
                            brand = bits[1];
                            color = bits[bits.length - 1];
                        } else {

                            brand = bits[0];

                        }

                        String filter = description.replaceAll(brand, "").trim();
                        String[] filter2 = filter.split("in");
                        String model = filter2[0].replaceFirst("2020|2021|2022|Womens|Mens|Mountain Bike|Road Bike|Hardtail|Grey|Carbon|Alloy|Aero", "").trim();

                        String img = image.attr("data-src");

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
                        bikeInstance.setColor(color);
                        bikeInstance.setImageURL("https://www.buyabike.co.uk" + img);
                        this.getBikeDAO().findBikeInstance(bikeInstance);

                        //Set and add bike comparison to database
                        BikeComparison bikeComparison = new BikeComparison();
                        bikeComparison.setBikeInstance(getBikeDAO().findBikeInstance(bikeInstance));
                        bikeComparison.setPrice(Float.parseFloat(outputPrice));
                        bikeComparison.setWebsiteURL("https://www.buyabike.co.uk" + url);
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
