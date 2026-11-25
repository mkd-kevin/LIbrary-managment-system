package kigali.clinic.rw.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kigali.clinic.rw.domain.Specialization;
import kigali.clinic.rw.service.SpecializationService;

@RestController 
@RequestMapping (value={"/api/specialization", "/api/specializations"})
public class SpecializationController {

    @Autowired 
    private SpecializationService specServe;

    @PostMapping(value = "/save")  
    public ResponseEntity<?> saveSpecialization(@RequestBody Specialization specialization){
        
       String returnedMessage =  specServe.saveSpecialization(specialization);

       if(returnedMessage.equals("Specialization is saved successfully")){
        return new ResponseEntity<>(returnedMessage,HttpStatus.OK);
       }else{
        return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
       }
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllSpecializations(){
        List<Specialization> specializations = specServe.getAllSpecializations();
        return new ResponseEntity<>(specializations, HttpStatus.OK);
    }

    @GetMapping(value = "/unused")
    public ResponseEntity<?> getUnusedSpecializations(){
        return new ResponseEntity<>(
                specServe.getUnusedSpecializations(),
                HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getSpecializationById(@PathVariable UUID id){
        Optional<Specialization> specialization = specServe.getSpecializationById(id);

        if(specialization.isPresent()){
            return new ResponseEntity<>(specialization.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Specialization not found", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updateSpecialization(@PathVariable UUID id, @RequestBody Specialization specialization){
        String returnedMessage = specServe.updateSpecialization(id, specialization);

        if(returnedMessage.equals("Specialization is updated successfully")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }else if(returnedMessage.equals("Specialization not found")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteSpecialization(@PathVariable UUID id){
        String returnedMessage = specServe.deleteSpecialization(id);

        if(returnedMessage.equals("Specialization is deleted successfully")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }else if(returnedMessage.equals("Specialization not found")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }
    }
}