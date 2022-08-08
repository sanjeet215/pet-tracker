package org.asiczen.pettracker.repository;


import org.asiczen.pettracker.model.Pet;
import org.asiczen.pettracker.model.message.PetTemperatureCalculate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetTemperatureRepository extends MongoRepository<PetTemperatureCalculate, String> {

    List<PetTemperatureCalculate> findByOwnerIdAndDevEui(String ownerId, String devEui);

    List<PetTemperatureCalculate> findByDevEui(String devEui);
}
