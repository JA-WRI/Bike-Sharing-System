package com.veloMTL.veloMTL.PCR;

import com.veloMTL.veloMTL.Model.Users.Rider;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillingRepository extends MongoRepository<Billing, String> {
    List<Billing> findAllByRiderID(String riderID);

}
