package ca.uqac.sosdoit.database;

import ca.uqac.sosdoit.data.Advert;

/**
 * Callback to handle modification of the advert database
 */

public interface AdvertCallback {

    void onAdvertAdded(Advert advert);
    void onAdvertRemoved(Advert advert);
    void onAdvertChanged(Advert advert);

}
