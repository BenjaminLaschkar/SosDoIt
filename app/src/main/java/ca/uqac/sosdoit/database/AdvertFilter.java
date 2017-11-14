package ca.uqac.sosdoit.database;

import java.util.ArrayList;
import java.util.List;

import ca.uqac.sosdoit.data.Tag;

/** A filter with the parameters which are used to filter the adverts
 */
public class AdvertFilter {

    private List<Tag> tags = new ArrayList<>();
    private Double distanceMax = -1.0; // in kilometers !
    private Double minPrice = -1.0;
    private Double maxPrice = -1.0;


    public AdvertFilter() {}

    public boolean hasFilterOnTasks() {
        return !tags.isEmpty();
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

    public void addTask(Tag tag) {
        tags.add(tag);
    }

    public void removeTask(Tag tag) {
        tags.remove(tag);
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
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
