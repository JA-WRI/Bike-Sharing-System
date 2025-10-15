package com.veloMTL.veloMTL.Repository;

import com.veloMTL.veloMTL.Model.Station;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRepository extends MongoRepository<Station, String>{


}
