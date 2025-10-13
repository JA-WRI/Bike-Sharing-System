package com.veloMTL.veloMTL.Repository;

import com.veloMTL.veloMTL.Model.Dock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DockRepository extends MongoRepository<Dock, String> {
}
