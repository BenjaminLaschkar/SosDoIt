package ca.uqac.sosdoit.data;

public class Bidder
{
    public enum Status
    {
        PENDING,
        ACCEPTED,
        REJECTED
    }

    private String uid;
    private float offer;
    private Status status;

    public Bidder() {}

    public Bidder(String uid, float offer)
    {
        this.uid = uid;
        this.offer = offer;
        this.status = Status.PENDING;
    }

    // ----- GETTER ----- //

    public String getUid()
    {
        return uid;
    }

    public float getOffer()
    {
        return offer;
    }

    public Status getStatus()
    {
        return status;
    }

    // ----- SETTER ----- //

    public Bidder setUid(String uid)
    {
        this.uid = uid;
        return this;
    }

    public Bidder setOffer(float offer)
    {
        this.offer = offer;
        return this;
    }

    public Bidder setStatus(Status status)
    {
        this.status = status;
        return this;
    }
}
