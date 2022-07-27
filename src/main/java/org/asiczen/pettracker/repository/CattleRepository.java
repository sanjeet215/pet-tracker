package org.asiczen.pettracker.repository;

import org.asiczen.pettracker.model.Cattle;
import org.asiczen.pettracker.model.Pet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "cattle" , path = "cattle")
public interface CattleRepository extends MongoRepository<Cattle, String> {

    Optional<List<Cattle>> findByOwnerId(String ownerId);

    Cattle findByDeviceDeviceId(String devEui);

    Optional<Cattle> findByOwnerIdAndDeviceDeviceId(String ownerId, String devEui);

    Optional<Cattle> findByOwnerIdAndId(String ownerId, String cattleId);
}
