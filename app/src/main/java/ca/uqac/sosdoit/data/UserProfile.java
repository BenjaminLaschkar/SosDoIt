package ca.uqac.sosdoit.data;

import android.text.TextUtils;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ServerValue;

import java.util.Date;

/**
 * User public profile data structure
 */

@IgnoreExtraProperties
public class UserProfile
{
    private String username;
    private Date registrationDate;

    UserProfile() {}

    UserProfile(UserProfile other)
    {
        this.username = other.username;
        this.registrationDate = other.registrationDate;
    }

    public boolean hasUsername()
    {
        return username != null;
    }

    public String getUsername()
    {
        return username;
    }

    public UserProfile setUsername(String username)
    {
        this.username = username;
        return this;
    }

    public UserProfile setUsernameWithCheck(String username)
    {
        if (!TextUtils.isEmpty(username)) {
            this.username = username;
        }
        return this;
    }

    public Object getTimestamp()
    {
        if (registrationDate == null) {
            return ServerValue.TIMESTAMP;
        } else {
            return registrationDate.getTime();
        }
    }

    public void setTimestamp(long timeStamp)
    {
        registrationDate = new Date(timeStamp);
    }

    public boolean hasRegistrationDate()
    {
        return registrationDate != null;
    }

    @Exclude
    public Date getRegistrationDate()
    {
        return registrationDate;
    }

    public boolean equals(UserProfile other)
    {
        return username.equals(other.username) && registrationDate.equals(other.registrationDate);
    }

    @Override
    public String toString()
    {
        return String.format("Profile: {%n  username: %s,%n  registration: %s%n}", username, registrationDate);
    }
}
