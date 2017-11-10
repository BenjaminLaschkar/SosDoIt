package ca.uqac.sosdoit.data;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * Data structure for an user
 */

@IgnoreExtraProperties
public class User {

    @Exclude
    private String idAccount;
    private String firstname;
    private String lastname;
    private String username;
    private Address address;
    private List<Qualification> qualification;
    private boolean isWorker;

    public User(){}

    public User(String idAccount, String firstname, String lastname, String username, Address address, List<Qualification> qualification, boolean isWorker) {
        this.idAccount = idAccount;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.address = address;
        this.qualification = qualification;
        this.isWorker = isWorker;
    }

    @Exclude
    public String getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(String idAccount) {
        this.idAccount = idAccount;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Qualification> getQualification() {
        return qualification;
    }

    public void setQualification(List<Qualification> qualification) {
        this.qualification = qualification;
    }

    public boolean isWorker() {
        return isWorker;
    }

    public void setWorker(boolean worker) {
        isWorker = worker;
    }
}
