package org.asiczen.pettracker.repository;

import org.asiczen.pettracker.model.Pet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "pet" , path = "pet")
public interface PetRepository extends MongoRepository<Pet, String> {
}
