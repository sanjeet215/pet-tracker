package org.asiczen.pettracker.repository;

import org.asiczen.pettracker.model.Address;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "address", path = "address")
public interface AddressRepository extends MongoRepository<Address, String> {
}
