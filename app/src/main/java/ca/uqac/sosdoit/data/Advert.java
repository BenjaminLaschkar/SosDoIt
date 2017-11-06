package ca.uqac.sosdoit.data;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Data Structure for a Advert
 */

@IgnoreExtraProperties
public class Advert {

    private String idAdvert;
    private String task;
    private String description;
    private String workAddress;
    private AdvertStatus status;
    private double price;
    private String idWorker;

    public Advert(){}

    public Advert(String task, String description, String workAddress, AdvertStatus status, double price, String idWorker) {
        this.task = task;
        this.description = description;
        this.workAddress = workAddress;
        this.status = status;
        this.price = price;
        this.idWorker = idWorker;
    }

    public String getIdAdvert() {
        return idAdvert;
    }

    public void setIdAdvert(String idAdvert) {
        this.idAdvert = idAdvert;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(String workAddress) {
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

    public String getIdWorker() {
        return idWorker;
    }

    public void setIdWorker(String idWorker) {
        this.idWorker = idWorker;
    }
}
