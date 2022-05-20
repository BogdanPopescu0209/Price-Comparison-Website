package eu.webscraping;

import javax.persistence.*;

/**
 * Class mapped to bike_instance table
 */
@Entity
@Table(name = "bike_instance")
public class BikeInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name="bike_model_id", nullable=false)
    BikeModel bikeModel;

    @Column(name = "description")
    private String description;

    @Column(name = "color")
    private String color;

    @Column(name = "size")
    private String size;

    @Column(name = "image_url")
    private String imageURL;

    public BikeInstance() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BikeModel getBikeModel() {
        return bikeModel;
    }

    public void setBikeModel(BikeModel bike_model_id) {
        this.bikeModel = bike_model_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return "BikeInstance{" +
                "id=" + id +
                ", bikeModel=" + bikeModel +
                ", description='" + description + '\'' +
                ", color='" + color + '\'' +
                ", size='" + size + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }
}
