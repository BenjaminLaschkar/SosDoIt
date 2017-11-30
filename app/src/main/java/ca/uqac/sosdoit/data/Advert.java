package ca.uqac.sosdoit.data;

import android.text.TextUtils;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.Date;

/**
 * Advert data structure
 */

@IgnoreExtraProperties
public class Advert
{
    @Exclude
    private String aid;

    private Status status;

    private String advertiserUid;
    private String workerUid;

    private String title;
    private String description;
    private Tag tag;

    private Date postingDate;
    private Date completionDate;

    private Bid bid;

    public Advert() {}

    public Advert(String idAdvertiser, String title)
    {
        this.aid = aid;
        this.status = Status.AVAILABLE;
        this.advertiserUid = idAdvertiser;
        this.title = title;
    }

    public boolean hasAid()
    {
        return aid != null;
    }

    @Exclude
    public String getAid()
    {
        return aid;
    }

    public Advert setAid(String aid)
    {
        this.aid = aid;
        return this;
    }

    public Status getStatus()
    {
        return status;
    }

    public Advert setStatus(Status status)
    {
        this.status = status;
        return this;
    }

    public String getAdvertiserUid()
    {
        return advertiserUid;
    }

    public Advert setAdvertiserUid(String advertiserUid)
    {
        this.advertiserUid = advertiserUid;
        return this;
    }

    public boolean hasWorkerUid()
    {
        return workerUid != null;
    }

    public String getWorkerUid()
    {
        return workerUid;
    }

    public Advert setWorkerUid(String workerUid)
    {
        this.workerUid = workerUid;
        return this;
    }

    public String getTitle()
    {
        return title;
    }

    public Advert setTitle(String title)
    {
        this.title = title;
        return this;
    }

    public boolean hasDescription()
    {
        return description != null;
    }

    public String getDescription()
    {
        return description;
    }

    public Advert setDescription(String description)
    {
        this.description = description;
        return this;
    }

    public Advert setDescriptionWithCheck(String description)
    {
        this.description = TextUtils.isEmpty(description) ? null : description;
        return this;
    }

    public boolean hasTag()
    {
        return tag != null;
    }

    public Tag getTag()
    {
        return tag;
    }

    public Advert setTag(Tag tag)
    {
        this.tag = tag;
        return this;
    }

    public boolean hasPostingDate()
    {
        return postingDate != null;
    }

    public Object getPostingTimestamp()
    {
        if (!hasPostingDate()) {
            return ServerValue.TIMESTAMP;
        } else {
            return postingDate.getTime();
        }
    }

    public void setPostingTimestamp(long postingTimestamp)
    {
        postingDate = new Date(postingTimestamp);
    }

    @Exclude
    public Date getPostingDate()
    {
        return postingDate;
    }

    public Advert setPostingDate(Date postingDate)
    {
        this.postingDate = postingDate;
        return this;
    }

    public boolean hasCompletionDate()
    {
        return completionDate != null;
    }

    public Object getCompletionTimestamp()
    {
        if (status == Status.COMPLETED && !hasCompletionDate()) {
            return ServerValue.TIMESTAMP;
        } else if (hasCompletionDate()) {
            return completionDate.getTime();
        } else {
            return null;
        }
    }

    public void setCompletionTimestamp(long completionTimestamp)
    {
        completionDate = new Date(completionTimestamp);
    }

    @Exclude
    public Date getCompletionDate()
    {
        return completionDate;
    }

    public Advert setCompletionDate(Date completionDate)
    {
        this.completionDate = completionDate;
        return this;
    }

    public boolean hasBid()
    {
        return bid != null;
    }

    @Exclude
    public Bid getBid()
    {
        return bid;
    }

    public Advert setBid(Bid bid)
    {
        this.bid = bid;
        return this;
    }

    @Override
    public String toString()
    {
        return String.format("Advert: {%n  aid: %s,%n  status: %s,%n  advertiser: %s,%n  title: %s,%n  description: %s%n}", aid, status, advertiserUid, title, hasDescription() ? description : "");
    }

    public enum Status
    {
        AVAILABLE,
        ACCEPTED,
        COMPLETED,
        RATED,
        CANCELED,
        DELETED
    }
}
