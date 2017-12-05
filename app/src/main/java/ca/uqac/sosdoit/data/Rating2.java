package ca.uqac.sosdoit.data;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

/**
 * Data Structure for a rating
 */

@IgnoreExtraProperties
public class Rating2
{

    public static double MAX_RATE = 5.0;

    @Exclude
    private String rid;
    private String uidRated;
    private String uidRater;
    private String aid;
    private double rate;
    private String commentary;
    private Date date;

    public Rating2(){}

    public Rating2(String uidRated, String uidRater, String aid, double rate, String commentary) {
        this.uidRated = uidRated;
        this.uidRater = uidRater;
        this.aid = aid;
        this.rate = rate;
        this.commentary = commentary;
    }

    @Exclude
    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getUidRated() {
        return uidRated;
    }

    public void setUidRated(String uidRated) {
        this.uidRated = uidRated;
    }

    public String getUidRater() {
        return uidRater;
    }

    public void setUidRater(String uidRater) {
        this.uidRater = uidRater;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
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
