package com.veloMTL.veloMTL.Repository;

import com.veloMTL.veloMTL.Model.Rider;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RiderRepository extends MongoRepository<Rider, String> {

}
