package org.asiczen.pettracker.repository;

import org.asiczen.pettracker.model.message.PetLastLocation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetLastLocationRepository extends MongoRepository<PetLastLocation, String> {

    Optional<PetLastLocation> findByDevEui(String devEui);

    Optional<List<PetLastLocation>> findByOwnerId(String ownerId);
}
