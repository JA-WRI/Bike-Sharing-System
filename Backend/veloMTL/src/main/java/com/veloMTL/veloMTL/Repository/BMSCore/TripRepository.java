package com.veloMTL.veloMTL.Repository.BMSCore;
import com.veloMTL.veloMTL.Model.BMSCore.Trip;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

@Repository
public interface TripRepository extends MongoRepository<Trip,String> {
    @Query("{ 'bike.$id': ?0, 'userEmail': ?1, $or: [ { 'endTime': null }, { 'endTime': { $exists: false } } ] }")
    Trip findOngoingTrip(String bikeId, String userEmail);

    @Query("{ 'bike.$id': ?0, 'userEmail': ?1, $or: [ { 'endTime': null }, { 'endTime': { $exists: false } } ] }")
    Trip findOngoingTripByRider(String bikeId, String userEmail);

    @Query("{ 'bike.$id': ?0, 'userEmail': ?1, $or: [ { 'endTime': null }, { 'endTime': { $exists: false } } ] }")
    Trip findOngoingTripByOperator(String bikeId, String userEmail);

    @Query("{ 'userEmail': ?0 }")
    List<Trip> fetchTripsByUserId(String userEmail);
}
