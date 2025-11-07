package com.veloMTL.veloMTL.Repository.BMSCore;
import com.veloMTL.veloMTL.Model.BMSCore.Trip;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.Query;


@Repository
public interface TripRepository extends MongoRepository<Trip,String> {
    @Query("{ 'bike.$id': ?0, 'rider.$id': { $oid: ?1 }, $or: [ { 'endTime': null }, { 'endTime': { $exists: false } } ] }")
    Trip findOngoingTrip(String bikeId, String riderId);

}
