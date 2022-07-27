package org.asiczen.pettracker.repository;

import org.asiczen.pettracker.model.message.GeofenceLocation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeofenceLocationsRepository extends MongoRepository<GeofenceLocation, String> {

    GeofenceLocation findByOwnerId(String ownerId);

}
