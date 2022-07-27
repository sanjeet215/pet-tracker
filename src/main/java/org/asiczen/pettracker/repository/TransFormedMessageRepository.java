package org.asiczen.pettracker.repository;

import org.asiczen.pettracker.model.message.TransFormedMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransFormedMessageRepository extends MongoRepository<TransFormedMessage, String> {

    //Optional<List<TransFormedMessage>> findFirstByOwnerIdOrderByTimeStampDesc(String ownerId);
    //Optional<List<TransFormedMessage>> findByOwnerIdGroupByDevEuiOrderByTimeStampDesc(String ownerId);

    Optional<List<TransFormedMessage>> findByDevEuiAndTimeStampBetween(String devEui, Date startTime, Date endTime);

}
