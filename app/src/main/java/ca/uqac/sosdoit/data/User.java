package ca.uqac.sosdoit.data;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

/**
 * Data structure for a User
 */
@IgnoreExtraProperties
public class User
{
    @Exclude
    private String uid;

    private String username;
    private String firstName;
    private String lastName;
    private Address address;
    private Set<Skill> skills;

    public User() {}

    public User(String uid, String username, String firstName, String lastName)
    {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    public User(String uid, String username, String firstName, String lastName, Address address)
    {
        this(uid, username, firstName, lastName);
        this.address = address;
    }

    public boolean hasUsername()
    {
        return username != null;
    }

    public boolean hasFirstName()
    {
        return firstName != null;
    }

    public boolean hasLastName()
    {
        return lastName != null;
    }

    public boolean hasAddress()
    {
        return address != null;
    }

    public boolean hasSkills()
    {
        return skills != null;
    }

    public User addSkill(Skill skill)
    {
        if (!hasSkills()) {
            skills = new TreeSet<Skill>();
        }
        skills.add(skill);
        return this;
    }

    public User addSkills(Collection<Skill> skills)
    {
        if (!hasSkills()) {
            skills = new TreeSet<Skill>();
        }
        for (Skill skill : skills) {
            skills.add(skill);
        }
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

    // ----- GETTER ----- //

    @Exclude
    public String getUid()
    {
        return uid;
    }

    public String getUsername()
    {
        return username;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public Address getAddress()
    {
        return address;
    }

    public Set<Skill> getSkills()
    {
        return skills;
    }

    // ----- SETTER ----- //

    public User setUid(String uid)
    {
        this.uid = uid;
        return this;
    }

    public User setUsername(String username)
    {
        this.username = username;
        return this;
    }

    public User setFirstName(String firstName)
    {
        this.firstName = firstName;
        return this;
    }

    public User setLastName(String lastName)
    {
        this.lastName = lastName;
        return this;
    }

    public User setAddress(Address address)
    {
        this.address = address;
        return this;
    }

    public User setSkills(Set<Skill> skills)
    {
        this.skills = skills;
        return this;
    }

    @Override
    public String toString()
    {
        return "(" + (hasUsername() ? username : " ") + ", " + (hasFirstName() ? firstName : " ") + ", " + (hasLastName() ? lastName : " " ) + ", " + (hasAddress() ? address : " ") + ")";
    }
}
