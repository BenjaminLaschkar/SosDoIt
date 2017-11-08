package ca.uqac.sosdoit.database;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ca.uqac.sosdoit.data.Address;
import ca.uqac.sosdoit.data.Advert;
import ca.uqac.sosdoit.data.Qualification;
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

    /** Get the instance of the database manager
     */
    public static DatabaseManager getInstance() {
        return INSTANCE;
    }

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

                if ( !userCallbacks.isEmpty() ) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        user.setIdAccount(dataSnapshot.getKey());
                    }
                    for (UserCallback callback : userCallbacks) {
                        callback.onUserAdded(user);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                if ( !userCallbacks.isEmpty() ) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        user.setIdAccount(dataSnapshot.getKey());
                    }
                    for (UserCallback callback : userCallbacks) {
                        callback.onUserChanged(user);
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                if ( !userCallbacks.isEmpty() ) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        user.setIdAccount(dataSnapshot.getKey());
                    }
                    for (UserCallback callback : userCallbacks) {
                        callback.onUserRemoved(user);
                    }
                }
            }

            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        });

        advertsRefs.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if ( !advertCallbacks.isEmpty() ) {
                    Advert advert = dataSnapshot.getValue(Advert.class);
                    for (AdvertCallback callback : advertCallbacks) {
                        callback.onAdvertAdded(advert);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                if ( !advertCallbacks.isEmpty() ) {
                    Advert advert = dataSnapshot.getValue(Advert.class);
                    for (AdvertCallback callback : advertCallbacks) {
                        callback.onAdvertChanged(advert);
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                if ( !advertCallbacks.isEmpty() ) {
                    Advert advert = dataSnapshot.getValue(Advert.class);
                    for (AdvertCallback callback : advertCallbacks) {
                        callback.onAdvertRemoved(advert);
                    }
                }
            }

            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        });

        ratingsRefs.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if ( !ratingCallbacks.isEmpty() ) {
                    Rating rating = dataSnapshot.getValue(Rating.class);
                    for (RatingCallback callback : ratingCallbacks) {
                        callback.onRatingAdded(rating);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                if ( !ratingCallbacks.isEmpty() ) {
                    Rating rating = dataSnapshot.getValue(Rating.class);
                    for (RatingCallback callback : ratingCallbacks) {
                        callback.onRatingChanged(rating);
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                if ( !ratingCallbacks.isEmpty() ) {
                    Rating rating = dataSnapshot.getValue(Rating.class);
                    for (RatingCallback callback : ratingCallbacks) {
                        callback.onRatingRemoved(rating);
                    }
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
        usersRef.child(idUser).setValue(user);
    }


    @Override
    public void addUser(String idAccount, String firstname, String lastname, String pseudo) {
        User user = new User(idAccount, firstname, lastname, pseudo, null, null, false);
        addUser(user);
    }

    /**
     * Edit the address of an user
     */
    @Override
    public void editAddressUser(String idAccount, Address address) {
        usersRef.child(idAccount).child("address").setValue(address);
    }

    /**
     * Edit the worker profile of the User
     */
    @Override
    public void EditWorkerProfileUser(String idAccount, boolean isWorker, List<Qualification> qualifications) {
        usersRef.child(idAccount).child("isWorker").setValue(true);
        usersRef.child(idAccount).child("qualifications").setValue(qualifications);
    }

    /**
     * Edit the information of an user
     */
    @Override
    public void editUser(String oldIdAccount, User newUser) {
        usersRef.child(oldIdAccount).setValue(newUser);
    }


    /** Remove an user
     */
    @Override
    public void removeUser(String idAccount) {
        usersRef.child(idAccount).removeValue();
    }

    /** Get an user from his idAccount
     * Return null if not found
     */
    @Override
    public void getUser(final String idUser, final UserResult result) {
        Query query = usersRef.child(idUser);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    user.setIdAccount(idUser);
                }
                result.call(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    /**
     * Add an advert in the database
     * Create an new ID when added
     *
     */
    @Override
    public void addAdvert(Advert advert) {
        advertsRefs.push().setValue(advert);
    }

    /**
     * Edit the information of an advert
     */
    @Override
    public void editAdvert(String oldIdAdvert, Advert advert) {
        advertsRefs.child(oldIdAdvert).setValue(advert);
    }

    /**
     * Remove an advert
     */
    @Override
    public void removeAdvert(String idAdvert) {
        advertsRefs.child(idAdvert).removeValue();
    }

    /**
     * Get an advert
     */
    @Override
    public void getAdvert(String idAdvert,final AdvertResult result) {
    }

    /**
     * Get all the adverts published by an advertiser
     */
    @Override
    public void getAllAdvertsPublished(String idAdvertiser,final AdvertListResult result) {
    }

    /**
     * Get all the adverts published by an advertiser and chosen by a worker
     */
    @Override
    public void getAllAdvertsChosen(String idAdvertiser,final AdvertListResult result) {

    }

    /**
     * Get all the adverts accepted by a worker
     */
    @Override
    public void getAllAdvertsAccepted(String idWorker,final AdvertListResult result) {

    }

    /**
     * Get all the advertsFinished by a worker
     */
    @Override
    public void getAllAdvertsFinished(String idAdvertiser,final AdvertListResult result) {

    }

    /**
     * Add a rating
     */
    @Override
    public void addRating(Rating rating) {
        String idRating = rating.getIdRating();
        ratingsRefs.child(idRating).setValue(rating);
    }

    /**
     * Edit the information of a rating
     */
    @Override
    public void editRating(String oldIdRating, Rating rating) {
        ratingsRefs.child(oldIdRating).setValue(rating);
    }

    /**
     * Remove an advert
     */
    @Override
    public void removeRating(String idRating) {
        ratingsRefs.child(idRating).removeValue();
    }

    /**
     * Get an advert
     */
    @Override
    public void getRating(String idRating,final RatingResult result) {

    }

    /**
     * Get all the ratings of an user
     */
    @Override
    public void getUserRatings(String userId,final RatingListResult result) {

    }

    /**
     * Get all the ratings given by a user
     */
    @Override
    public void getGivenUserRating(String idUser, RatingListResult result) {

    }

    /** Register an UserCallback
     */
    public void addUserCallback(UserCallback callback) {
        userCallbacks.add(callback);
    }

    /** Register an AdvertCallback
     */
    public void addAdvertCallback(AdvertCallback callback) {
        advertCallbacks.add(callback);
    }

    /** Register an RatingCallback
     */
    public void addRatingCallback(RatingCallback callback) {
        ratingCallbacks.add(callback);
    }


}
