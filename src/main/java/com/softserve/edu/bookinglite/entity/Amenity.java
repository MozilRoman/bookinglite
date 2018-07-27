package com.softserve.edu.bookinglite.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "amenities")
public class Amenity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull
    private String name;

    @ManyToMany(mappedBy = "amenities")
    private Set<Apartment> apartments = new HashSet<>();

    public Amenity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Apartment> getApartments() {
        return apartments;
    }

    public void setApartments(Set<Apartment> apartments) {
        this.apartments = apartments;
    }

}
