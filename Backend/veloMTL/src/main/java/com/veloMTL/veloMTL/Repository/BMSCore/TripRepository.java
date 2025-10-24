package com.veloMTL.veloMTL.Repository.BMSCore;
import com.veloMTL.veloMTL.Model.BMSCore.Trip;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends MongoRepository<Trip,String> {
}
