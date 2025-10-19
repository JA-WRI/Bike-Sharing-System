package com.veloMTL.veloMTL.Repository.Users;

import com.veloMTL.veloMTL.Model.Users.Rider;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiderRepository extends MongoRepository<Rider, String> {

    Optional<Rider> findByEmail(String email);
    boolean existsByEmail(String email);
}
