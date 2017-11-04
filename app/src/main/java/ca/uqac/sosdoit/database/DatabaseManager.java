package ca.uqac.sosdoit.database;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import ca.uqac.sosdoit.data.Advert;
import ca.uqac.sosdoit.data.Rating;
import ca.uqac.sosdoit.data.User;

/**
 * The databaseManager can interact with the data in the database : add, modify, retrieve or remove data.
 */

public class DatabaseManager implements IDatabaseManager {

    private static String USERS = "users";
    private static String ADVERTS = "adverts";
    private static String RATINGS = "ratings";

    // References:
    private DatabaseReference database;
    private DatabaseReference usersRef;
    private DatabaseReference advertsRefs;
    private DatabaseReference ratingsRefs;

    // DatabaseManager instance
    private static DatabaseManager INSTANCE = new DatabaseManager();

    // Callbacks
    private List<UserCallback> userCallbacks;
    private List<AdvertCallback> advertCallbacks;
    private List<RatingCallback> ratingCallbacks;

    private DatabaseManager() {
        database = FirebaseDatabase.getInstance().getReference();
        usersRef = database.child(USERS);
        advertsRefs = database.child(ADVERTS);
        ratingsRefs = database.child(RATINGS);

        userCallbacks = new ArrayList<>();
        advertCallbacks = new ArrayList<>();
        ratingCallbacks = new ArrayList<>();

        // Add the listeners for callback :
        usersRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                User user = dataSnapshot.getValue(User.class);
                for (UserCallback callback: userCallbacks) {
                    callback.onUserAdded(user);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                User user = dataSnapshot.getValue(User.class);
                for (UserCallback callback: userCallbacks) {
                    callback.onUserChanged(user);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                for (UserCallback callback: userCallbacks) {
                    callback.onUserRemoved(user);
                }
            }

            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        });

        advertsRefs.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Advert advert = dataSnapshot.getValue(Advert.class);
                for (AdvertCallback callback: advertCallbacks) {
                    callback.onAdvertAdded(advert);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                Advert advert = dataSnapshot.getValue(Advert.class);
                for (AdvertCallback callback: advertCallbacks) {
                    callback.onAdvertChanged(advert);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                Advert advert = dataSnapshot.getValue(Advert.class);
                for (AdvertCallback callback: advertCallbacks) {
                    callback.onAdvertRemoved(advert);
                }
            }

            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        });

        ratingsRefs.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Rating rating = dataSnapshot.getValue(Rating.class);
                for (RatingCallback callback: ratingCallbacks) {
                    callback.onRatingAdded(rating);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                Rating rating = dataSnapshot.getValue(Rating.class);
                for (RatingCallback callback: ratingCallbacks) {
                    callback.onRatingChanged(rating);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                Rating rating = dataSnapshot.getValue(Rating.class);
                for (RatingCallback callback: ratingCallbacks) {
                    callback.onRatingRemoved(rating);
                }
            }

            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        });
    }

    /** Add an user in the database
     */
    @Override
    public void addUser(User user) {
        String idUser = user.getIdAccount();
        usersRef.setValue(idUser);
        usersRef.child(idUser).setValue(user);
    }

    /**
     * Edit the information of an user
     */
    @Override
    public void editUser(String oldIdAccount, User newUser) {
        usersRef.child(oldIdAccount).setValue(newUser);
    }


    /* Remove an user
     */
    @Override
    public void removeUser(String idAccount) {
        database.child(USERS).child(idAccount).removeValue();
    }

    /** Get an user from his idAccount
     */
    public User getUser(String idAccount) {
        //database.child(USERS).
        return null;
    }

    /**
     * Add an advert in the database
     *
     * @param advert
     */
    @Override
    public void addAdvert(Advert advert) {

    }

    /**
     * Edit the information of an advert
     *
     * @param advert
     */
    @Override
    public void editAdvert(Advert advert) {

    }

    /**
     * Remove an advert
     *
     * @param idAdvert
     */
    @Override
    public void removeAdvert(String idAdvert) {

    }

    /**
     * Get an advert
     *
     * @param idAdvert
     */
    @Override
    public Advert getAdvert(String idAdvert) {
        return null;
    }

    /**
     * Get all the adverts published by an advertiser
     *
     * @param idAdvertiser
     */
    @Override
    public List<Advert> getAllAdvertsPublished(String idAdvertiser) {
        return null;
    }

    /**
     * Get all the adverts published by an advertiser and chosen by a worker
     *
     * @param idAdvertiser
     */
    @Override
    public List<Advert> getAllAdvertsChosen(String idAdvertiser) {
        return null;
    }

    /**
     * Get all the adverts accepted by a worker
     *
     * @param idWorker
     */
    @Override
    public List<Advert> getAllAdvertsAccepted(String idWorker) {
        return null;
    }

    /**
     * Get all the advertsFinished by a worker
     *
     * @param idAdvertiser
     */
    @Override
    public List<Advert> getAllAdvertsFinished(String idAdvertiser) {
        return null;
    }

    /**
     * Add a rating
     *
     * @param rating
     */
    @Override
    public void addRating(Rating rating) {

    }

    /**
     * Edit the information of a rating
     *
     * @param olsIdRating
     * @param rating
     */
    @Override
    public void editRating(String olsIdRating, Rating rating) {

    }

    /**
     * Remove an advert
     *
     * @param idRating
     */
    @Override
    public void removeRating(String idRating) {

    }

    /**
     * Get an advert
     *
     * @param idRating
     */
    @Override
    public Advert getRating(String idRating) {
        return null;
    }

    /**
     * Get all the ratings of an user
     *
     * @param userId
     */
    @Override
    public List<Rating> getUserRatings(String userId) {
        return null;
    }


    /** Get the instance of the database manager
     */
    public static DatabaseManager getInstance() {
        return INSTANCE;
    }

}