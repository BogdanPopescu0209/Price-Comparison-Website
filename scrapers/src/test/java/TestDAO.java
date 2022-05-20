import eu.webscraping.BikeDAO;
import eu.webscraping.BikeComparison;
import eu.webscraping.BikeInstance;
import eu.webscraping.BikeModel;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class test from where functions are tested
 */
@DisplayName("Test BikeDAO")
public class TestDAO {

    private static SessionFactory sessionFactory;
    private static BikeDAO bikeDAO = new BikeDAO();

    /**
     * Initialise session factory
     */
    @BeforeAll
    static void initAll() {

        try {
            //Create a builder for the standard service registry
            StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();

            //Load configuration from hibernate configuration file
            standardServiceRegistryBuilder.configure("test_hibernate.cfg.xml");

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

        bikeDAO.setSessionFactory(sessionFactory);

    }

    @BeforeEach
    void init() {
    }

    /**
     * Test from where function add bike is tested by adding
     * a bike to database and checking if id is different from 0
     */
    @Test
    @DisplayName("Test add bike model")
    void testAddBikeModel() {

        BikeModel bikeModel = new BikeModel();
        bikeModel.setManufacturer("Brand");
        bikeModel.setModel("Model1");

        assertEquals(bikeModel.getId(), 0);

        try {
            assertNotEquals(bikeDAO.addBikeModel(bikeModel).getId(), 0);
        } catch (Exception ex) {
            fail("Failed to add bike model. Exception thrown: " + ex.getMessage());
        }
    }

    /**
     * Test from where function add instance is tested by adding
     * a bike to database and checking if id is different from 0
     */
    @Test
    @DisplayName("Test add bike instance")
    void testAddBikeInstance() {

        BikeModel bikeModel = new BikeModel();
        bikeModel.setManufacturer("Brand");
        bikeModel.setModel("Model1");

        BikeInstance bikeInstance = new BikeInstance();
        bikeInstance.setBikeModel(bikeModel);
        bikeInstance.setColor("Blue");
        bikeInstance.setSize("XS, M, L, XL");
        bikeInstance.setDescription("Brand Model1");
        bikeInstance.setImageURL("www.example.com");

        assertEquals(bikeInstance.getId(), 0);

        try {
            assertNotEquals(bikeDAO.addBikeInstance(bikeInstance).getId(), 0);
        } catch (Exception ex) {
            fail("Failed to add bike model. Exception thrown: " + ex.getMessage());
        }
    }

    /**
     * Test from where function add comparison is tested by adding
     * a bike to database and checking if id is different from 0
     */
    @Test
    @DisplayName("Test add bike comparison")
    void testAddBikeComparison() {

        BikeModel bikeModel = new BikeModel();
        bikeModel.setManufacturer("Brand");
        bikeModel.setModel("Model1");

        BikeInstance bikeInstance = new BikeInstance();
        bikeInstance.setBikeModel(bikeModel);
        bikeInstance.setColor("Blue");
        bikeInstance.setSize("XS, M, L, XL");
        bikeInstance.setDescription("Brand Model1");
        bikeInstance.setImageURL("www.example.com");

        BikeComparison bikeComparison = new BikeComparison();
        bikeComparison.setPrice(100f);
        bikeComparison.setBikeInstance(bikeInstance);
        bikeComparison.setWebsiteURL("www.example.com");

        assertEquals(bikeComparison.getId(), 0);

        try {
            assertNotEquals(bikeDAO.addBikeComparison(bikeComparison).getId(), 0);
        } catch (Exception ex) {
            fail("Failed to add bike comparison. Exception thrown: " + ex.getMessage());
        }
    }

    /**
     * Test from where function find bike model is tested by
     * checking the database for a specific model and if model
     * is not found function will add model to database
     */
    /*@Test
    @DisplayName("Test find bike model")
    void testFindBikeModel() {

        BikeModel bikeModel = new BikeModel();
        bikeModel.setManufacturer("Brand");
        bikeModel.setModel("Model1");

        try {
            assertEquals(bikeDAO.findBikeModel(bikeModel).getId(), 7);
        } catch (Exception ex) {
            fail("Failed to find bike model. Exception thrown: " + ex.getMessage());
        }
    }
*/
    /**
     * Test from where function find bike instance is tested by
     * checking the database for a specific instance and if model
     * is not found function will add instanec to database
     */
   /* @Test
    @DisplayName("Test find bike instance")
    void testFindBikeInstance() {

        BikeModel bikeModel = new BikeModel();
        bikeModel.setManufacturer("Brand");
        bikeModel.setModel("Model1");

        BikeInstance bikeInstance = new BikeInstance();
        bikeInstance.setBikeModel(bikeDAO.findBikeModel(bikeModel));
        bikeInstance.setColor("Blue");
        bikeInstance.setSize("XS, M, L, XL");
        bikeInstance.setDescription("Brand Model1");
        bikeInstance.setImageURL("www.example.com");

        try {
            assertEquals(bikeDAO.findBikeInstance(bikeInstance).getId(), 5);
        } catch (Exception ex) {
            fail("Failed to find bike instance. Exception thrown: " + ex.getMessage());
        }
    }*/

    /**
     * Test from where function find bike comparison is tested by
     * checking the database for a specific comparison and if model
     * is not found function will add comparison to database
     */
  /*  @Test
    @DisplayName("Test find bike comparison")
    void testFindBikeComparison() {

        BikeModel bikeModel = new BikeModel();
        bikeModel.setManufacturer("Brand");
        bikeModel.setModel("Model1");

        BikeInstance bikeInstance = new BikeInstance();
        bikeInstance.setBikeModel(bikeDAO.findBikeModel(bikeModel));
        bikeInstance.setColor("Blue");
        bikeInstance.setSize("XS, M, L, XL");
        bikeInstance.setDescription("Brand Model1");
        bikeInstance.setImageURL("www.example.com");

        BikeComparison bikeComparison = new BikeComparison();
        bikeComparison.setBikeInstance(bikeDAO.findBikeInstance(bikeInstance));
        bikeComparison.setPrice(100f);
        bikeComparison.setWebsiteURL("www.example.com");

        try {
            assertEquals(bikeDAO.findBikeComparison(bikeComparison).getId(), 3);
        } catch (Exception ex) {
            fail("Failed to find bike comparison. Exception thrown: " + ex.getMessage());
        }
    }*/

    @Test
    @DisplayName("Test update bike instance")
    void testUpdateBikeInstance() {

        BikeModel bikeModel = new BikeModel();
        bikeModel.setManufacturer("Brand");
        bikeModel.setModel("Model1");

        BikeInstance bikeInstance = new BikeInstance();
        bikeInstance.setId(5);
        bikeInstance.setBikeModel(bikeDAO.findBikeModel(bikeModel));
        bikeInstance.setColor("Blue");
        bikeInstance.setSize("XS, M, L, XL");
        bikeInstance.setDescription("Brand Model1");
        bikeInstance.setImageURL("www.example.com");

        try {
            assertEquals(bikeDAO.updateInstance(bikeInstance).getColor(), "Blue");
        } catch (Exception ex) {
            fail("Failed to find bike comparison. Exception thrown: " + ex.getMessage());
        }
    }

    @Test
    @DisplayName("Test update bike comparison")
    void testUpdateBikeComparison() {

        BikeModel bikeModel = new BikeModel();
        bikeModel.setManufacturer("Brand");
        bikeModel.setModel("Model1");

        BikeInstance bikeInstance = new BikeInstance();
        bikeInstance.setId(5);
        bikeInstance.setBikeModel(bikeDAO.findBikeModel(bikeModel));
        bikeInstance.setColor("Blue");
        bikeInstance.setSize("XS, M, L, XL");
        bikeInstance.setDescription("Brand Model1");
        bikeInstance.setImageURL("www.example.com");

        BikeComparison bikeComparison = new BikeComparison();
        bikeComparison.setId(3);
        bikeComparison.setBikeInstance(bikeDAO.findBikeInstance(bikeInstance));
        bikeComparison.setPrice(200f);
        bikeComparison.setWebsiteURL("www.example.com");

        try {
            assertEquals(bikeDAO.updateComparison(bikeComparison).getPrice(), 200f);
        } catch (Exception ex) {
            fail("Failed to find bike comparison. Exception thrown: " + ex.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * Close session factory
     */
    @AfterAll
    static void tearDownAll() {
        bikeDAO.shutDown();
    }
}
