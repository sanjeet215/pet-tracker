package org.asiczen.pettracker.repository;

import org.asiczen.pettracker.model.Cow;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "cow" , path = "cow")
public interface CowRepository extends MongoRepository<Cow, String> {
    Optional<List<Cow>> findByOwnerId(String ownerId);

    Cow findByDeviceDeviceId(String devEui);

    Optional<Cow> findByOwnerIdAndDeviceDeviceId(String ownerId, String devEui);

    Optional<Cow> findByOwnerIdAndId(String ownerId, String petId);
}
