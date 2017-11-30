package ca.uqac.sosdoit.data;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Bid
{
    private String uid;
    private Status status;
    private double offer;
    public Bid() {}

    public Bid(String uid, double offer)
    {
        this.uid = uid;
        this.offer = offer;
        this.status = Status.PENDING;
    }

    @Exclude
    public String getUid()
    {
        return uid;
    }

    public Bid setUid(String uid)
    {
        this.uid = uid;
        return this;
    }

    public double getOffer()
    {
        return offer;
    }

    public Bid setOffer(double offer)
    {
        this.offer = offer;
        return this;
    }

    public Status getStatus()
    {
        return status;
    }

    public Bid setStatus(Status status)
    {
        this.status = status;
        return this;
    }

    public Bid accept()
    {
        status = Status.ACCEPTED;
        return this;
    }

    public Bid reject()
    {
        status = Status.REJECTED;
        return this;
    }

    public enum Status
    {
        PENDING,
        ACCEPTED,
        REJECTED
    }
}
