package com.softserve.edu.bookinglite.controller;

import com.softserve.edu.bookinglite.dto.ApartmentDto;
import com.softserve.edu.bookinglite.service.ApartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApartmentController {

    @Autowired
    private ApartmentService apartmentService;

    @GetMapping("/apartment")
    public List<ApartmentDto> getAllApartments(){
        return apartmentService.findAllApartmentDto();
    }

    @GetMapping("/apartment/{id}")
    public ApartmentDto getApartment(@PathVariable ("id") Long id){
        return apartmentService.findDtoById(id);
    }

    @PostMapping("/property/{id}/apartment")
    public ResponseEntity<Void> saveApartment (@Valid @RequestBody ApartmentDto apartmentDto, @PathVariable ("id") Long propertyId){
        if (apartmentService.saveApartment(apartmentDto, propertyId)){
            return new ResponseEntity<Void>(HttpStatus.OK);
        }else {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@RequestBody ApartmentDto apartmentDto, @PathVariable ("id") Long apartmentId) {
        if (apartmentService.updateApartment(apartmentDto, apartmentId)){
            return new ResponseEntity<Void>(HttpStatus.OK);
        }else {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }
    }

}
