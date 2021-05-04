package org.asiczen.pettracker.repository;

import org.asiczen.pettracker.model.Device;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//@RepositoryRestResource(collectionResourceRel = "device", path = "device")
public interface DeviceRepository extends MongoRepository<Device, String> {
}
