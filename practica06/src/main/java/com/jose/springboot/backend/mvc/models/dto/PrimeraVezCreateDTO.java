package com.jose.springboot.backend.mvc.models.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para crear una PrimeraVez con sus telefonos.
 * 
 * @autor Jose Pinilla
 */
public class PrimeraVezCreateDTO {

    @NotNull(message = "El jokeId no puede ser nulo")
    private Integer jokeId;
    
    @NotEmpty(message = "El programa no puede estar vacío")
    private String programa;

    private LocalDate fechaEmision;

    @NotEmpty(message = "Debe haber al menos un teléfono")
    private List<String> telefonos;

    public PrimeraVezCreateDTO() {
    }

    public PrimeraVezCreateDTO(Integer jokeId, String programa, LocalDate fechaEmision, List<String> telefonos) {
        this.jokeId = jokeId;
        this.programa = programa;
        this.fechaEmision = fechaEmision;
        this.telefonos = telefonos;
    }

    public Integer getJokeId() {
        return jokeId;
    }

    public void setJokeId(Integer jokeId) {
        this.jokeId = jokeId;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public List<String> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(List<String> telefonos) {
        this.telefonos = telefonos;
    }
}

