package org.asiczen.pettracker.serviceImpl;

import org.asiczen.pettracker.exception.ResourceNotFoundException;
import org.asiczen.pettracker.model.Cattle;
import org.asiczen.pettracker.repository.CattleRepository;
import org.asiczen.pettracker.service.CattleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CattleServiceImpl implements CattleService {

    @Autowired
    CattleRepository cattleRepository;

    @Override
    public List<Cattle> getAllCattleList(String ownerId) {
        Optional<List<Cattle>> cattle = cattleRepository.findByOwnerId(ownerId);

        if (cattle.isPresent()) {
            return cattle.get();
        } else {
            throw new ResourceNotFoundException("No pet registered in this owner yet ....");
        }
    }

    @Override
    public long getCattleCount(String ownerId) {

        Optional<List<Cattle>> cattle = cattleRepository.findByOwnerId(ownerId);

        return (cattle.isPresent()) ? cattle.get().size() : 0L;
    }
}
