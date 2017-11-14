package ca.uqac.sosdoit.data;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Date;

/**
 * Data structure for an Advert
 */
@IgnoreExtraProperties
public class Advert
{
    public enum Status
    {
        AVAILABLE,
        ACCEPTED,
        COMPLETED,
        RATED,
        CANCELED,
        DELETED
    }

    @Exclude
    private String aid;

    private Status status;

    private String advertiserUid;
    private String workerUid;

    private String title;
    private String description;
    private double budget;
    private Tag tag;

    @Exclude
    private Address address;

    private Date postingDate;
    private Date completionDate;

    private ArrayList<Bidder> bidders;

    public Advert()
    {
        this.status = Status.AVAILABLE;
        this.budget = -1.0;
        this.bidders = new ArrayList<Bidder>();
    }

    public Advert(String aid, String idAdvertiser, String title)
    {
        this();
        this.aid = aid;
        this.advertiserUid = idAdvertiser;
        this.title = title;
    }

    public boolean hasWorkerUid()
    {
        return workerUid != null;
    }

    public boolean hasDescription()
    {
        return description != null;
    }

    public boolean hasBudget()
    {
        return budget > 0.0;
    }

    public boolean hasTag()
    {
        return tag != null;
    }

    public boolean hasAddress()
    {
        return address != null;
    }

    public boolean hasPostingDate()
    {
        return postingDate != null;
    }

    public boolean hasCompletionDate()
    {
        return completionDate != null;
    }

    // ----- GETTER ----- //

    @Exclude
    public String getAid()
    {
        return aid;
    }

    public Status getStatus()
    {
        return status;
    }

    public String getAdvertiserUid()
    {
        return advertiserUid;
    }

    public String getWorkerUid()
    {
        return workerUid;
    }

    public String getTitle()
    {
        return title;
    }

    public String getDescription()
    {
        return description;
    }

    public double getBudget()
    {
        return budget;
    }

    public Tag getTag()
    {
        return tag;
    }

    @Exclude
    public Address getAddress()
    {
        return address;
    }

    public Date getPostingDate()
    {
        return postingDate;
    }

    public Date getCompletionDate()
    {
        return completionDate;
    }

    public ArrayList<Bidder> getBidders()
    {
        return bidders;
    }

    // ----- SETTER ----- //

    public Advert setAid(String aid)
    {
        this.aid = aid;
        return this;
    }

    public Advert setStatus(Status status)
    {
        this.status = status;
        return this;
    }

    public Advert setAdvertiserUid(String advertiserUid)
    {
        this.advertiserUid = advertiserUid;
        return this;
    }

    public Advert setWorkerUid(String workerUid)
    {
        this.workerUid = workerUid;
        return this;
    }

    public Advert setTitle(String title)
    {
        this.title = title;
        return this;
    }

    public Advert setDescription(String description)
    {
        this.description = description;
        return this;
    }

    public Advert setBudget(double budget)
    {
        this.budget = budget;
        return this;
    }

    public Advert setTag(Tag tag)
    {
        this.tag = tag;
        return this;
    }

    public Advert setAddress(Address workAddress)
    {
        this.address = address;
        return this;
    }

    public Advert setPostingDate()
    {
        if (!hasPostingDate())
        {
            postingDate = new Date();
        }
        return this;
    }

    public Advert setPostingDate(Date postingDate)
    {
        this.postingDate = postingDate;
        return this;
    }

    public Advert setCompletionDate()
    {
        if (!hasCompletionDate()) {
            this.completionDate = new Date();
        }
        return this;
    }

    public Advert setCompletionDate(Date completionDate)
    {
        this.completionDate = completionDate;
        return this;
    }

    public Advert setBidders(ArrayList<Bidder> bidders)
    {
        this.bidders = bidders;
        return this;
    }
}
