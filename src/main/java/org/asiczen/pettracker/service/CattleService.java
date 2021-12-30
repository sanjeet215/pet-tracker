package org.asiczen.pettracker.service;

import org.asiczen.pettracker.dto.response.OwnerResponse;
import org.asiczen.pettracker.model.Cattle;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CattleService {

    List<Cattle> getAllCattleList(String ownerId);

    long getCattleCount(String ownerId);

    OwnerResponse deleteCattle(String ownerId, String cattleId);

    Cattle editCattle(Cattle cattle);
}
