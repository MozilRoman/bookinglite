package com.softserve.edu.bookinglite.controller;

import com.softserve.edu.bookinglite.service.dto.ApartmentDto;
import com.softserve.edu.bookinglite.service.ApartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApartmentController {

    private final ApartmentService apartmentService;

    @Autowired
    public ApartmentController(ApartmentService apartmentService) {
        this.apartmentService = apartmentService;
    }

    @GetMapping("/apartment")
    public List<ApartmentDto> getAllApartments(){
        return apartmentService.findAllApartmentDtos();
    }

    @GetMapping("/apartment/{id}")
    public ApartmentDto getApartment(@PathVariable ("id") Long apartmentId){
        return apartmentService.findApartmentDtoById(apartmentId);
    }

    @PostMapping("/property/{id}/apartment")
    public ResponseEntity<Void> saveApartment (@Valid @RequestBody ApartmentDto apartmentDto,
                                               @PathVariable ("id") Long propertyId,
                                               Principal principal){
        Long userId = Long.parseLong(principal.getName());
        if (apartmentService.saveApartment(apartmentDto, propertyId, userId)){
            return new ResponseEntity<Void>(HttpStatus.CREATED);
        }else {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/apartment/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody ApartmentDto apartmentDto,
                                       @PathVariable ("id") Long apartmentId,
                                       Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        if (apartmentService.updateApartment(apartmentDto, apartmentId, userId)){
            return new ResponseEntity<Void>(HttpStatus.OK);
        }else {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }
    }

}