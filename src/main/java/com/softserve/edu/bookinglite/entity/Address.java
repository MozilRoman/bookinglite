package com.softserve.edu.bookinglite.entity;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String adressLine;
    @Column(nullable = false)
    private String zip;


    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name = "city_id",nullable = false)
    private City city;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "address")
    private List<Property> properties=new ArrayList<>();

    public Address() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdressLine() {
        return adressLine;
    }

    public void setAdressLine(String adressLine) {
        this.adressLine = adressLine;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
