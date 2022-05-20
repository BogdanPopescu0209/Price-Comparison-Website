package eu.webscraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Tredz class from where bike are loaded to database
 */
public class Tredz extends WebScraper {

    /**
     * Function to run thread
     */
    @Override
    public void run() {

        //While loop will keep running until runThread is set to false
        while (this.setRunThread()) {

            try {

                //Array that contains all urls to be scraped
                String[] query = {"https://www.tredz.co.uk/mountain-bikes",
                        "https://www.tredz.co.uk/mountain-bikes/pgn/2",
                        "https://www.tredz.co.uk/mountain-bikes/pgn/3",
                        "https://www.tredz.co.uk/mountain-bikes/pgn/4",
                        "https://www.tredz.co.uk/mountain-bikes/pgn/5",
                        "https://www.tredz.co.uk/mountain-bikes/pgn/6",
                        "https://www.tredz.co.uk/mountain-bikes/pgn/7",
                        "https://www.tredz.co.uk/road-bikes",
                        "https://www.tredz.co.uk/road-bikes/pgn/2",
                        "https://www.tredz.co.uk/road-bikes/pgn/3",
                        "https://www.tredz.co.uk/road-bikes/pgn/4"};

                for (int i = 0; i < query.length; i++) {

                    Document doc = Jsoup.connect(query[i]).userAgent("Chrome").get();

                    Elements products = doc.select("div .search-results div");

                    for (int j = 0; j < products.size(); j++) {

                        Elements description = products.get(j).select("div .img-wrap img");

                        Elements url = products.get(j).select("a");

                        Elements price = products.get(j).select("a .pricing .sales-price meta");

                        String outputPrice = price.attr("content");

                        String image = description.attr("src");

                        String image1 = description.attr("data-original");

                        String title = description.attr("title");

                        String[] line = title.split(" ");

                        String[] bits = title.split("-");

                        String brand = line[0];

                        String filter = bits[0].replaceAll(brand, "").trim();

                        String model = filter.replaceAll("Mountain Bike|Road Bike|Womens|Mens|2020|2021|2022|-", "").trim();

                        String prodUrl = url.attr("href");

                        if (brand.length() != 0) {

                            //Set and add bike model to database
                            BikeModel bikeModel = new BikeModel();
                            bikeModel.setManufacturer(brand);
                            bikeModel.setModel(model);
                            this.getBikeDAO().findBikeModel(bikeModel);

                            //Set and add bike instance to database
                            BikeInstance bikeInstance = new BikeInstance();
                            bikeInstance.setBikeModel(getBikeDAO().findBikeModel(bikeModel));
                            bikeInstance.setDescription(title);
                            bikeInstance.setSize("N/A");
                            bikeInstance.setColor("N/A");
                            if (image1.length() != 0) {
                                bikeInstance.setImageURL(image1);
                            }

                            if (!image.contains(".gif")) {
                                bikeInstance.setImageURL(image);
                            }
                            this.getBikeDAO().findBikeInstance(bikeInstance);

                            //Set and add bike comparison to database
                            BikeComparison bikeComparison = new BikeComparison();
                            bikeComparison.setBikeInstance(getBikeDAO().findBikeInstance(bikeInstance));
                            bikeComparison.setPrice(Float.parseFloat(outputPrice));
                            bikeComparison.setWebsiteURL("https://www.tredz.co.uk/" + prodUrl);
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
