package eu.webscraping;

import javax.persistence.*;

/**
 * Class mapped to bike_comparison table
 */
@Entity
@Table(name = "bike_comparison")
public class BikeComparison {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "bike_instance_id", nullable = false)
    BikeInstance bikeInstance;

    @Column(name = "price")
    private Float price;

    @Column(name = "website_url")
    private String websiteURL;

    public BikeComparison() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BikeInstance getBikeInstance() {
        return bikeInstance;
    }

    public void setBikeInstance(BikeInstance bike_instance_id) {
        this.bikeInstance = bike_instance_id;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getWebsiteURL() {
        return websiteURL;
    }

    public void setWebsiteURL(String websiteURL) {
        this.websiteURL = websiteURL;
    }

    @Override
    public String toString() {
        return "BikeComparison{" +
                "id=" + id +
                ", bikeInstance=" + bikeInstance +
                ", price=" + price +
                ", websiteURL='" + websiteURL + '\'' +
                '}';
    }
}
