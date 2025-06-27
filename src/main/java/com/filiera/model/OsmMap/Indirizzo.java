package com.filiera.model.OsmMap;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Indirizzo {
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String road;
    @Column(nullable = false)
    private String civicNumber;
}