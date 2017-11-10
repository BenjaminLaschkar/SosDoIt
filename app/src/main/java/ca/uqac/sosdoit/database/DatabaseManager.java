package ca.uqac.sosdoit.database;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ca.uqac.sosdoit.data.Address;
import ca.uqac.sosdoit.data.Advert;
import ca.uqac.sosdoit.data.AdvertFilter;
import ca.uqac.sosdoit.data.AdvertStatus;
import ca.uqac.sosdoit.data.Qualification;
import ca.uqac.sosdoit.data.Rating;
import ca.uqac.sosdoit.data.User;
import ca.uqac.sosdoit.util.Util;

/**
 * The databaseManager can interact with the data in the database : add, modify, retrieve or remove data.
 */

public class DatabaseManager implements IDatabaseManager {

    // Paths of sections in the database :
    private static String USERS = "users";
    private static String ADVERTS = "adverts";
    private static String RATINGS = "ratings";

    // References:
    private DatabaseReference usersRef;
    private DatabaseReference advertsRefs;
    private DatabaseReference ratingsRefs;

    // DatabaseManager instance :
    private static DatabaseManager INSTANCE = new DatabaseManager();

    // Callbacks :
    private List<UserCallback> userCallbacks;
    private List<AdvertCallback> advertCallbacks;
    private List<RatingCallback> ratingCallbacks;

    /** Get the instance of the database manager
     */
    public static DatabaseManager getInstance() {
        return INSTANCE;
    }

    private DatabaseManager() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
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
     * Update the information of the user if he is already in the database.
     * WARNING ! In the case of update, onUserChanged is called instead of onUserAdded
     */
    @Override
    public void addUser(User user) {
        String idUser = user.getIdAccount();
        usersRef.child(idUser).setValue(user);
    }

    /** Add an user only with his id, his firstname, his lastname and his pseudo
     * Update the information of the user if he is already in the database.
     * WARNING ! In the case of update, onUserChanged is called instead of onUserAdded
     */
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

    /** Edit the worker profile of the User
     * Add the user if not found in the database
     */
    @Override
    public void EditWorkerProfileUser(String idAccount, boolean isWorker, List<Qualification> qualifications) {
        usersRef.child(idAccount).child("isWorker").setValue(true);
        usersRef.child(idAccount).child("qualifications").setValue(qualifications);
    }

    /** Edit the address of an user
     * Add an user if he is not in the database.
     * WARNING ! In the case of add, onUserAdded is called instead of onUserChanged
     */
    @Override
    public void editUser(String oldIdAccount, User newUser) {
        usersRef.child(oldIdAccount).setValue(newUser);
    }


    /** Remove an user
     * Do nothing if the user is not in the database (in this case, onUserRemoved is not called)
     */
    @Override
    public void removeUser(String idAccount) {
        usersRef.child(idAccount).removeValue();
    }

    /** Get an user with UserResult
     * This method search the user in the database and call the UserResult when the user is found
     * WARNING ! If the user is not found, the method call UserResult with null ( call(null) )
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

    /** Add an advert in the database
     * Create a new unique ID when added, as key in the database.
     */
    @Override
    public void addAdvert(Advert advert) {
        // Date the advert, if not did before
        if (advert.getCreationDate() == null) {
            advert.setCreationDate(new Date());
        }
        advertsRefs.push().setValue(advert);
    }

    /** Edit the information of an advert
     * Add an advert if he is not in the database.
     * WARNING ! In the case of add, onAdvertAdded is called instead of onAdvertChanged
     */
    @Override
    public void editAdvert(String oldIdAdvert, Advert advert) {
        // Date the advert, if not did before
        if (advert.getCreationDate() == null) {
            advert.setCreationDate(new Date());
        }
        advertsRefs.child(oldIdAdvert).setValue(advert);
    }

    /** Remove an advert
     * Do nothing if the advert is not in the database (in this case, onAdvertRemoved is not called)
     */
    @Override
    public void removeAdvert(String idAdvert) {
        advertsRefs.child(idAdvert).removeValue();
    }

    /** Get an Advert with AdvertResult
     * This method search the advert in the database and call the AdvertResult when the advert is found
     * WARNING ! If the advert is not found, the method call UserResult with null ( call(null) )
     */
    @Override
    public void getAdvert(final String idAdvert, final AdvertResult result) {
        Query query = advertsRefs.child(idAdvert);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Advert advert = dataSnapshot.getValue(Advert.class);
                if (advert != null) {
                    advert.setIdAdvert(idAdvert);
                }
                result.call(advert);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    /** Get all the adverts in the database
     * WARNING ! May produce lag and surcharge memory
     */
    @Override
    public void getAllAdverts(final AdvertListResult result) {
        Query query = advertsRefs;
        this.getAdvertsWithQuery(query, result, false, null);
    }

    /** Get adverts available in the database, with filtering parameters.
     * It use the current location of the user, if this information is unavailable, use null as currentLocation
     */
    @Override
    public void getAdvertsAvailableWithFilter(final AdvertListResult result, final AdvertFilter filter, final LatLng currentLocation) {
        Query query = advertsRefs.orderByChild("status").equalTo(AdvertStatus.AVAILABLE.name());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Advert> adverts = new ArrayList<>();

                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    Advert advert = data.getValue(Advert.class);
                    if (advert != null && isAdvertCompatibleWithFilter(advert, filter, currentLocation)) {
                        advert.setIdAdvert(data.getKey());
                        adverts.add(advert);
                    }
                }
                result.call(adverts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw  databaseError.toException();
            }
        });
    }

    /** Determine if an advert is compatible with the filter
     */
    private boolean isAdvertCompatibleWithFilter(Advert advert, AdvertFilter filter, LatLng currentLocation) {
        // First filter tasks
        if (filter.getTasks() != null && !filter.getTasks().contains(advert.getTask())) {
            return false;
        }
        // Filter price
        if ((filter.getMinPrice() != -1 && filter.getMinPrice() > advert.getPrice()) || (filter.getMaxPrice() != -1 && filter.getMaxPrice() < advert.getPrice())) {
            return  false;
        }
        // Filter distance
        return currentLocation == null || advert.getWorkAddress().getLatLng() == null || filter.getDistanceMax() == -1 || Util.distanceBetweenTowLocation(currentLocation, advert.getWorkAddress().getLatLng()) <= filter.getDistanceMax();

    }

    /** Get all the adverts available, i.e. not chose or finished by a worker
     * WARNING ! May produce lag and surcharge memory
     */
    public void getAllAdvertsAvailable(AdvertListResult result) {
        Query query = advertsRefs.orderByChild("status").equalTo(AdvertStatus.AVAILABLE.name());
        this.getAdvertsWithQuery(query, result, false, null);
    }

    /** Get all the adverts published by an advertiser
     * This method search the adverts in the database and call the AdvertListResult once all the adverts are found
     */
    @Override
    public void getAllAdvertsPublished(String idAdvertiser, final AdvertListResult result) {
        Query query = advertsRefs.orderByChild("idAdvertiser").equalTo(idAdvertiser);
        this.getAdvertsWithQuery(query, result, false, null);
    }

    /** Get all the adverts available published by an advertiser, i.e. not chose or finished by a worker
     * This method search the adverts in the database and call the AdvertListResult once all the adverts are found
     */
    @Override
    public void getAllAdvertsPublishedAvailable(String idAdvertiser, AdvertListResult result) {
        Query query = advertsRefs.orderByChild("idAdvertiser").equalTo(idAdvertiser);
        this.getAdvertsWithQuery(query, result, true, AdvertStatus.AVAILABLE);
    }

    /** Get all the adverts available published by an advertiser, i.e. not chose or finished by a worker
     * This method search the adverts in the database and call the AdvertListResult once all the adverts are found
     */
    @Override
    public void getAllAdvertsChosen(String idAdvertiser, final AdvertListResult result) {
        Query query = advertsRefs.orderByChild("idAdvertiser").equalTo(idAdvertiser);
        this.getAdvertsWithQuery(query, result, true, AdvertStatus.CHOSEN);
    }

    /** Get all the adverts accepted by a worker, accepted and not finished yet
     * This method search the adverts in the database and call the AdvertListResult once all the adverts are found
     */
    @Override
    public void getAllAdvertsAccepted(String idWorker, final AdvertListResult result) {
        Query query = advertsRefs.orderByChild("idWorker").equalTo(idWorker);
        this.getAdvertsWithQuery(query, result, true, AdvertStatus.ACCEPTED);
    }

    /** Get all the advertsFinished by a worker
     * This method search the adverts in the database and call the AdvertListResult once all the adverts are found
     */
    @Override
    public void getAllAdvertsFinished(String idAdvertiser, final AdvertListResult result) {
        Query query = advertsRefs.orderByChild("idAdvertiser").equalTo(idAdvertiser);
        this.getAdvertsWithQuery(query, result, true, AdvertStatus.FINISHED);
    }

    /** Private method to filter the adverts
     */
    private void getAdvertsWithQuery(Query query, final AdvertListResult result, final boolean filterStatus, final AdvertStatus status) {
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Advert> adverts = new ArrayList<>();

                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    Advert advert = data.getValue(Advert.class);
                    if (advert != null && (!filterStatus ||advert.getStatus().equals(status))) {
                        advert.setIdAdvert(data.getKey());
                        adverts.add(advert);
                    }
                }
                result.call(adverts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw  databaseError.toException();
            }
        });
    }

    /** Add an rating in the database
     * Create a new unique ID when added, as key in the database.
     */
    @Override
    public void addRating(Rating rating) {
        // Date the rating, if not did before
        if (rating.getDate() == null) {
            rating.setDate(new Date());
        }
        ratingsRefs.push().setValue(rating);
    }

    /** Edit the information of an rating
     * Add an rating if he is not in the database.
     * WARNING ! In the case of add, onRatingAdded is called instead of onRatingChanged
     */
    @Override
    public void editRating(String oldIdRating, Rating rating) {
        // Date the rating, if not did before
        if (rating.getDate() == null) {
            rating.setDate(new Date());
        }
        ratingsRefs.child(oldIdRating).setValue(rating);
    }

    /** Remove an rating
     * Do nothing if the rating is not in the database (in this case, onRatingRemoved is not called)
     */
    @Override
    public void removeRating(String idRating) {
        ratingsRefs.child(idRating).removeValue();
    }

    /** Get an Rating with AdvertResult
     * This method search the rating in the database and call the RatingResult when the rating is found
     * WARNING ! If the rating is not found, the method call RatingResult with null ( call(null) )
     */
    @Override
    public void getRating(final String idRating, final RatingResult result) {
        Query query = ratingsRefs.child(idRating);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Rating rating = dataSnapshot.getValue(Rating.class);
                if (rating != null) {
                    rating.setIdRating(idRating);
                }
                result.call(rating);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    /** Get all the ratings of an user, i.e. all the rating given to an user
     * This method search the adverts in the database and call the AdvertListResult once all the adverts are found
     */
    @Override
    public void getUserRatings(String idUserRated,final RatingListResult result) {
        Query query = ratingsRefs.orderByChild("idUserRated").equalTo(idUserRated);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Rating> ratings = new ArrayList<>();

                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    Rating rating = data.getValue(Rating.class);
                    if (rating != null) {
                        rating.setIdRating(data.getKey());
                        ratings.add(rating);
                    }
                }
                result.call(ratings);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw  databaseError.toException();
            }
        });
    }

    /** Get all the ratings given by a user
     * This method search the adverts in the database and call the AdvertListResult once all the adverts are found
     */
    @Override
    public void getGivenUserRating(String idGiver, final RatingListResult result) {
        Query query = ratingsRefs.orderByChild("idGiver").equalTo(idGiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Rating> ratings = new ArrayList<>();

                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    Rating rating = data.getValue(Rating.class);
                    if (rating != null) {
                        rating.setIdRating(data.getKey());
                        ratings.add(rating);
                    }
                }
                result.call(ratings);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw  databaseError.toException();
            }
        });
    }

    /** Register an UserCallback
     * The callback will be noticed when a user is respectively added, modified or removed
     * */
    public void addUserCallback(UserCallback callback) {
        userCallbacks.add(callback);
    }

    /** Register an AdvertCallback
     * The callback will be noticed when a advert is respectively added, modified or removed
     */
    public void addAdvertCallback(AdvertCallback callback) {
        advertCallbacks.add(callback);
    }

    /** Register an RatingCallback
     * The callback will be noticed when a rating is respectively added, modified or removed
     */
    public void addRatingCallback(RatingCallback callback) {
        ratingCallbacks.add(callback);
    }


}
