package org.asiczen.pettracker.repository;

import org.asiczen.pettracker.model.Owner;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "owner",path = "owner")
public interface OwnerRepository extends MongoRepository<Owner,String> {
    public Owner findByOwnerId(String ownerId);
}
