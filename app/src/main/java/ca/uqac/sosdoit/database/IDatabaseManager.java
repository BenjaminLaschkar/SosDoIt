package ca.uqac.sosdoit.database;

import java.util.List;

import ca.uqac.sosdoit.data.Address;
import ca.uqac.sosdoit.data.Advert;
import ca.uqac.sosdoit.data.Qualification;
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
    void addUser(String idAccount, String firstname, String lastname, String pseudo);

    /** Edit the address of an user
     */
    void editAddressUser(String idAccount, Address address);

    /** Edit the worker profile of the User
     * Add the user if not found in the database
     */
    void EditWorkerProfileUser(String idAccount, boolean isWorker, List<Qualification> qualifications);

    /** Edit the address of an user
     * Add an user if he is not in the database.
     * WARNING ! In the case of add, onUserAdded is called instead of onUserChanged
     */
    void editUser(String oldIdAccount, User newUser);

    /** Remove an user
     * Do nothing if the user is not in the database (in this case, onUserRemoved is not called)
     */
    void removeUser(String idAccount);

    /** Get an user with UserResult
     * This method search the user in the database and call the UserResult when the user is found
     * WARNING ! If the user is not found, the method call UserResult with null ( call(null) )
     */
    void getUser(String idUser, final UserResult result);

    /** Add an advert in the database
     * Create a new unique ID when added, as key in the database.
     * Update the information of the advert if he is already in the database.
     * WARNING ! In the case of update, onAdvertChanged is called instead of onAdvertAdded
V     */
    void addAdvert(Advert advert);

    /** Edit the information of an advert
     * Add an advert if he is not in the database.
     * WARNING ! In the case of add, onAdvertAdded is called instead of onAdvertChanged
     */
    void editAdvert(String oldIdAdvert, Advert advert);

    /** Remove an advert
     * Do nothing if the advert is not in the database (in this case, onAdvertRemoved is not called)
     */
    void removeAdvert(String idAdvert);

    /** Get an advert
     */
    void getAdvert(String idAdvert, final AdvertResult result);

    /** Get all the adverts published by an advertiser
     */
    void getAllAdvertsPublished(String idAdvertiser, final AdvertListResult result);

    /** Get all the adverts published by an advertiser and chosen by a worker
     */
    void getAllAdvertsChosen(String idAdvertiser, final AdvertListResult result);

    /** Get all the adverts accepted by a worker
     */
    void getAllAdvertsAccepted(String idWorker, final AdvertListResult result);

    /** Get all the advertsFinished by a worker
     */
    void getAllAdvertsFinished(String idAdvertiser, final AdvertListResult result);


    /** Add a rating
     */
    void addRating(Rating rating);

    /** Edit the information of a rating
     */
    void editRating(String olsIdRating, Rating rating);

    /** Remove an advert
     */
    void removeRating(String idRating);

    /** Get an advert
     */
    void getRating(String idRating, final RatingResult result);

    /** Get all the ratings of an user
     */
    void getUserRatings(String idUser, final RatingListResult result);

    /** Get all the ratings given by a user
     */
    void getGivenUserRating(String idUser, final  RatingListResult result);

    /** Register an UserCallback
     * The callback will be noticed when a user is respectively added, modified or removed
     */
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
