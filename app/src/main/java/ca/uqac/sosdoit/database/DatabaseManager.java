package ca.uqac.sosdoit.database;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

import ca.uqac.sosdoit.data.Advert;
import ca.uqac.sosdoit.data.Bid;
import ca.uqac.sosdoit.data.Rating;
import ca.uqac.sosdoit.data.User;
import ca.uqac.sosdoit.data.UserProfile;

/**
 * The DatabaseManager interacts with the data in the database: set (add, update), retrieve or remove data
 */

public class DatabaseManager // implements IDatabaseManager
{
    private final static String USERS = "users";
    private final static String USERNAMES = "usernames";
    private final static String ADVERTS = "adverts";
    private final static String BIDS = "bids";
    private final static String JOBS = "jobs";

    private final static String USER_PROFILE = "profile";
    private final static String ADVERT_STATUS = "status";
    private final static String ADVERT_ADVERTISER = "advertiserUid";
    private final static String ADVERT_ADVERTISER_RATING = "advertiserRating";

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

    // ----- USERNAMES ----- //

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

    public Task<Void> removeUsername(final String username)
    {
        return ref.usernames.child(username).removeValue();
    }

    // ----- USERS ----- //

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

    public void getAdvert(final String aid, final Result<Advert> result)
    {
        ref.adverts.child(aid).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Advert advert = dataSnapshot.getValue(Advert.class);
                if (advert != null) {
                    result.onSuccess(advert.setAid(aid));
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

    public Task<Void> setAdvert(final Advert advert)
    {
        if (advert.hasAid()) {
            return ref.adverts.child(advert.getAid()).setValue(advert);
        } else {
            return ref.adverts.push().setValue(advert);
        }
    }

    public void addAdvertEventListener(final String aid, final ResultListener<Advert> result)
    {
        if (result.listener == null) {
            result.listener = new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    Advert advert = dataSnapshot.getValue(Advert.class);
                    if (advert != null) {
                        result.onSuccess(advert.setAid(aid));
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
            ref.adverts.child(aid).addValueEventListener(result.listener);
            result.added = true;
        }
    }

    public void removeAdvertEventListener(final String aid, final ResultListener<Advert> result)
    {
        ref.adverts.child(aid).removeEventListener(result.listener);
        result.added = false;
    }

    public void addAdvertsEventListener(final String uid, final ResultListener<ArrayList<Advert>> result)
    {
        if (result.listener == null) {
            result.listener = new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.getValue() != null) {
                        ArrayList<Advert> list = new ArrayList<>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Advert advert = data.getValue(Advert.class);
                            if (advert != null) {
                                list.add(advert.setAid(data.getKey()));
                            }
                        }
                        result.onSuccess(list);
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
            ref.adverts.orderByChild(ADVERT_ADVERTISER).equalTo(uid).addValueEventListener(result.listener);
            result.added = true;
        }
    }

    public void addAdvertsEventListener(final Advert.Status status, final ResultListener<ArrayList<Advert>> result)
    {
        if (result.listener == null) {
            result.listener = new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.getValue() != null) {
                        ArrayList<Advert> list = new ArrayList<>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Advert advert = data.getValue(Advert.class);
                            if (advert != null) {
                                list.add(advert.setAid(data.getKey()));
                            }
                        }
                        result.onSuccess(list);
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
            ref.adverts.orderByChild(ADVERT_STATUS).equalTo(status.name()).addValueEventListener(result.listener);
            result.added = true;
        }
    }

    public void removeAdvertsEventListener(final ResultListener<ArrayList<Advert>> result)
    {
        ref.adverts.removeEventListener(result.listener);
        result.added = false;
    }

    // ----- BIDS ----- //

    public void getBid(final String advertiser_uid, final String aid, final String uid, final Result<Bid> result)
    {
        ref.bids.child(advertiser_uid).child(aid).child(uid).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Bid bid = dataSnapshot.getValue(Bid.class);
                if (bid != null) {
                    result.onSuccess(bid.setUid(dataSnapshot.getKey()));
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

    public void getBid(final Advert advert, final String uid, final Result<Advert> result)
    {
        ref.bids.child(advert.getAdvertiserUid()).child(advert.getAid()).child(uid).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Bid bid = dataSnapshot.getValue(Bid.class);
                if (bid != null) {
                    result.onSuccess(advert.setBid(bid.setUid(dataSnapshot.getKey())));
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

    public Task<Void> setBid(final String advertiser_uid, final String aid, final Bid bid)
    {
        return ref.bids.child(advertiser_uid).child(aid).child(bid.getUid()).setValue(bid);
    }

    public Task<Void> removeBid(final String advertiser_uid, final String aid, final String uid)
    {
        return ref.bids.child(advertiser_uid).child(aid).child(uid).removeValue();
    }

    public Task<Void> setBids(final String advertiser_uid, final String aid, final Map<String, Bid> bids)
    {
        return ref.bids.child(advertiser_uid).child(aid).setValue(bids);
    }

    public void addBidEventListener(final String advertiser_uid, final String aid, final String uid, final ResultListener<Bid> result)
    {
        if (result.listener == null) {
            result.listener = new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    Bid bid = dataSnapshot.getValue(Bid.class);
                    if (bid != null) {
                        result.onSuccess(bid.setUid(dataSnapshot.getKey()));
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
            ref.bids.child(advertiser_uid).child(aid).child(uid).addValueEventListener(result.listener);
            result.added = true;
        }
    }

    public void removeBidEventListener(final String advertiser_uid, final String aid, final String uid, final ResultListener<Bid> result)
    {
        ref.bids.child(advertiser_uid).child(aid).child(uid).addValueEventListener(result.listener);
        result.added = false;
    }

    public void addBidsEventListener(final String uid, final String aid, final ResultListener<ArrayList<Bid>> result)
    {
        if (result.listener == null) {
            result.listener = new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.getValue() != null) {
                        ArrayList<Bid> list = new ArrayList<>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Bid bid = data.getValue(Bid.class);
                            if (bid != null) {
                                list.add(bid.setUid(data.getKey()));
                            }
                        }
                        result.onSuccess(list);
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
            ref.bids.child(uid).child(aid).addValueEventListener(result.listener);
            result.added = true;
        }
    }

    public void removeBidsEventListener(final String uid, final String aid, final ResultListener<ArrayList<Bid>> result)
    {
        ref.bids.child(uid).child(aid).removeEventListener(result.listener);
        result.added = false;
    }

    // ----- JOBS ----- //

    public void getJobs(final ArrayList<String> aids, final String uid, final ResultSynced<Advert> result)
    {
        for (String aid : aids) {
            getAdvert(aid, new Result<Advert>()
            {
                @Override
                public void onSuccess(Advert advert)
                {
                    getBid(advert, uid, result);
                }

                @Override
                public void onFailure()
                {
                    result.onFailure();
                }
            });
        }
    }

    public Task<Void> setJob(final String uid, final String aid)
    {
        return ref.jobs.child(uid).child(aid).setValue(true);
    }

    public void addJobsEventListener(final String uid, final ResultListener<ArrayList<String>> result)
    {
        if (result.listener == null) {
            result.listener = new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.getValue() != null) {
                        ArrayList<String> list = new ArrayList<>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            list.add(data.getKey());
                        }
                        result.onSuccess(list);
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
            ref.jobs.child(uid).addValueEventListener(result.listener);
            result.added = true;
        }
    }

    public void removeJobsEventListener(final String uid, final ResultListener<ArrayList<String>> result)
    {
        ref.jobs.child(uid).removeEventListener(result.listener);
        result.added = false;
    }

    // ----- RATINGS ----- //

    public Task<Void> setAdvertiserRating(final String aid, final Rating rating)
    {
        return ref.adverts.child(aid).child(ADVERT_ADVERTISER_RATING).setValue(rating);
    }

    // ----- INNER CLASS ----- //

    private class Reference
    {
        private DatabaseReference database;
        private DatabaseReference users;
        private DatabaseReference usernames;
        private DatabaseReference adverts;
        private DatabaseReference bids;
        private DatabaseReference jobs;

        Reference()
        {
            database = FirebaseDatabase.getInstance().getReference();
            users = database.child(USERS);
            usernames = database.child(USERNAMES);
            adverts = database.child(ADVERTS);
            bids = database.child(BIDS);
            jobs = database.child(JOBS);
        }
    }

    public static abstract class Result<T>
    {
        public void onComplete() {}

        public abstract void onSuccess(T t);

        public void onFailure()
        {
            onError(DatabaseError.fromException(new Exception("Data not found")));
        }

        public void onError(DatabaseError error)
        {
            Log.d("DATABASE ERROR", error.getMessage());
        }

        ;
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

    public static abstract class ResultSynced<T> extends Result<T>
    {
        private int count;
        private int max;

        public ResultSynced(int max)
        {
            this.max = max;
        }

        public void sync()
        {
            if (++count == max) {
                onSynced();
            }

        }

        public abstract void onSynced();
    }
}



//        private DatabaseManager() {
//            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
//            usersRef = database.child(USERS);
//            advertsRefs = database.child(ADVERTS);
//            ratingsRefs = database.child(RATINGS);
//            ratingsGivenIndexRefs = database.child(RATINGS_GIVEN_INDEX);
//            advertsPublishedIndexRefs = database.child(ADVERTS_PUBLISHED_INDEX);
//
//            userCallbacks = new ArrayList<>();
//            advertCallbacks = new ArrayList<>();
//            ratingCallbacks = new ArrayList<>();
//
//            userListenerMap = new HashMap<>();
//            advertListenerMap = new HashMap<>();
//            ratingListenerMap = new HashMap<>();
//
//            // Add the listeners for callback :
//            usersRef.addChildEventListener(new ChildEventListener() {

//
//    DatabaseReference newRef = advertsRefs.push();
//        newRef.setValue(advert);
//    // Add a link in the adverts published index
//    addAdvertPublishedInIndex(advert.getAdvertiserUid(), newRef.getKey());
//}
//
//
//    /** Update the advert published index when a advert is added in the database
//     */
//    private void addAdvertPublishedInIndex(String uidAdvertiser, String aid) {
//        advertsPublishedIndexRefs.child(uidAdvertiser).child(aid).setValue("true");
//    }

//        /** Remove an advert
//         * Do nothing if the advert is not in the database (in this case, onAdvertRemoved is not called)
//         */
//        @Override
//        public void removeAdvert(final String aid) {
//            getAdvert(aid, new AdvertResult() {
//                @Override
//                public void call(Advert advert) {
//                    if (advert != null) {
//                        removeAdvertPublishedInIndex(advert.getAdvertiserUid(), aid); // remove link
//                    }
//                    advertsRefs.child(aid).removeValue(); // Remove advert
//                }
//            });
//        }
//
//        /** Update the rating index when a rating is removed in the database
//         */
//        private void removeAdvertPublishedInIndex(String uidAdvertiser, String aid) {
//            advertsPublishedIndexRefs.child(uidAdvertiser).child(aid).removeValue();
//        }
//
//
//        /** Get an Advert with AdvertResult
//         * This method search the advert in the database and call the AdvertResult when the advert is found
//         * WARNING ! If the advert is not found, the method call UserResult with null ( call(null) )
//         */
//        @Override
//        public void getAdvert(final String aid, final AdvertResult result) {
//            Query query = advertsRefs.child(aid);
//            query.addValueEventListener(new AutoRemovedValueEventListener(query) {

//
//        /** Get all the adverts published by an advertiser
//         * This method search the adverts in the database and call the AdvertListResult once all the adverts are found
//         */
//        @Override
//        public void getAllAdvertsPublished(String uidAdvertiser, final AdvertListResult result) {
//            // Get all the aid in the adverts published index
//            advertsPublishedIndexRefs.child(uidAdvertiser).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    // Get all the aid
//                    List<String> aidList = new ArrayList<>();
//                    for (DataSnapshot data: dataSnapshot.getChildren()) {
//                        aidList.add(data.getKey());
//                    }
//                    if (aidList.isEmpty()) {
//                        // No AID found : call the result with empty list :
//                        result.call(new ArrayList<Advert>());
//                    }
//                    else {
//                        // Get all the Advert from the aid
//                        int nbAdverts = aidList.size();
//                        final List<Advert> adverts = new ArrayList<>();
//                        // Create the listener to get the adverts :
//                        ValueEventListenerForIndex listener = new ValueEventListenerForIndex(nbAdverts) {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                super.onDataChange(dataSnapshot);
//                                Advert advert = dataSnapshot.getValue(Advert.class);
//                                if (advert != null) {
//                                    advert.setAid(dataSnapshot.getKey());
//                                    adverts.add(advert);
//                                }
//                                // Call the result when all the AID had been treated
//                                if (nbCallsMax == nbCalls) {
//                                    result.call(adverts);
//                                }
//                            }
//                        };
//                        // Add the listener for each aid
//                        for (final String aid : aidList) {
//                            advertsRefs.child(aid).addListenerForSingleValueEvent(listener);
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    throw databaseError.toException();
//                }
//            });
//
//        }
//
//
//    // Add the rating
//    DatabaseReference newRef = ratingsRefs.push();
//        newRef.setValue(rating);
//    // Add the id of the rating in the User-Rating2 links list
//    addRatingInIndex(rating.getUidRater(), newRef.getKey());
//}
//
//    /** Update the rating index when a rating is added in the database
//     */
//    private void addRatingInIndex(String uidRater, String rid) {
//        if (rid != null) {
//            ratingsGivenIndexRefs.child(uidRater).child(rid).setValue(true);
//        }
//    }
//        ratingsRefs.child(rid).setValue(rating);
//                }
//
//
///** Remove an rating
// * Do nothing if the rating is not in the database (in this case, onRatingRemoved is not called)
// */
//@Override
//public void removeRating(final String rid) {
//        getRating(rid, new DatabaseManager.RatingResult() {
//@Override
//public void call(Rating2 rating) {
//        if (rating != null) {
//        removeRatingInIndex(rating.getUidRater(), rid); // Remove the rating in the index
//        }
//        ratingsRefs.child(rid).removeValue(); // remove the rating
//
//        }
//        });
//        }
//
///** Update the RatingIndex when the rating is removed
// */
//private void removeRatingInIndex(String uidRater, String rid) {
//        ratingsGivenIndexRefs.child(uidRater).child(rid).removeValue();
//        }




//
//    /** Get all the ratings given by a user
//     * This method search the adverts in the database and call the AdvertListResult once all the adverts are found
//     */
//    @Override
//    public void getGivenUserRating(String uidRater, final RatingListResult result) {
//        // Get all the rid in the ratings index
//        ratingsGivenIndexRefs.child(uidRater).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Get all the rid
//                final List<String> ridList = new ArrayList<>();
//                for (DataSnapshot data: dataSnapshot.getChildren()) {
//                    ridList.add(data.getKey());
//                }
//                if (ridList.isEmpty()) {
//
//                    // No AID found : call the result with empty list :
//                    result.call(new ArrayList<Rating2>());
//                }
//                else {
//                    // Get all the Rating2 from the rid
//                    final int nbRatings = ridList.size();
//                    final List<Rating2> ratings = new ArrayList<>();
//                    // Create the listener to get the ratings :
//                    ValueEventListener listener = new ValueEventListenerForIndex(nbRatings) {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            super.onDataChange(dataSnapshot);
//                            Rating2 rating = dataSnapshot.getValue(Rating2.class);
//                            if (rating != null) {
//                                rating.setRid(dataSnapshot.getKey());
//                                ratings.add(rating);
//                            }
//                            // Call the result when all the RID had been treated
//                            if (nbCallsMax == nbCalls) {
//                                result.call(ratings);
//                            }
//                        }
//                    };
//                    // Add the listener for each rid
//                    for (final String rid : ridList) {
//                        ratingsRefs.child(rid).addListenerForSingleValueEvent(listener);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                throw databaseError.toException();
//            }
//        });
//    }
//
//
//
//    /** A private class to get data from index in the database
//     * Count the calls of onDataChange
//     * Allow to use this listener to a lot of queries and count them, to synchronized the return function
//     */
//    private abstract class ValueEventListenerForIndex implements ValueEventListener {
//
//        protected int nbCallsMax;
//        protected int nbCalls = 0;
//
//        ValueEventListenerForIndex(int nbCallsMax) {
//            this.nbCallsMax = nbCallsMax;
//        }
//
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//            nbCalls++;
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//            throw databaseError.toException();
//        }
//    }
//
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
//                    Rating2 rating = dataSnapshot.getValue(Rating2.class);
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
//                    Rating2 rating = dataSnapshot.getValue(Rating2.class);
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
//                    Rating2 rating = dataSnapshot.getValue(Rating2.class);
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
//    public void addRating(Rating2 rating)
//    {
//        // Date the rating, if not did before
//        if (rating.getDate() == null) {
//            rating.setDate(new Date());
//        }
//        // Add the rating TODO
////        ratingsRefs.push().setValue(rating);
////        // Add the id of the rating in the User-Rating2 links list
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
//    public void editRating(String rid, Rating2 rating)
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
//     * Get an Rating2 with AdvertResult
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
//                Rating2 rating = dataSnapshot.getValue(Rating2.class);
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
//                ArrayList<Rating2> ratings = new ArrayList<>();
//
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    Rating2 rating = data.getValue(Rating2.class);
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
//                ArrayList<Rating2> ratings = new ArrayList<>();
//
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    Rating2 rating = data.getValue(Rating2.class);
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
