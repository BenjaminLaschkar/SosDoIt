package ca.uqac.sosdoit.database;

import java.util.ArrayList;
import java.util.List;

import ca.uqac.sosdoit.data.Task;

/** A filter with the parameters which are used to filter the adverts
 */
public class AdvertFilter {

    private List<Task> tasks = new ArrayList<>();
    private Double distanceMax = -1.0; // in kilometers !
    private Double minPrice = -1.0;
    private Double maxPrice = -1.0;


    public AdvertFilter() {}

    public boolean hasFilterOnTasks() {
        return !tasks.isEmpty();
    }
    public boolean hasFilterOnDistanceMax() {
        return distanceMax != -1;
    }
    public boolean hasFilterOnMinPrice() {
        return minPrice != -1;
    }
    public boolean hasFilterOnMaxPrice() {
        return maxPrice != -1;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
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
