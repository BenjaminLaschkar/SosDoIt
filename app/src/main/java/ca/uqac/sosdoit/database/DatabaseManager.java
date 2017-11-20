package ca.uqac.sosdoit.database;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ca.uqac.sosdoit.data.User;
import ca.uqac.sosdoit.data.UserProfile;

/**
 * The DatabaseManager interacts with the data in the database: set (add, update), retrieve or remove data
 */

public class DatabaseManager // implements IDatabaseManager
{
    // Database nodes
    private final static String USERS = "users";
    private final static String USERNAMES = "usernames";
    private final static String USER_PROFILE = "profile";

    //private final static String ADVERTS = "adverts";
    //private final static String RATINGS = "ratings";

    private Reference ref;

    private static DatabaseManager INSTANCE = new DatabaseManager();

    private DatabaseManager()
    {
        ref = new Reference();
    }

    public static DatabaseManager getInstance()
    {
        return INSTANCE;
    }

    // ----- USERS ----- //

    public void isUsernameUnique(final String username, final Result<Void> result)
    {
        ref.usernames.orderByKey().equalTo(username).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.getValue() == null) {
                    result.onSuccess(null);
                } else {
                    result.onFailure();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                result.onError(databaseError);
            }
        });
    }

    public Task<Void> setUsername(final String username, final String uid)
    {
        return ref.usernames.child(username).setValue(uid);
    }

    public void setUsername(final String username, final String uid, final OnCompleteListener<Void> listener)
    {
        setUsername(username, uid).addOnCompleteListener(listener);
    }

    public Task<Void> removeUsername(final String username)
    {
        return ref.usernames.child(username).removeValue();
    }

    public void removeUsername(final String username, final OnCompleteListener<Void> listener)
    {
        removeUsername(username).addOnCompleteListener(listener);
    }

    public void getUserProfile(final String uid, final Result<UserProfile> result)
    {
        ref.users.child(uid).child(USER_PROFILE).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                UserProfile profile = dataSnapshot.getValue(UserProfile.class);
                if (profile != null) {
                    result.onSuccess(profile);
                } else {
                    result.onFailure();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                result.onError(databaseError);
            }
        });
    }

    public void getUser(final String uid, final Result<User> result)
    {
        ref.users.child(uid).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    result.onSuccess(user.setUid(uid));
                } else {
                    result.onFailure();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                result.onError(databaseError);
            }
        });
    }

    public Task<Void> setUser(final User user)
    {
        return ref.users.child(user.getUid()).setValue(user);
    }

    public void setUser(final User user, final OnCompleteListener<Void> listener)
    {
        setUser(user).addOnCompleteListener(listener);
    }

    public void addUserEventListener(final String uid, final ResultListener<User> result)
    {
        if (result.listener == null) {
            result.listener = new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        result.onSuccess(user.setUid(uid));
                    } else {
                        result.onFailure();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {
                    result.onError(databaseError);
                }
            };
        }

        if (!result.added) {
            ref.users.child(uid).addValueEventListener(result.listener);
            result.added = true;
        }
    }

    public void removeUserEventListener(final String uid, final ResultListener<User> result)
    {
        ref.users.child(uid).removeEventListener(result.listener);
        result.added = false;
    }

    // ----- ADVERTS ----- //

    private class Reference
    {
        public DatabaseReference database;
        public DatabaseReference users;
        public DatabaseReference usernames;

        Reference()
        {
            database = FirebaseDatabase.getInstance().getReference();
            users = database.child(USERS);
            usernames = database.child(USERNAMES);
        }
    }

    public static abstract class Result<T>
    {
        public void onSuccess() {};

        public abstract void onSuccess(T t);

        public void onFailure()
        {
            onError(DatabaseError.fromException(new Exception("Data not found")));
        }

        public void onError(DatabaseError error)
        {
            Log.d("DATABASE ERROR", error.getMessage());
        };
    }

    public static abstract class ResultListener<T> extends Result<T>
    {
        private boolean added;
        private ValueEventListener listener;

        public ResultListener<T> setListener(ValueEventListener listener)
        {
            this.listener = listener;
            return this;
        }
    }





//    // Paths of sections in the database :
//    //private static String USERS = "users";
//    private static String USER_RATINGS_LINKS = "users-ratings-links";
//    // References:
//    private DatabaseReference usersRef;
//    private DatabaseReference advertsRefs;
//    private DatabaseReference ratingsRefs;
//    private DatabaseReference userRatingLinksRef;
//    // Callbacks :
//    private List<UserCallback> userCallbacks;
//    private List<AdvertCallback> advertCallbacks;
//    private List<RatingCallback> ratingCallbacks;
//
//    // Map of listeners :
//    private Map<String, ValueEventListener> userListenerMap;
//    private Map<String, ValueEventListener> advertListenerMap;
//    private Map<String, ValueEventListener> ratingListenerMap;
//
//    private DatabaseManager(String s)
//    {
//        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
//        usersRef = database.child(USERS);
//        advertsRefs = database.child(ADVERTS);
//        ratingsRefs = database.child(RATINGS);
//        userRatingLinksRef = database.child(USER_RATINGS_LINKS);
//
//        userCallbacks = new ArrayList<>();
//        advertCallbacks = new ArrayList<>();
//        ratingCallbacks = new ArrayList<>();
//
//        userListenerMap = new HashMap<>();
//        advertListenerMap = new HashMap<>();
//        ratingListenerMap = new HashMap<>();
//
//        // Add the listeners for callback :
//    usersRef.addChildEventListener(new ChildEventListener()
//        {
//
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s)
//            {
//
//                if (!userCallbacks.isEmpty()) {
//                    User user = dataSnapshot.getValue(User.class);
//                    if (user != null) {
//                        user.setUid(dataSnapshot.getKey());
//                    }
//                    for (UserCallback callback : userCallbacks) {
//                        callback.onUserAdded(user);
//                    }
//                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s)
//            {
//
//                if (!userCallbacks.isEmpty()) {
//                    User user = dataSnapshot.getValue(User.class);
//                    if (user != null) {
//                        user.setUid(dataSnapshot.getKey());
//                    }
//                    for (UserCallback callback : userCallbacks) {
//                        callback.onUserChanged(user);
//                    }
//                }
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot)
//            {
//
//                if (!userCallbacks.isEmpty()) {
//                    User user = dataSnapshot.getValue(User.class);
//                    if (user != null) {
//                        user.setUid(dataSnapshot.getKey());
//                    }
//                    for (UserCallback callback : userCallbacks) {
//                        callback.onUserRemoved(user);
//                    }
//                }
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {}
//        });
//
//        advertsRefs.addChildEventListener(new ChildEventListener()
//        {
//
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s)
//            {
//
//                if (!advertCallbacks.isEmpty()) {
//                    Advert advert = dataSnapshot.getValue(Advert.class);
//                    for (AdvertCallback callback : advertCallbacks) {
//                        callback.onAdvertAdded(advert);
//                    }
//                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s)
//            {
//
//                if (!advertCallbacks.isEmpty()) {
//                    Advert advert = dataSnapshot.getValue(Advert.class);
//                    for (AdvertCallback callback : advertCallbacks) {
//                        callback.onAdvertChanged(advert);
//                    }
//                }
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot)
//            {
//
//                if (!advertCallbacks.isEmpty()) {
//                    Advert advert = dataSnapshot.getValue(Advert.class);
//                    for (AdvertCallback callback : advertCallbacks) {
//                        callback.onAdvertRemoved(advert);
//                    }
//                }
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {}
//        });
//
//        ratingsRefs.addChildEventListener(new ChildEventListener()
//        {
//
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s)
//            {
//
//                if (!ratingCallbacks.isEmpty()) {
//                    Rating rating = dataSnapshot.getValue(Rating.class);
//                    for (RatingCallback callback : ratingCallbacks) {
//                        callback.onRatingAdded(rating);
//                    }
//                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s)
//            {
//
//                if (!ratingCallbacks.isEmpty()) {
//                    Rating rating = dataSnapshot.getValue(Rating.class);
//                    for (RatingCallback callback : ratingCallbacks) {
//                        callback.onRatingChanged(rating);
//                    }
//                }
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot)
//            {
//
//                if (!ratingCallbacks.isEmpty()) {
//                    Rating rating = dataSnapshot.getValue(Rating.class);
//                    for (RatingCallback callback : ratingCallbacks) {
//                        callback.onRatingRemoved(rating);
//                    }
//                }
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {}
//        });
//    }
//
//    /**
//     * Add an user in the database
//     * Update the information of the user if he is already in the database.
//     * WARNING ! In the case of update, onUserChanged is called instead of onUserAdded
//     */
//    @Override
//    public void addUser(User user)
//    {
//        String uid = user.getUid();
//        usersRef.child(uid).setValue(user);
//    }
//
//    /**
//     * Add an user only with his id, his firstname, his lastname and his pseudo
//     * Update the information of the user if he is already in the database.
//     * WARNING ! In the case of update, onUserChanged is called instead of onUserAdded
//     */
//    @Override
//    public void addUser(String uid, String username, String firstName, String lastName)
//    {
//        //User user = new User(uid, username, firstName, lastName);
//        //addUser(user);
//    }
//
//    /**
//     * Edit the address of an user
//     */
//    @Override
//    public void editAddressUser(String uid, Address address)
//    {
//        usersRef.child(uid).child("address").setValue(address);
//    }
//
//    /**
//     * Edit the worker profile of the User
//     * Add the user if not found in the database
//     */
//    @Override
//    public void EditWorkerProfileUser(String uid, boolean isWorker, List<Skill> skills)
//    {
//        usersRef.child(uid).child("isWorker").setValue(true);
//        usersRef.child(uid).child("skills").setValue(skills);
//    }
//
//    /**
//     * Edit the address of an user
//     * Add an user if he is not in the database.
//     * WARNING ! In the case of add, onUserAdded is called instead of onUserChanged
//     */
//    @Override
//    public void editUser(String uid, User newUser)
//    {
//        usersRef.child(uid).setValue(newUser);
//    }
//
//    /**
//     * Remove an user
//     * Do nothing if the user is not in the database (in this case, onUserRemoved is not called)
//     */
//    @Override
//    public void removeUser(String uid)
//    {
//        usersRef.child(uid).removeValue();
//    }
//
//    /**
//     * Get an user with UserResult
//     * This method search the user in the database and showProfile the UserResult when the user is found
//     * WARNING ! If the user is not found, the method showProfile UserResult with null ( showProfile(null) )
//     */
//    @Override
//    public void getUser(final String uid, final UserResult result)
//    {
//        Query query = usersRef.child(uid);
//        query.addValueEventListener(new AutoRemovedValueEventListener(query)
//        {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot)
//            {
//                super.onDataChange(dataSnapshot);
//                User user = dataSnapshot.getValue(User.class);
//                if (user != null) {
//                    user.setUid(uid);
//                }
//                result.showProfile(user);
//            }
//        });
//    }
//
//    /**
//     * Get an user with UserResult
//     * UserResult is not showProfile once, but each time the user is modified
//     * This method search the user in the database and showProfile the UserResult when the user is found
//     * WARNING ! If the user is not found, the method showProfile UserResult with null ( showProfile(null) )
//     */
//    @Override
//    public void AddUserListener(final String uid, final UserResult result)
//    {
//        Query query = usersRef.child(uid);
//        ValueEventListener listener = new ValueEventListener()
//        {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot)
//            {
//                User user = dataSnapshot.getValue(User.class);
//                if (user != null) {
//                    user.setUid(uid);
//                }
//                result.showProfile(user);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError)
//            {
//                throw databaseError.toException();
//            }
//        };
//        if (!userListenerMap.containsKey(uid)) {
//            userListenerMap.put(uid, listener);
//        }
//        query.addValueEventListener(listener);
//    }
//
//    /**
//     * Remove a listener for a specific uid
//     */
//    @Override
//    public void removeUserListener(String uid)
//    {
//        if (userListenerMap.containsKey(uid)) {
//            ValueEventListener listener = userListenerMap.get(uid);
//            usersRef.child(uid).removeEventListener(listener);
//            userListenerMap.remove(uid);
//        }
//    }
//
//    /**
//     * Add an advert in the database
//     * Create a new unique ID when added, as key in the database.
//     */
//    @Override
//    public void addAdvert(Advert advert)
//    {
//        // Date the advert, if not did before
//        if (advert.getPostingDate() == null) {
//            advert.setPostingDate(new Date());
//        }
//        advertsRefs.push().setValue(advert);
//    }
//
//    /**
//     * Edit the information of an advert
//     * Add an advert if he is not in the database.
//     * WARNING ! In the case of add, onAdvertAdded is called instead of onAdvertChanged
//     */
//    @Override
//    public void editAdvert(String aid, Advert advert)
//    {
//        // Date the advert, if not did before
//        if (advert.getPostingDate() == null) {
//            advert.setPostingDate(new Date());
//        }
//        advertsRefs.child(aid).setValue(advert);
//    }
//
//    /**
//     * Remove an advert
//     * Do nothing if the advert is not in the database (in this case, onAdvertRemoved is not called)
//     */
//    @Override
//    public void removeAdvert(String aid)
//    {
//        advertsRefs.child(aid).removeValue();
//    }
//
//    /**
//     * Get an Advert with AdvertResult
//     * This method search the advert in the database and showProfile the AdvertResult when the advert is found
//     * WARNING ! If the advert is not found, the method showProfile UserResult with null ( showProfile(null) )
//     */
//    @Override
//    public void getAdvert(final String aid, final AdvertResult result)
//    {
//        Query query = advertsRefs.child(aid);
//        query.addValueEventListener(new AutoRemovedValueEventListener(query)
//        {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot)
//            {
//                super.onDataChange(dataSnapshot);
//                Advert advert = dataSnapshot.getValue(Advert.class);
//                if (advert != null) {
//                    advert.setAid(aid);
//                }
//                result.showProfile(advert);
//            }
//        });
//    }
//
//    /**
//     * Get all the adverts in the database
//     * WARNING ! May produce lag and surcharge memory
//     */
//    @Override
//    public void getAllAdverts(final AdvertListResult result)
//    {
//        Query query = advertsRefs;
//        this.getAdvertsWithQuery(query, result, false, null);
//    }
//
//    /**
//     * Get adverts available in the database, with filtering parameters.
//     * It use the current location of the user, if this information is unavailable, use null as currentLocation
//     */
//    @Override
//    public void getAdvertsAvailableWithFilter(final AdvertFilter filter, final Coordinates currentLocation, final AdvertListResult result)
//    {
//        Query query = advertsRefs.orderByChild("status").equalTo(Advert.Status.AVAILABLE.name());
//        query.addValueEventListener(new AutoRemovedValueEventListener(query)
//        {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot)
//            {
//                super.onDataChange(dataSnapshot);
//                ArrayList<Advert> adverts = new ArrayList<>();
//
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    Advert advert = data.getValue(Advert.class);
//                    if (advert != null && isAdvertCompatibleWithFilter(advert, filter, currentLocation)) {
//                        advert.setAid(data.getKey());
//                        adverts.add(advert);
//                    }
//                }
//                result.showProfile(adverts);
//            }
//        });
//    }
//
//    /**
//     * Determine if an advert is compatible with the filter
//     */
//    private boolean isAdvertCompatibleWithFilter(Advert advert, AdvertFilter filter, Coordinates currentLocation)
//    {
//        // First filter tasks
//        if (filter.hasFilterOnTasks() && !filter.getTags().contains(advert.getTag())) {
//            return false;
//        }
//        // Filter price
//        if ((filter.hasFilterOnMinPrice() && filter.getMinPrice() > advert.getBudget()) || (filter.hasFilterOnMaxPrice() && filter.getMaxPrice() < advert.getBudget())) {
//            return false;
//        }
//        // Filter distance
//        return currentLocation == null || advert.getAddress().getCoordinates() == null || !filter.hasFilterOnDistanceMax() || Util.distanceBetweenTwoLocation(currentLocation, advert.getAddress().getCoordinates()) <= filter.getDistanceMax();
//    }
//
//    /**
//     * Get all the adverts available, i.e. not chose or finished by a worker
//     * WARNING ! May produce lag and surcharge memory
//     */
//    public void getAllAdvertsAvailable(AdvertListResult result)
//    {
//        Query query = advertsRefs.orderByChild("status").equalTo(Advert.Status.AVAILABLE.name());
//        this.getAdvertsWithQuery(query, result, false, null);
//    }
//
//    /**
//     * Get all the adverts published by an advertiser
//     * This method search the adverts in the database and showProfile the AdvertListResult once all the adverts are found
//     */
//    @Override
//    public void getAllAdvertsPublished(String uidAdvertiser, final AdvertListResult result)
//    {
//        Query query = advertsRefs.orderByChild("idAdvertiser").equalTo(uidAdvertiser);
//        this.getAdvertsWithQuery(query, result, false, null);
//    }
//
//    /**
//     * Get all the adverts available published by an advertiser, i.e. not chose or finished by a worker
//     * This method search the adverts in the database and showProfile the AdvertListResult once all the adverts are found
//     */
//    @Override
//    public void getAllAdvertsPublishedAvailable(String idAdvertiser, AdvertListResult result)
//    {
//        Query query = advertsRefs.orderByChild("idAdvertiser").equalTo(idAdvertiser);
//        this.getAdvertsWithQuery(query, result, true, Advert.Status.AVAILABLE);
//    }
//
//    /**
//     * Get all the adverts accepted by a worker, accepted and not finished yet
//     * This method search the adverts in the database and showProfile the AdvertListResult once all the adverts are found
//     */
//    @Override
//    public void getAllAdvertsAccepted(String idWorker, final AdvertListResult result)
//    {
//        Query query = advertsRefs.orderByChild("idWorker").equalTo(idWorker);
//        this.getAdvertsWithQuery(query, result, true, Advert.Status.ACCEPTED);
//    }
//
//    /**
//     * Get all the advertsFinished by a worker
//     * This method search the adverts in the database and showProfile the AdvertListResult once all the adverts are found
//     */
//    @Override
//    public void getAllAdvertsCompleted(String idAdvertiser, final AdvertListResult result)
//    {
//        Query query = advertsRefs.orderByChild("idAdvertiser").equalTo(idAdvertiser);
//        this.getAdvertsWithQuery(query, result, true, Advert.Status.COMPLETED);
//    }
//
//    /**
//     * Private method to filter the adverts
//     */
//    private void getAdvertsWithQuery(Query query, final AdvertListResult result, final boolean filterStatus, final Advert.Status status)
//    {
//        query.addValueEventListener(new AutoRemovedValueEventListener(query)
//        {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot)
//            {
//                super.onDataChange(dataSnapshot);
//
//                ArrayList<Advert> adverts = new ArrayList<>();
//
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    Advert advert = data.getValue(Advert.class);
//                    if (advert != null && (!filterStatus || advert.getStatus().equals(status))) {
//                        advert.setAid(data.getKey());
//                        adverts.add(advert);
//                    }
//                }
//                result.showProfile(adverts);
//            }
//        });
//    }
//
//    /**
//     * Add an rating in the database
//     * Create a new unique ID when added, as key in the database.
//     */
//    @Override
//    public void addRating(Rating rating)
//    {
//        // Date the rating, if not did before
//        if (rating.getDate() == null) {
//            rating.setDate(new Date());
//        }
//        // Add the rating TODO
////        ratingsRefs.push().setValue(rating);
////        // Add the id of the rating in the User-Rating links list
////        if (rating.getUidRated() != null) {
////            userRatingLinksRef.child(rating.getUidRated()).child(rating.getRid()).setValue(true);
////        }
//    }
//
//    /**
//     * Edit the information of an rating
//     * Add an rating if he is not in the database.
//     * WARNING ! In the case of add, onRatingAdded is called instead of onRatingChanged
//     */
//    @Override
//    public void editRating(String rid, Rating rating)
//    {
//        // Date the rating, if not did before
//        if (rating.getDate() == null) {
//            rating.setDate(new Date());
//        }
//        ratingsRefs.child(rid).setValue(rating);
//        // TODO move the ref of the user
//    }
//
//    /**
//     * Remove an rating
//     * Do nothing if the rating is not in the database (in this case, onRatingRemoved is not called)
//     */
//    @Override
//    public void removeRating(String rid)
//    {
//        // remove the rating
//        ratingsRefs.child(rid).removeValue();
//        // Remove the link
//        // TODO remove the links
//    }
//
//    /**
//     * Get an Rating with AdvertResult
//     * This method search the rating in the database and showProfile the RatingResult when the rating is found
//     * WARNING ! If the rating is not found, the method showProfile RatingResult with null ( showProfile(null) )
//     */
//    @Override
//    public void getRating(final String rid, final RatingResult result)
//    {
//        Query query = ratingsRefs.child(rid);
//        query.addValueEventListener(new AutoRemovedValueEventListener(query)
//        {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot)
//            {
//                super.onDataChange(dataSnapshot);
//
//                Rating rating = dataSnapshot.getValue(Rating.class);
//                if (rating != null) {
//                    rating.setRid(rid);
//                }
//                result.showProfile(rating);
//            }
//        });
//    }
//
//    /**
//     * Get all the ratings of an user, i.e. all the rating given to an user
//     * This method search the adverts in the database and showProfile the AdvertListResult once all the adverts are found
//     */
//    @Override
//    public void getUserRatings(String uidRated, final RatingListResult result)
//    {
////        Query query1 = userRatingLinksRef.child(uidRated);
////        query1.addValueEventListener(new AutoRemovedValueEventListener(query1) {
////            @Override
////            public void onDataChange(DataSnapshot dataSnapshot) {
////                super.onDataChange(dataSnapshot);
////
////
////            }
////        });
//        Query query = ratingsRefs.orderByChild("idUserRated").equalTo(uidRated);
//        query.addValueEventListener(new AutoRemovedValueEventListener(query)
//        {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot)
//            {
//                super.onDataChange(dataSnapshot);
//
//                ArrayList<Rating> ratings = new ArrayList<>();
//
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    Rating rating = data.getValue(Rating.class);
//                    if (rating != null) {
//                        rating.setRid(data.getKey());
//                        ratings.add(rating);
//                    }
//                }
//                result.showProfile(ratings);
//            }
//        });
//    }
//
//    /**
//     * Get all the ratings given by a user
//     * This method search the adverts in the database and showProfile the AdvertListResult once all the adverts are found
//     */
//    @Override
//    public void getGivenUserRating(String uidRater, final RatingListResult result)
//    {
//        Query query = ratingsRefs.orderByChild("idGiver").equalTo(uidRater);
//        query.addValueEventListener(new AutoRemovedValueEventListener(query)
//        {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot)
//            {
//                super.onDataChange(dataSnapshot);
//
//                ArrayList<Rating> ratings = new ArrayList<>();
//
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    Rating rating = data.getValue(Rating.class);
//                    if (rating != null) {
//                        rating.setRid(data.getKey());
//                        ratings.add(rating);
//                    }
//                }
//                result.showProfile(ratings);
//            }
//        });
//    }
//
//    /**
//     * Register an UserCallback
//     * The callback will be noticed when a user is respectively added, modified or removed
//     */
//    public void addUserCallback(UserCallback callback)
//    {
//        userCallbacks.add(callback);
//    }
//
//    /**
//     * Register an AdvertCallback
//     * The callback will be noticed when a advert is respectively added, modified or removed
//     */
//    public void addAdvertCallback(AdvertCallback callback)
//    {
//        advertCallbacks.add(callback);
//    }
//
//    /**
//     * Register an RatingCallback
//     * The callback will be noticed when a rating is respectively added, modified or removed
//     */
//    public void addRatingCallback(RatingCallback callback)
//    {
//        ratingCallbacks.add(callback);
//    }
//
//    /**
//     * A private class to auto remove the listener ValueEventListener when the method onDataChange is called
//     */
//    private abstract class AutoRemovedValueEventListener implements ValueEventListener
//    {
//
//        private Query query;
//
//        AutoRemovedValueEventListener(Query query)
//        {
//            this.query = query;
//        }
//
//        /**
//         * Remove the listener from the query
//         */
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot)
//        {
//            query.removeEventListener(this);
//            query = null;
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError)
//        {
//            Log.d("SOSDOIT-ERROR", databaseError.getMessage());
//            //throw databaseError.toException();
//        }
//    }
}
