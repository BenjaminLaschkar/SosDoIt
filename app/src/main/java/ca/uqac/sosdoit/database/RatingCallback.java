package ca.uqac.sosdoit.database;

import ca.uqac.sosdoit.data.Rating2;

/**
 * Callback to handle modification of the rating database
 */

public interface RatingCallback {

    void onRatingAdded(Rating2 rating2);
    void onRatingRemoved(Rating2 rating2);
    void onRatingChanged(Rating2 rating2);

}
