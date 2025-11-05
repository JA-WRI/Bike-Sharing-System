package com.veloMTL.veloMTL.PCR;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingRepository extends MongoRepository<Billing, String> {

}
