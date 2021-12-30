package org.asiczen.pettracker.repository;

import org.asiczen.pettracker.model.Pet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "pet" , path = "pet")
public interface PetRepository extends MongoRepository<Pet, String> {
    Optional<List<Pet>> findByOwnerId(String ownerId);

    Pet findByDeviceDeviceId(String devEui);

    Optional<Pet> findByOwnerIdAndDeviceDeviceId(String ownerId, String devEui);

    Optional<Pet> findByOwnerIdAndId(String ownerId, String petId);
}
