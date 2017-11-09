package ca.uqac.sosdoit.data;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

/**
 * Data Structure for a rating
 */

@IgnoreExtraProperties
public class Rating {

    public static double MAX_RATE = 5.0;

    @Exclude
    private String idRating;
    private String idUserRated;
    private String idGiver;
    private double rate;
    private String commentary;
    private Date date;

    public Rating(){}

    public Rating(String idUserRated, String idGiver, double rate, String commentary) {
        this.idUserRated = idUserRated;
        this.idGiver = idGiver;
        this.rate = rate;
        this.commentary = commentary;
    }

    @Exclude
    public String getIdRating() {
        return idRating;
    }

    public void setIdRating(String idRating) {
        this.idRating = idRating;
    }

    public String getIdUserRated() {
        return idUserRated;
    }

    public void setIdUserRated(String idUserRated) {
        this.idUserRated = idUserRated;
    }

    public String getIdGiver() {
        return idGiver;
    }

    public void setIdGiver(String idGiver) {
        this.idGiver = idGiver;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
