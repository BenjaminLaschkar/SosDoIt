package ca.uqac.sosdoit.database;

import ca.uqac.sosdoit.data.Rating;

/**
 * Callback to handle modification of the rating database
 */

public interface RatingCallback {

    void onRatingAdded(Rating rating);
    void onRatingRemoved(Rating rating);
    void onRatingChanged(Rating rating);

}
