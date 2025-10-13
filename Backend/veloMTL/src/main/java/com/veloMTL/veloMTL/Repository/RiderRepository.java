package com.veloMTL.veloMTL.Repository;

import com.veloMTL.veloMTL.Model.Users.Rider;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RiderRepository extends MongoRepository<Rider, String> {

}
