package com.veloMTL.veloMTL.Repository.BMSCore;
import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import com.veloMTL.veloMTL.Model.BMSCore.Trip;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BikeRepository extends MongoRepository<Bike,String> {
    @Query("{ 'reserveUser': ?0 }")
    List<Bike> findByReserveUser(String reserveUser);
}
