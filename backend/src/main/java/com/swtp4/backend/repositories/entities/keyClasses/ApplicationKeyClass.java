package com.swtp4.backend.repositories.entities.keyClasses;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationKeyClass implements Serializable {

    @Column(name = "id")
    private String id; //TODO ID-Generator Datentyp

    @Column(name = "creator")
    private String creator;
}
