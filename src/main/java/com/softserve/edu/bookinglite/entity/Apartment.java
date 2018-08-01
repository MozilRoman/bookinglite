package com.softserve.edu.bookinglite.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "apartments")
public class Apartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition= "DECIMAL(8,2)", nullable = false)
    private BigDecimal price;

    @Column(name= "number_of_guests", nullable = false)
    private int numberOfGuests;

    @ManyToOne
    @JoinColumn(name = "apartment_type_id", nullable = false)
    private ApartmentType apartmentType;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "apartment_amenities",
            joinColumns = @JoinColumn(name = "apartment_id", updatable=false,insertable=false),
            inverseJoinColumns = @JoinColumn(name = "amenity_id", updatable=false,insertable=false))
    private Set<Amenity> amenities = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    public Apartment() {
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public ApartmentType getApartmentType() {
        return apartmentType;
    }

    public void setApartmentType(ApartmentType apartmentType) {
        this.apartmentType = apartmentType;
    }

    public Set<Amenity> getAmenities() {
        return amenities;
    }

    public void setAmenities(Set<Amenity> amenities) {
        this.amenities = amenities;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }
}
