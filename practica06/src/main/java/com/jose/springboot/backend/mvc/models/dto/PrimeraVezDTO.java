package com.jose.springboot.backend.mvc.models.dto;

import java.time.LocalDate;

/**
 * DTO para mostrar la info de PrimeraVez (GET).
 * 
 * @autor Jose Pinilla
 */
public class PrimeraVezDTO {
    
    private Long id;
    private String programa;
    private LocalDate fechaEmision;
    private Integer jokeId; 

    public PrimeraVezDTO() {
    }

    public PrimeraVezDTO(Long id, String programa, LocalDate fechaEmision, Integer jokeId) {
        this.id = id;
        this.programa = programa;
        this.fechaEmision = fechaEmision;
        this.jokeId = jokeId;
    }

    public Long getId() {
        return id;
    }

    public String getPrograma() {
        return programa;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public Integer getJokeId() {
        return jokeId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public void setJokeId(Integer jokeId) {
        this.jokeId = jokeId;
    }
}
