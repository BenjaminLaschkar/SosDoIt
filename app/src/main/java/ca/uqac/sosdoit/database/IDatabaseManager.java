package ca.uqac.sosdoit.database;

import java.util.List;

import ca.uqac.sosdoit.data.Address;
import ca.uqac.sosdoit.data.Advert;
import ca.uqac.sosdoit.data.LatitudeLongitude;
import ca.uqac.sosdoit.data.Skill;
import ca.uqac.sosdoit.data.Rating;
import ca.uqac.sosdoit.data.User;

/**
 * The databaseManager can interact with the data in the database : add, modify, retrieve or remove data.
 */

public interface IDatabaseManager {

    /** Add an user in the database
     * Update the information of the user if he is already in the database.
     * WARNING ! In the case of update, onUserChanged is called instead of onUserAdded
     */
    void addUser(User user);

    /** Add an user only with his id, his firstname, his lastname and his pseudo
     * Update the information of the user if he is already in the database.
     * WARNING ! In the case of update, onUserChanged is called instead of onUserAdded
     */
    void addUser(String uid, String firstname, String lastname, String pseudo);

    /** Edit the address of an user
     */
    void editAddressUser(String uid, Address address);

    /** Edit the worker profile of the User
     * Add the user if not found in the database
     */
    void EditWorkerProfileUser(String uid, boolean isWorker, List<Skill> skills);

    /** Edit the address of an user
     * Add an user if he is not in the database.
     * WARNING ! In the case of add, onUserAdded is called instead of onUserChanged
     */
    void editUser(String uid, User newUser);

    /** Remove an user
     * Do nothing if the user is not in the database (in this case, onUserRemoved is not called)
     */
    void removeUser(String uid);

    /** Get an user with UserResult
     * This method search the user in the database and call the UserResult when the user is found
     * WARNING ! If the user is not found, the method call UserResult with null ( call(null) )
     */
    void getUser(String uid, final UserResult result);

    /** Add an advert in the database
     * Create a new unique ID when added, as key in the database.
     */
    void addAdvert(Advert advert);

    /** Edit the information of an advert
     * Add an advert if he is not in the database.
     * WARNING ! In the case of add, onAdvertAdded is called instead of onAdvertChanged
     */
    void editAdvert(String aid, Advert advert);

    /** Remove an advert
     * Do nothing if the advert is not in the database (in this case, onAdvertRemoved is not called)
     */
    void removeAdvert(String aid);

    /** Get an Advert with AdvertResult
     * This method search the advert in the database and call the AdvertResult when the advert is found
     * WARNING ! If the advert is not found, the method call UserResult with null ( call(null) )
     */
    void getAdvert(String aid, final AdvertResult result);

    /** Get all the adverts in the database
     * WARNING ! May produce lag and surcharge memory
     */
    void getAllAdverts(final AdvertListResult result);

    /** Get adverts available in the database, with filtering parameters.
     * It use the current location of the user, if this information is unavailable, use null as currentLocation
     */
    void getAdvertsAvailableWithFilter(AdvertFilter filter, LatitudeLongitude currentLocation, final AdvertListResult result);

    /** Get all the adverts available, i.e. not chose or finished by a worker
     * WARNING ! May produce lag and surcharge memory
     */
    void getAllAdvertsAvailable(final AdvertListResult result);

    /** Get all the adverts published by an advertiser
     * This method search the adverts in the database and call the AdvertListResult once all the adverts are found
     */
    void getAllAdvertsPublished(String uidAdvertiser, final AdvertListResult result);

    /** Get all the adverts available published by an advertiser, i.e. not chose or finished by a worker
     * This method search the adverts in the database and call the AdvertListResult once all the adverts are found
     */
    void getAllAdvertsPublishedAvailable(String uidAdvertiser, final AdvertListResult result);

    /** Get all the adverts published by an advertiser and chosen by a worker, but not accepted yet
     * This method search the adverts in the database and call the AdvertListResult once all the adverts are found
     */
    void getAllAdvertsChosen(String uidAdvertiser, final AdvertListResult result);

    /** Get all the adverts accepted by a worker, accepted and not finished yet
     * This method search the adverts in the database and call the AdvertListResult once all the adverts are found
     */
    void getAllAdvertsAccepted(String uidWorker, final AdvertListResult result);

    /** Get all the advertsFinished by a worker
     * This method search the adverts in the database and call the AdvertListResult once all the adverts are found
     */
    void getAllAdvertsFinished(String uidAdvertiser, final AdvertListResult result);


    /** Add an rating in the database
     * Create a new unique ID when added, as key in the database.
     *
     */
    void addRating(Rating rating);

    /** Edit the information of an rating
     * Add an rating if he is not in the database.
     * WARNING ! In the case of add, onRatingAdded is called instead of onRatingChanged
     */
    void editRating(String rid, Rating rating);

    /** Remove an rating
     * Do nothing if the rating is not in the database (in this case, onRatingRemoved is not called)
     */
    void removeRating(String rid);

    /** Get an Rating with AdvertResult
     * This method search the rating in the database and call the RatingResult when the rating is found
     * WARNING ! If the rating is not found, the method call RatingResult with null ( call(null) )
     */
    void getRating(String rid, final RatingResult result);

    /** Get all the ratings of an user, i.e. all the rating given to an user
     * This method search the adverts in the database and call the AdvertListResult once all the adverts are found
     */
    void getUserRatings(String uidRated, final RatingListResult result);

    /** Get all the ratings given by a user
     * This method search the adverts in the database and call the AdvertListResult once all the adverts are found
     */
    void getGivenUserRating(String uidRater, final  RatingListResult result);

    /** Register an UserCallback
     * The callback will be noticed when a user is respectively added, modified or removed
     * */
    void addUserCallback(UserCallback callback);

    /** Register an AdvertCallback
     * The callback will be noticed when a advert is respectively added, modified or removed
     */
    void addAdvertCallback(AdvertCallback callback);

    /** Register an RatingCallback
     * The callback will be noticed when a rating is respectively added, modified or removed
     */
    void addRatingCallback(RatingCallback callback);


    // Results to retrieve data
    interface UserResult {
        void call(User user);
    }
    interface AdvertResult {
        void call(Advert advert);
    }
    interface AdvertListResult {
        void call(List<Advert> advertList);
    }
    interface RatingResult {
        void call(Rating rating);
    }
    interface RatingListResult {
        void call(List<Rating> ratingList);
    }










}
