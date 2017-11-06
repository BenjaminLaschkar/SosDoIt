package ca.uqac.sosdoit.database;

import ca.uqac.sosdoit.data.User;

/**
 * Callback to handle modification of the user database
 */

public interface UserCallback {

    void onUserAdded(User user);
    void onUserRemoved(User user);
    void onUserChanged(User user);

}
