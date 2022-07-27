package org.asiczen.pettracker.repository;

import org.asiczen.pettracker.model.Sheep;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "sheep" , path = "sheep")
public interface SheepRepository extends MongoRepository<Sheep, String> {
    Optional<List<Sheep>> findByOwnerId(String ownerId);

    Sheep findByDeviceDeviceId(String devEui);

    Optional<Sheep> findByOwnerIdAndDeviceDeviceId(String ownerId, String devEui);

    Optional<Sheep> findByOwnerIdAndId(String ownerId, String cattleId);
}
