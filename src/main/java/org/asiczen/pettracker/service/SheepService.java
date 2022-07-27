package org.asiczen.pettracker.service;

import org.asiczen.pettracker.dto.response.OwnerResponse;
import org.asiczen.pettracker.model.Sheep;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SheepService {
    List<Sheep> getAllSheepList(String ownerId);

    long getSheepCount(String ownerId);

    OwnerResponse deleteSheep(String ownerId, String sheepId);

    Sheep editSheep(Sheep sheep);
}
