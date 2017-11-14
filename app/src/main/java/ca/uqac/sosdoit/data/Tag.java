package ca.uqac.sosdoit.data;

import android.support.annotation.StringRes;

import ca.uqac.sosdoit.R;

/**
 * The list of all the Tags possible for an Advert
 */
public enum Tag
{
    TAG_TEST(R.string.app_name);

    @StringRes
    public int id;

    Tag(@StringRes int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }
}
