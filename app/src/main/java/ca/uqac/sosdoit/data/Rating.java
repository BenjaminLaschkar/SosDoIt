package ca.uqac.sosdoit.data;

import android.text.TextUtils;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ServerValue;

import java.util.Date;

@IgnoreExtraProperties
public class Rating
{
    private int rate;
    private String comment;
    private Date date;

    public Rating() {}

    public Rating(int rate)
    {
        this.rate = rate;
    }

    public int getRate()
    {
        return rate;
    }

    public Rating setRate(int rate)
    {
        this.rate = rate;
        return this;
    }

    public boolean hasComment()
    {
        return comment != null;
    }

    public String getComment()
    {
        return comment;
    }

    public Rating setComment(String comment)
    {
        this.comment = comment;
        return this;
    }

    public Rating setCommentWithCheck(String comment)
    {
        this.comment = TextUtils.isEmpty(comment) ? null : comment;
        return this;
    }

    public boolean hasDate()
    {
        return date != null;
    }

    public Object getTimestamp()
    {
        if (!hasDate()) {
            return ServerValue.TIMESTAMP;
        } else {
            return date.getTime();
        }
    }

    public void setTimestamp(long timestamp)
    {
        date = new Date(timestamp);
    }

    @Exclude
    public Date getDate()
    {
        return date;
    }
}
