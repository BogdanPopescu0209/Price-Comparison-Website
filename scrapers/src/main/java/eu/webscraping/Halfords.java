package eu.webscraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Halfords class from where bike are loaded to database
 */
public class Halfords extends WebScraper {

    /**
     * Function to run thread
     */
    @Override
    public void run() {

        //While loop will keep running until runThread is set to false
        while (this.setRunThread()) {

            try {

                //Array that contains all urls to be scraped
                String[] urls = {"https://www.halfords.com/bikes/mountain-bikes/",
                        "https://www.halfords.com/bikes/mountain-bikes/?start=24&sz=24",
                        "https://www.halfords.com/bikes/mountain-bikes/?start=48&sz=24"
                };

                for (int i = 0; i < urls.length; i++) {

                    Document doc = Jsoup.connect(urls[i]).userAgent("Chrome").get();

                    Elements products = doc.select("div .b-product__grid .b-product");

                    for (int j = 0; j < products.size(); j++) {

                        Elements description = products.get(j).select("div .b-product__inner a");

                        Elements image = products.get(j).select("div .b-product__inner a img");

                        Elements price = products.get(j).select("div .b-product__inner .b-product__content .b-product__actions .b-product__info  .b-product__price .price .b-price__sale");

                        String outputPrice = price.text().replaceAll("£|,", "").trim();

                        String title = description.attr("title");

                        String[] line = title.split("-");

                        String prodDescription = line[0];

                        String[] bits = prodDescription.split(" ");

                        String brand = bits[0];

                        String filter = prodDescription.replaceAll(brand, "").trim();

                        String model = filter.replaceAll(" Mens|Mountain Bike|Womens|Mountain Bike|2020|2021|2022", "").trim();

                        String url = description.attr("href");

                        String img = image.attr("data-src");

                        String color = "";

                        String size = "";

                        if (line.length == 3) {

                            color = line[1];

                            size = line[2];

                        } else {

                            color = "N/A";
                            size = "N/A";
                        }

                        if (brand.length() != 0) {

                            //Set and add bike model to database
                            BikeModel bikeModel = new BikeModel();
                            bikeModel.setManufacturer(brand);
                            bikeModel.setModel(model);
                            this.getBikeDAO().findBikeModel(bikeModel);

                            //Set and add bike instance to database
                            BikeInstance bikeInstance = new BikeInstance();
                            bikeInstance.setBikeModel(getBikeDAO().findBikeModel(bikeModel));
                            bikeInstance.setDescription(prodDescription);
                            bikeInstance.setColor(color);
                            bikeInstance.setSize(size);
                            bikeInstance.setImageURL(img);
                            this.getBikeDAO().findBikeInstance(bikeInstance);

                            //Set and add bike comparison to database
                            BikeComparison bikeComparison = new BikeComparison();
                            bikeComparison.setBikeInstance((this.getBikeDAO().findBikeInstance(bikeInstance)));
                            bikeComparison.setWebsiteURL(url);
                            bikeComparison.setPrice(Float.parseFloat(outputPrice));
                            this.getBikeDAO().findBikeComparison(bikeComparison);
                        }
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
