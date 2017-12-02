package ca.uqac.sosdoit.data;

import android.content.Context;
import android.text.TextUtils;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

import ca.uqac.sosdoit.util.Util;

/**
 * User data structure
 */

@IgnoreExtraProperties
public class User
{
    private String uid;
    private String email;

    private UserProfile profile;

    private String firstName;
    private String lastName;
    private Address address;

    public User() {}

    public User(User other)
    {
        this.uid = other.uid;
        this.email = other.email;
        this.profile = new UserProfile(other.profile);
        this.firstName = other.firstName;
        this.lastName = other.lastName;
        if (other.hasAddress()) {
            this.address = new Address(other.address);
        }
    }

    public User(String uid, String username)
    {
        this.uid = uid;
        this.profile = new UserProfile().setUsername(username);
    }

    @Exclude
    public String getUid()
    {
        return uid;
    }

    public User setUid(String uid)
    {
        this.uid = uid;
        return this;
    }

    public boolean hasEmail()
    {
        return email != null;
    }

    @Exclude
    public String getEmail()
    {
        return email;
    }

    public User setEmail(String email)
    {
        this.email = email;
        return this;
    }

    public UserProfile getProfile()
    {
        return profile;
    }

    public User setProfile(UserProfile profile)
    {
        this.profile = profile;
        return this;
    }

    public boolean hasUsername()
    {
        return profile.hasUsername();
    }

    @Exclude
    public String getUsername()
    {
        return profile.getUsername();
    }

    public User setUsername(String username)
    {
        profile.setUsername(username);
        return this;
    }

    public User setUsernameWithCheck(String username)
    {
        profile.setUsernameWithCheck(username);
        return this;
    }

    public boolean hasRegistrationDate()
    {
        return profile.hasRegistrationDate();
    }

    @Exclude
    public Date getRegistrationDate()
    {
        return profile.getRegistrationDate();
    }

    public boolean hasFirstName()
    {
        return firstName != null;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public User setFirstName(String firstName)
    {
        this.firstName = firstName;
        return this;
    }

    public User setFirstNameWithCheck(String firstName)
    {
        this.firstName = TextUtils.isEmpty(firstName) ? null : firstName;
        return this;
    }

    public boolean hasLastName()
    {
        return lastName != null;
    }

    public String getLastName()
    {
        return lastName;
    }

    public User setLastName(String lastName)
    {
        this.lastName = lastName;
        return this;
    }

    public User setLastNameWithCheck(String lastName)
    {
        this.lastName = TextUtils.isEmpty(lastName) ? null : lastName;
        return this;
    }

    public boolean hasAddress()
    {
        return address != null;
    }

    public Address getAddress()
    {
        return address;
    }

    public User setAddress(Address address)
    {
        this.address = address;
        return this;
    }

    public User setHouseNumberWithCheck(String houseNumber)
    {
        if (!hasAddress()) {
            address = new Address();
        }
        address.setHouseNumberWithCheck(houseNumber);
        return this;
    }

    public User setStreetWithCheck(String street)
    {
        if (!hasAddress()) {
            address = new Address();
        }
        address.setStreetWithCheck(street);
        return this;
    }

    public User setAdditionalAddressWithCheck(String additionalAddress)
    {
        if (!hasAddress()) {
            address = new Address();
        }
        address.setAdditionalAddressWithCheck(additionalAddress);
        return this;
    }

    public User setCityWithCheck(String city)
    {
        if (!hasAddress()) {
            address = new Address();
        }
        address.setCityWithCheck(city);
        return this;
    }

    public User setStateWithCheck(String state)
    {
        if (!hasAddress()) {
            address = new Address();
        }
        address.setStateWithCheck(state);
        return this;
    }

    public User setPostalCodeWithCheck(String postalCode)
    {
        if (!hasAddress()) {
            address = new Address();
        }
        address.setPostalCodeWithCheck(postalCode);
        return this;
    }

    public User setCountryWithCheck(String country)
    {
        if (!hasAddress()) {
            address = new Address();
        }
        address.setCountryWithCheck(country);
        return this;
    }

    public User clearAddressIfEmpty()
    {
        if (hasAddress() && address.isEmpty()) {
            address = null;
        }
        return this;
    }

    public void findAddressCoordinates(Context context)
    {
        if (hasAddress()) {
            address.findCoordinates(context);
        }
    }

    public boolean equals(User other)
    {
        return uid.equals(other.uid) && Util.equals(email, other.email) && profile.equals(other.profile) && Util.equals(firstName, other.firstName) && Util.equals(lastName, other.lastName) && (!hasAddress() && !other.hasAddress() || (hasAddress() && other.hasAddress() && address.equals(other.address)));
    }

    @Override
    public String toString()
    {
        return String.format("%nUser: {%n  uid: %s,%n  username: %s,%n  registration: %s%n}", getUid(), getUsername(), getRegistrationDate());
    }

    /*

    public boolean hasSkills()
    {
        return skills != null;
    }

    private Set<Skill> skills;

    public Set<Skill> getSkills()
    {
        return skills;
    }

    public User setSkills(Set<Skill> skills)
    {
        this.skills = skills;
        return this;
    }



    public User addSkill(Skill skill)
    {
        if (!hasSkills()) {
            skills = new TreeSet<>();
        }
        skills.add(skill);
        return this;
    }

    public User addSkills(Collection<Skill> skills)
    {
        if (!hasSkills()) {
            skills = new TreeSet<>();
        }
        this.skills.addAll(skills);
        return this;
    }

    public User removeSkill(Skill skill)
    {
        if (hasSkills()) {
            skills.remove(skill);
        }
        return this;
    }

    public User removeSkills(Collection<Skill> skill)
    {
        if (hasSkills()) {
            skills.removeAll(skill);
        }
        return this;
    }

    */
}
