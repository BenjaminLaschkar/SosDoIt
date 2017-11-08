package ca.uqac.sosdoit.data;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Data Structure for a Advert
 */

@IgnoreExtraProperties
public class Advert {

    @Exclude
    private String idAdvert;
    private Task task;
    private String description;
    private Address workAddress;
    private AdvertStatus status;
    private double price;
    private String idAdvertiser;
    private String idWorker;

    public Advert(){}

    public Advert(Task task, String description, Address workAddress, AdvertStatus status, double price, String idAdvertiser, String idWorker) {
        this.task = task;
        this.description = description;
        this.workAddress = workAddress;
        this.status = status;
        this.price = price;
        this.idAdvertiser = idAdvertiser;
        this.idWorker = idWorker;
    }

    @Exclude
    public String getIdAdvert() {
        return idAdvert;
    }

    public void setIdAdvert(String idAdvert) {
        this.idAdvert = idAdvert;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Address getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(Address workAddress) {
        this.workAddress = workAddress;
    }

    public AdvertStatus getStatus() {
        return status;
    }

    public void setStatus(AdvertStatus status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getIdAdvertiser() {
        return idAdvertiser;
    }

    public void setIdAdvertiser(String idAdvertiser) {
        this.idAdvertiser = idAdvertiser;
    }

    public String getIdWorker() {
        return idWorker;
    }

    public void setIdWorker(String idWorker) {
        this.idWorker = idWorker;
    }
}
