package com.veloMTL.veloMTL.Repository.BMSCore;

import com.veloMTL.veloMTL.Model.BMSCore.Dock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DockRepository extends MongoRepository<Dock, String> {
    List<Dock> findByStationId(String stationId);
}
