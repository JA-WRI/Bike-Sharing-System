package com.veloMTL.veloMTL.Repository.BMSCore;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.veloMTL.veloMTL.Model.BMSCore.Reservation;

@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {
    @Query("{ 'reserveUser': ?0 }")
    List<Reservation> findByReserveUser(String reserveUser);

    @Query("{ 'reserveUser': ?0, 'reserveStart': { $gte: { $dateAdd: { startDate: new Date(), unit: 'month', amount: -?1 } }, $lte: new Date() } }")
    List<Reservation> findByReserveUserAndPeriod(String reserveUser, int period);
}
