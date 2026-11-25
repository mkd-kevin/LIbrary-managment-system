package kigali.clinic.rw.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import kigali.clinic.rw.domain.Office;
import kigali.clinic.rw.repository.OfficeRepository;

@Service 
public class OfficeService {

    @Autowired 
    private OfficeRepository offRepo;

    public String saveOffice(Office office){
        if (offRepo.findByOfficeNumber(office.getOfficeNumber()).isPresent()) {
            return "Office number already exists";
        }

        offRepo.save(office);
        return "Office is saved successfully";
    }

    public List<Office> getAllOffices(){
        return offRepo.findAll();
    }

    public Optional<Office> getOfficeById(UUID id){
        return offRepo.findById(id);
    }

    public String updateOffice(UUID id, Office office){
        Optional<Office> existingOffice = offRepo.findById(id);

        if(existingOffice.isEmpty()){
            return "Office not found";
        }

        Office officeToUpdate = existingOffice.get();
        officeToUpdate.setName(office.getName());
        officeToUpdate.setOfficeNumber(office.getOfficeNumber());

        offRepo.save(officeToUpdate);
        return "Office is updated successfully";
    }

    public String deleteOffice(UUID id){
        if(!offRepo.existsById(id)){
            return "Office not found";
        }

        offRepo.deleteById(id);
        return "Office is deleted successfully";
    }

    public Optional<Object[]> getBusiestOffice() {
        return offRepo.findBusiestOffice(PageRequest.of(0, 1))
                .stream()
                .findFirst();
    }
}