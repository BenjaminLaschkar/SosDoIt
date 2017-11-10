package ca.uqac.sosdoit.data;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

/** A filter with the parameters which are used to filter the adverts
 */
public class AdvertFilter {

    private List<Task> tasks;
    private Double distanceMax = -1.0; // in kilometers !
    private Double minPrice = -1.0;
    private Double maxPrice = -1.0;


    public AdvertFilter(List<Task> tasks, Double distanceMax, Double minPrice, Double maxPrice) {
        this.tasks = tasks;
        this.distanceMax = distanceMax;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Double getDistanceMax() {
        return distanceMax;
    }

    public void setDistanceMax(Double distanceMax) {
        this.distanceMax = distanceMax;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }
}
