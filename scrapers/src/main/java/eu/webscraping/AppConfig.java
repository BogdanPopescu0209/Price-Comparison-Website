package eu.webscraping;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

/**
 * This class initialises session factory,
 * scrapers and get them ready to scrape
 */
@Configuration
public class AppConfig {

    SessionFactory sessionFactory;

    /**
     * @return scraperManager array containing all the scrapers
     * set and ready to scrape
     */
    @Bean
    public WebScraperManager scraperManager() {

        WebScraperManager scraperManager = new WebScraperManager();
        ArrayList<WebScraper> scraperArrayList = new ArrayList();
        scraperArrayList.add(evansCycles());
        scraperArrayList.add(halfords());
        scraperArrayList.add(tredz());
        scraperArrayList.add(wiggle());
        scraperArrayList.add(buyABike());
        scraperManager.setWebScraperArrayList(scraperArrayList);

        return scraperManager;
    }

    /**
     * @return scraper1 with BikeDAO and
     * scrape delay initialise
     */
    @Bean
    public EvansCycles evansCycles() {

        EvansCycles scraper1 = new EvansCycles();
        scraper1.setBikeDAO(bikeDAO());
        scraper1.setScrapeDelay(10000);

        return scraper1;
    }

    /**
     * @return scraper2 with BikeDAO and
     * scrape delay initialise
     */
    @Bean
    public Halfords halfords() {

        Halfords scraper2 = new Halfords();
        scraper2.setBikeDAO(bikeDAO());
        scraper2.setScrapeDelay(20000);

        return scraper2;
    }

    /**
     * @return scraper3 with BikeDAO and
     * scrape delay initialise
     */
    @Bean
    public Tredz tredz() {

        Tredz scraper3 = new Tredz();
        scraper3.setBikeDAO(bikeDAO());
        scraper3.setScrapeDelay(30000);

        return scraper3;
    }

    /**
     * @return scraper4 with BikeDAO and
     * scrape delay initialise
     */
    @Bean
    public Wiggle wiggle() {

        Wiggle scraper4 = new Wiggle();
        scraper4.setBikeDAO(bikeDAO());
        scraper4.setScrapeDelay(40000);

        return scraper4;
    }

    /**
     * @return scraper5 with BikeDAO and
     * scrape delay initialise
     */
    @Bean
    public BuyABike buyABike() {

        BuyABike scraper5 = new BuyABike();
        scraper5.setBikeDAO(bikeDAO());
        scraper5.setScrapeDelay(50000);

        return scraper5;
    }

    /**
     * @return bikeDAO with session factory initialise
     */
    @Bean
    public BikeDAO bikeDAO() {

        BikeDAO bikeDAO = new BikeDAO();
        bikeDAO.setSessionFactory(sessionFactory());

        return bikeDAO;
    }

    /**
     * @return session factory that was build based on
     * the hibernate.cfg.xml
     */
    @Bean
    public SessionFactory sessionFactory() {

        if (sessionFactory == null) {

            //Set up the session factory
            try {
                //Create a builder for the standard service registry
                StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();

                //Load configuration from hibernate configuration file
                standardServiceRegistryBuilder.configure("hibernate.cfg.xml");

                //Create the registry that will be used to build the session factory
                StandardServiceRegistry registry = standardServiceRegistryBuilder.build();

                try {

                    //Create the session factory - this is the goal of the init method.
                    sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

                } catch (Exception e) {

                    //The registry would be destroyed by the SessionFactory
                    System.err.println("Session factory build failed!");
                    e.printStackTrace();
                    StandardServiceRegistryBuilder.destroy(registry);

                }

                //Output result
                System.out.println("Session factory build!");

            } catch (Throwable ex) {

                // Make sure you log the exception, as it might be swallowed
                System.err.println("Session factory creation failed: " + ex);

            }
        }
        return sessionFactory;
    }
}
