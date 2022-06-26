package com.grpcflix.user.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@ToString
@Table(name = "\"user\"")
public class User {

    @Id
    private String login;
    private String name;
    private String genre;


}
