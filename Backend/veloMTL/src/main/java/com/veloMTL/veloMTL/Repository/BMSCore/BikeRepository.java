package com.veloMTL.veloMTL.Repository.BMSCore;
import com.veloMTL.veloMTL.Model.BMSCore.Bike;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BikeRepository extends MongoRepository<Bike,String> {
}
