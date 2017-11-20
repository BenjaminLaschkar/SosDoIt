package ca.uqac.sosdoit.util;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.List;

import ca.uqac.sosdoit.data.Coordinates;
import ca.uqac.sosdoit.data.Rating;

public final class Util
{
    public static final int REGISTRATION_REQUEST = 1;
    public static final int RESET_PASSWORD_REQUEST = 2;

    private Util() {}

    public static void toggleKeyboard(Activity activity)
    {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    public static boolean equals(String s1, String s2)
    {
        return (s1 == null && s2 == null) || (s1 != null && s1.equals(s2));
    }

    public static class AdvancedTextWatcher implements TextWatcher
    {
        private boolean added;
        private EditText e;
        private TextInputLayout c;

        public AdvancedTextWatcher(EditText editText, TextInputLayout container) {
            added = false;
            e = editText;
            c = container;
        }

        public void setErrorAndListener(String error) {
            if (!added) {
                c.setPasswordVisibilityToggleEnabled(false);
                e.setError(error);
                e.addTextChangedListener(this);
                added = true;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            c.setPasswordVisibilityToggleEnabled(true);
            e.removeTextChangedListener(this);
            added = false;
        }

        @Override
        public void afterTextChanged(Editable s) {}
    }

    /** Get the mean value from a list of Rating
     *
     * @param ratingList a list of rating of an user
     * @return the mean of the rating
     */
    public static double getFinalRating(List<Rating> ratingList) {
        double nbRatings = ratingList.size();
        double mean = 0.0;
        for (Rating rating: ratingList) {
            mean += rating.getRate();
        }
        return mean;
    }

    /** Get the distance between two locations (in kilometers)
     *
     * @param location1 location 1, not null
     * @param location2 location 2, not null
     * @return the distance between the two locations (in kilometers)
     */
    public static float distanceBetweenTwoLocation(Coordinates location1, Coordinates location2) {
        float[] results = new float[1];
        Location.distanceBetween(location1.getLatitude(), location1.getLongitude(), location2.getLatitude(), location2.getLongitude(), results);
        return results[0]/1000.0f;
    }
}
