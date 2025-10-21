package com.veloMTL.veloMTL.Repository.BMSCore;
import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BikeRepository extends MongoRepository<Bike,String> {
}
