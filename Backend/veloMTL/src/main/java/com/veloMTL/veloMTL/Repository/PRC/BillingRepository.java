package com.veloMTL.veloMTL.Repository.PRC;

import com.veloMTL.veloMTL.PCR.Billing;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillingRepository extends MongoRepository<Billing, String> {
    List<Billing> findAllByriderID(String riderID);

}
