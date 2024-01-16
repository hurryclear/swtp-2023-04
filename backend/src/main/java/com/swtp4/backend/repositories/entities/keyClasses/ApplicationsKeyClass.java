package com.swtp4.backend.repositories.entities.keyClasses;

import jakarta.persistence.Column;

import java.io.Serializable;
import java.util.UUID;

public class ApplicationsKeyClass implements Serializable {

    @Column(name = "id")
    private UUID id; //TODO ID-Generator Datentyp

    @Column(name = "creator")
    private String creator;
}
