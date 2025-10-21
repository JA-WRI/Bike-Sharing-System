package com.veloMTL.veloMTL.Repository;

import com.veloMTL.veloMTL.Model.Users.Operator;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OperatorRepository extends MongoRepository<Operator, String> {
    Optional<Operator> findByEmail(String email);
    boolean existsByEmail(String email);
}
