package org.asiczen.pettracker.service;

import org.asiczen.pettracker.dto.response.OwnerResponse;
import org.asiczen.pettracker.model.Cow;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CowService {
    List<Cow> getAllCowList(String ownerId);

    long getCowCount(String ownerId);

    OwnerResponse deleteCow(String ownerId, String cowId);

    Cow editCow(Cow cow);
}
