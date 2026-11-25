package kigali.clinic.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.repository.SpecializationRepository;

@Service 
public class SpecializationService {

    @Autowired 
    private SpecializationRepository specRepo;

    public String saveSpecialization(Specialization specialization){
        if(specRepo.findByNameIgnoreCase(specialization.getName()).isPresent()){
            return "Specialization already exists";
        }

        specRepo.save(specialization);
        return "Specialization is saved successfully";
    }

    public List<Specialization> getAllSpecializations(){
        return specRepo.findAll();
    }

    public List<Specialization> getUnusedSpecializations() {
        return specRepo.findUnusedSpecializations();
    }

    public Optional<Specialization> getSpecializationById(UUID id){
        return specRepo.findById(id);
    }

    public String updateSpecialization(UUID id, Specialization specialization){
        Optional<Specialization> existingSpecialization = specRepo.findById(id);

        if(existingSpecialization.isEmpty()){
            return "Specialization not found";
        }

        Optional<Specialization> sameName = specRepo.findByNameIgnoreCase(specialization.getName());

        if(sameName.isPresent() && !sameName.get().getId().equals(id)){
            return "Specialization already exists";
        }

        Specialization specializationToUpdate = existingSpecialization.get();
        specializationToUpdate.setName(specialization.getName());

        specRepo.save(specializationToUpdate);
        return "Specialization is updated successfully";
    }

    @Transactional
    public String deleteSpecialization(UUID id){
        Optional<Specialization> existingSpecialization = specRepo.findById(id);

        if(existingSpecialization.isEmpty()){
            return "Specialization not found";
        }

        if(!existingSpecialization.get().getDoctors().isEmpty()){
            return "Specialization is assigned to doctors and cannot be deleted";
        }

        specRepo.deleteById(id);
        return "Specialization is deleted successfully";
    }
}