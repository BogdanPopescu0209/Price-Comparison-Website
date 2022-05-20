package eu.webscraping;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * This class is the BikeDAO
 * which contains the functions
 * to add, update anf find bikes
 * from database
 */
public class BikeDAO {

    /**
     * Creates new sessions when needed to interact with the database
     */
    //Creates new Sessions when we needed to interact with the database
    SessionFactory sessionFactory;

    /**
     * This function adds bike model to database and returns the bike added
     * @param bikeModel
     * @return bikeModel
     */
    //Add bike model
    public BikeModel addBikeModel(BikeModel bikeModel) {

        //Get a new Session instance from the session factory
        Session session = sessionFactory.getCurrentSession();

        //Start transaction
        session.beginTransaction();

        session.save(bikeModel);

        //Commit transaction to save it to database
        session.getTransaction().commit();

        //Close the session and release database connection
        session.close();
        System.out.println("Bike model added to database with ID: " + bikeModel.getId());

        return bikeModel;
    }


    /**
     * This function adds bike instance to database and returns the bike added
     * @param bikeInstance
     * @return bikeInstance
     */
    //Add bike instance
    public BikeInstance addBikeInstance(BikeInstance bikeInstance) {

        //Get a new Session instance from the session factory
        Session session = sessionFactory.getCurrentSession();

        //Start transaction
        session.beginTransaction();

        session.save(bikeInstance);

        //Commit transaction to save it to database
        session.getTransaction().commit();

        //Close the session and release database connection
        session.close();
        System.out.println("Bike added instance to database with ID: " + bikeInstance.getId());

        return bikeInstance;
    }


    /**
     * This function adds bike comparison to database and returns the bike added
     * @param bikeComparison
     * @return bikeComparison
     */
    //Add bike comparison
    public BikeComparison addBikeComparison(BikeComparison bikeComparison) {

        //Get a new Session instance from the session factory
        Session session = sessionFactory.getCurrentSession();

        //Start transaction
        session.beginTransaction();

        session.save(bikeComparison);

        //Commit transaction to save it to database
        session.getTransaction().commit();

        //Close the session and release database connection
        session.close();
        System.out.println("Bike comparison added to database with ID: " + bikeComparison.getId());

        return bikeComparison;
    }


    /**
     * This function finds and returns bike model if bike not found the function adds bike model to database
     * @param bikeModel
     * @return bikeModel
     */
    //Find bike model
    public BikeModel findBikeModel(BikeModel bikeModel) {

        //Get a new Session instance from the session factory and start transaction
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        String queryStr = "from BikeModel where manufacturer='" + bikeModel.getManufacturer() + "' AND model='" + bikeModel.getModel() + "'";

        //Get class
        List<BikeModel> bikeModelList = session.createQuery(queryStr).getResultList();

        //Close the session and release database connection
        session.close();

        if (bikeModelList.size() == 1) {

            return bikeModelList.get(0);

        } else {

            return addBikeModel(bikeModel);
        }
    }


    /**
     * This function finds and returns bike instance if bike not found the function adds bike instance to database
     * @param bikeInstance
     * @return bikeInstance
     */
    //Find bike instance
    public BikeInstance findBikeInstance(BikeInstance bikeInstance) {

        //Get a new Session instance from the session factory and start transaction
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        String queryStr = "from BikeInstance where bike_model_id='" + bikeInstance.getBikeModel().getId() + "'";

        //Get class
        List<BikeInstance> bikeInstanceList = session.createQuery(queryStr).getResultList();

        //Close the session and release database connection
        session.close();

        if (bikeInstanceList.size() == 1) {

            bikeInstance.setId(bikeInstanceList.get(0).getId());
            updateInstance(bikeInstance);
            return bikeInstanceList.get(0);

        } else {

            return addBikeInstance(bikeInstance);

        }
    }


    /**
     * This function finds and returns bike comparison if bike not found the function adds bike comparison to database
     * @param bikeComparison
     * @return bikeComparison
     */
    //Find comparison
    public BikeComparison findBikeComparison(BikeComparison bikeComparison) {

        BikeComparison newBikeComparison = new BikeComparison();

        //Get a new Session instance from the session factory and start transaction
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        String queryStr = "from BikeComparison where website_url='" + bikeComparison.getWebsiteURL() + "'";

        //Get class
        List<BikeComparison> bikeComparisonList = session.createQuery(queryStr).getResultList();

        //Close the session and release database connection
        session.close();

        if(bikeComparisonList.size() == 0){

            newBikeComparison = addBikeComparison(bikeComparison);

        } else {

            for (BikeComparison comparison : bikeComparisonList) {

                if (comparison.getWebsiteURL() == bikeComparison.getWebsiteURL()) {
                    bikeComparison.setId(comparison.getId());
                    newBikeComparison = updateComparison(bikeComparison);
                    break;
                }
            }
        }

        return newBikeComparison;
    }

    /**
     * This function updates bike comparison price
     * @param bikeInstance
     * @return bikeInstance
     */
    public BikeInstance updateInstance(BikeInstance bikeInstance) {

        Session session = sessionFactory.getCurrentSession();

        session.beginTransaction();

        String queryStr = "from BikeInstance where id='" + bikeInstance.getId() + "'";
        List<BikeInstance> bikeInstanceList = session.createQuery(queryStr).getResultList();

        if (bikeInstanceList.size() == 1) {

            if (bikeInstanceList.get(0).getSize().contains("N/A")) {
                bikeInstanceList.get(0).setSize(bikeInstance.getSize());
            }

            if (bikeInstanceList.get(0).getColor().contains("N/A")) {
                bikeInstanceList.get(0).setColor(bikeInstance.getColor());
            }
        }

        session.getTransaction().commit();

        return bikeInstanceList.get(0);
    }


    /**
     * This function updates bike comparison price
     * @param bikeComparison
     * @return bikeComparison
     */
    public BikeComparison updateComparison(BikeComparison bikeComparison) {

        Session session = sessionFactory.getCurrentSession();

        session.beginTransaction();

        String queryStr = "from BikeComparison where id='" + bikeComparison.getId() + "'";
        List<BikeComparison> bikeComparisonList = session.createQuery(queryStr).getResultList();

        if (bikeComparisonList.size() == 1) {

            if (bikeComparisonList.get(0).getWebsiteURL().contains(bikeComparison.getWebsiteURL()))
                bikeComparisonList.get(0).setPrice(bikeComparison.getPrice());
        }

        session.getTransaction().commit();

        return bikeComparisonList.get(0);
    }


    /**
     * This function closes session factory
     */
    //Close Hibernate down and stop threads from running
    public void shutDown() {
        sessionFactory.close();
    }


    /**
     * Return session factory
     * @return session factory
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }


    /**
     * Set session factory
     * @param sessionFactory
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
