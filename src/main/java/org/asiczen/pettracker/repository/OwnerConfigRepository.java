package org.asiczen.pettracker.repository;

import org.asiczen.pettracker.model.OwnerConfig;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnerConfigRepository extends MongoRepository<OwnerConfig, String> {

    public Optional<OwnerConfig> findByOwnerId(String ownerId);
}
