package com.grpcflix.movie.entity;

import lombok.Data;
import lombok.ToString;

//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Data
@Entity
@ToString
public class Movie {

    @Id
    private int id;
    private String title;

    @Column(name="release_year")
    private int year;

    private double rating;
    private String genre;

}
