package ca.uqac.sosdoit.data;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * Data structure for an user
 */

@IgnoreExtraProperties
public class User {


    private String idAccount;
    private String name;
    private String lastname;
    private String Address;
    private List<Qualification> Qualification;
    private boolean isWorker;
    private List<Rating> ratings;

    public User(){}

    public User(String idAccount, String name, String lastname, String address, List<Qualification> qualification, boolean isWorker, List<Rating> ratings) {
        this.idAccount = idAccount;
        this.name = name;
        this.lastname = lastname;
        Address = address;
        Qualification = qualification;
        this.isWorker = isWorker;
        this.ratings = ratings;
    }

    public String getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(String idAccount) {
        this.idAccount = idAccount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public List<Qualification> getQualification() {
        return Qualification;
    }

    public void setQualification(List<Qualification> qualification) {
        Qualification = qualification;
    }

    public boolean isWorker() {
        return isWorker;
    }

    public void setWorker(boolean worker) {
        isWorker = worker;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }
}
