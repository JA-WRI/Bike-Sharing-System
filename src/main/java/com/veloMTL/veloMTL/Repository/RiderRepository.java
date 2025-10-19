package com.veloMTL.veloMTL.Repository;

import com.veloMTL.veloMTL.Model.Users.Rider;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface RiderRepository extends MongoRepository<Rider, String> {

    Optional<Rider> findByEmail(String email);
    boolean existsByEmail(String email);
}
