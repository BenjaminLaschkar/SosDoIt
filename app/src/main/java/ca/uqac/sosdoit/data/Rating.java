package ca.uqac.sosdoit.data;

/**
 * Data Structure for a rate
 */

public class Rating {

    public static double MAX_RATE = 5.0;

    private String idRating;
    private String idRated;
    private String idGiver;
    private double rate;
    private String commentary;

    public Rating(String idRated, String idGiver, double rate, String commentary) {
        this.idRated = idRated;
        this.idGiver = idGiver;
        this.rate = rate;
        this.commentary = commentary;
    }

    public String getIdRating() {
        return idRating;
    }

    public void setIdRating(String idRating) {
        this.idRating = idRating;
    }

    public String getIdRated() {
        return idRated;
    }

    public void setIdRated(String idRated) {
        this.idRated = idRated;
    }

    public String getidGiver() {
        return idGiver;
    }

    public void setidGiver(String idGiver) {
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
}
