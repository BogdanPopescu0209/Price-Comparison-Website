package eu.webscraping;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

/**
 * This is the main method from where the application is run
 */
public class App {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        WebScraperManager webScraperManager = (WebScraperManager) context.getBean("scraperManager");

        //Start scrapping
        webScraperManager.startScraping();

    }

}
