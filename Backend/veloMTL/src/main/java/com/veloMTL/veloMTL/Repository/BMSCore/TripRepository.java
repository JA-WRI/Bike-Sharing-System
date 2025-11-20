package com.veloMTL.veloMTL.Repository.BMSCore;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.veloMTL.veloMTL.Model.BMSCore.Trip;

@Repository
public interface TripRepository extends MongoRepository<Trip,String> {
    @Query("{ 'bike.$id': ?0, 'userEmail': ?1, 'startTime': { $exists: true }, $or: [ { 'endTime': null }, { 'endTime': '' }, { 'endTime': { $exists: false } } ] }")
    Trip findOngoingTrip(String bikeId, String userEmail);

    //    If u want to use this query, might need to add 'startTime': { $exists: true }
    @Query("{ 'bike.$id': ?0, 'userEmail': ?1, $or: [ { 'endTime': null }, { 'endTime': { $exists: false } } ] }")
    Trip findOngoingTripByRider(String bikeId, String userEmail);

    //    If u want to use this query, might need to add 'startTime': { $exists: true }
    @Query("{ 'bike.$id': ?0, 'userEmail': ?1, $or: [ { 'endTime': null }, { 'endTime': { $exists: false } } ] }")
    Trip findOngoingTripByOperator(String bikeId, String userEmail);

    @Query("{ 'userEmail': ?0 }")
    List<Trip> fetchTripsByUserId(String userEmail);

    @Query("{ 'userEmail': ?0, 'startTime': { $gte: { $dateAdd: { startDate: new Date(), unit: 'month', amount: -?1 } }, $lte: new Date() } }")
    List<Trip> findByUserEmailAndPeriod(String userEmail, int period);

    @Query("{ 'bike.$id': ?0, 'userEmail': ?1, 'reservationExpired': false, 'reserveStart': { $exists: true }, $or: [ { 'reserveEnd': null }, { 'reserveEnd': '' }, { 'reserveEnd': { $exists: false } } ] }")
    List<Trip> findOngoingReserveTrips(String bikeId, String userEmail);

//    Finds reservations in the past 'period' number of months
    @Query("{ 'userEmail': ?0, 'reserveStart': { $gte: { $dateAdd: { startDate: new Date(), unit: 'month', amount: -?1 } }, $lte: new Date() } }")
    List<Trip> findReservationByUserAndPeriod(String userEmail, int period);
}
