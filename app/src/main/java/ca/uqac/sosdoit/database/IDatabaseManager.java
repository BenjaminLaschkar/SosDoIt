package ca.uqac.sosdoit.database;

import java.util.List;

import ca.uqac.sosdoit.data.Advert;
import ca.uqac.sosdoit.data.Rating;
import ca.uqac.sosdoit.data.User;

/**
 * The databaseManager can interact with the data in the database : add, modify, retrieve or remove data.
 */

public interface IDatabaseManager {

    /** Add an user in the database
     */
    void addUser(User user);

    /** Edit the information of an user
     */
    void editUser(String oldIdAccount, User newUser);

    /** Remove an user
     */
    void removeUser(String idAccount);

    /** Get an user
     */
    User getUser(String isUser);

    /** Add an advert in the database
     */
    void addAdvert(Advert advert);

    /** Edit the information of an advert
     */
    void editAdvert(String oldIdAdvert, Advert advert);

    /** Remove an advert
     */
    void removeAdvert(String idAdvert);

    /** Get an advert
     */
    Advert getAdvert(String idAdvert);

    /** Get all the adverts published by an advertiser
     */
    List<Advert> getAllAdvertsPublished(String idAdvertiser);

    /** Get all the adverts published by an advertiser and chosen by a worker
     */
    List<Advert> getAllAdvertsChosen(String idAdvertiser);

    /** Get all the adverts accepted by a worker
     */
    List<Advert> getAllAdvertsAccepted(String idWorker);

    /** Get all the advertsFinished by a worker
     */
    List<Advert> getAllAdvertsFinished(String idAdvertiser);


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
    Advert getRating(String idRating);


    /** Get all the ratings of an user
     */
    List<Rating> getUserRatings(String userId);


    /** Register an UserCallback
     */
    void addUserCallback(UserCallback callback);

    /** Register an AdvertCallback
     */
    void addAdvertCallback(AdvertCallback callback);

    /** Register an RatingCallback
     */
    void addRatingCallback(RatingCallback callback);









}
