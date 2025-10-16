package com.veloMTL.veloMTL.Repository;

import com.veloMTL.veloMTL.Model.Users.Operator;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OperatorRepository extends MongoRepository<Operator, String> {
}
