package eu.webscraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * EvansCycle class from where bike are loaded to database
 */
public class EvansCycles extends WebScraper {

    /**
     * Function to run thread
     */
    @Override
    public void run() {

        //While loop will keep running until runThread is set to false
        while (this.setRunThread()) {

            try {

                //Array that contains all urls to be scraped
                String[] query = {"https://www.evanscycles.com/bikes/mountain-bikes#dcp=1&dppp=48&OrderBy=rank",
                        "https://www.evanscycles.com/bikes/mountain-bikes#dcp=2&dppp=48&OrderBy=rank",
                        "https://www.evanscycles.com/bikes/road-bikes#dcp=1&dppp=48&OrderBy=rank",
                        "https://www.evanscycles.com/bikes/road-bikes#dcp=2&dppp=48&OrderBy=rank"};

                for (int i = 0; i < query.length; i++) {

                    WebDriver driver = new ChromeDriver();

                    driver.get(query[i]);

                    Document doc = Jsoup.parse(driver.getPageSource());

                    Elements products = doc.select("div .columns3 .s-productscontainer2 li");

                    for (int j = 0; j < products.size(); j++) {

                        Elements size = products.get(j).select("li .sizeDetail");

                        Elements image = products.get(j).select("li .ProductImageList .rtimg");

                        String url = products.get(j).attr("li-url");

                        String brand = products.get(j).attr("li-brand");

                        String filter = products.get(j).attr("li-name");

                        String model = filter.replaceAll("Mountain Bike|Road Bike|Mens|Women's|Ladies|Men's|2020|2021|2022", "").trim();

                        String description = brand + " " + filter;

                        String color = products.get(j).attr("li-variant");

                        String price = products.get(j).attr("li-price");

                        String img = image.attr("src");

                        //Set and add bike model to database
                        BikeModel bikeModel = new BikeModel();
                        bikeModel.setManufacturer(brand);
                        bikeModel.setModel(model);
                        this.getBikeDAO().findBikeModel(bikeModel);

                        //Set and add bike instance to database
                        BikeInstance bikeInstance = new BikeInstance();
                        bikeInstance.setBikeModel(this.getBikeDAO().findBikeModel(bikeModel));
                        bikeInstance.setDescription(description);
                        bikeInstance.setColor(color);
                        bikeInstance.setSize(size.text());
                        bikeInstance.setImageURL(img);
                        this.getBikeDAO().findBikeInstance(bikeInstance);

                        //Set and add bike comparison to database
                        BikeComparison bikeComparison = new BikeComparison();
                        bikeComparison.setBikeInstance(this.getBikeDAO().findBikeInstance(bikeInstance));
                        bikeComparison.setPrice(Float.parseFloat(price));
                        bikeComparison.setWebsiteURL("https://www.evanscycles.com" + url);
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
