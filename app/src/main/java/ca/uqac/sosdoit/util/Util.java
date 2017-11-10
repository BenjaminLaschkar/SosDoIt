package ca.uqac.sosdoit.util;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import ca.uqac.sosdoit.data.Rating;

public final class Util
{
    public static final int REGISTRATION_COMPLETE_REQUEST = 1;

    private Util() {}

    public static void showKeyboard(Activity activity, View view)
    {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }

    public static void hideKeyboard(Activity activity, View view)
    {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
            c.setPasswordVisibilityToggleEnabled(true);
            e.removeTextChangedListener(this);
            added = false;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

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
     * @return the distance between the two location (in kilometers)
     */
    public static float distanceBetweenTowLocation(LatLng location1, LatLng location2) {
        float[] results = new float[1];
        Location.distanceBetween(location1.latitude, location1.longitude, location2.latitude, location2.longitude, results);
        return results[0]/1000.0f;
    }

}
