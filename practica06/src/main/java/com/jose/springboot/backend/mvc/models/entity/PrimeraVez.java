package com.jose.springboot.backend.mvc.models.entity;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Entidad que representa la tabla 'primera_vez'.
 * 
 * @autor Jose Pinilla
 */
@Entity
@Table(name = "primera_vez")
public class PrimeraVez implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String programa;
	private LocalDate fechaEmision;
	private Jokes jokes; 

	public PrimeraVez() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "programa", length = 255)
	public String getPrograma() {
		return programa;
	}

	public void setPrograma(String programa) {
		this.programa = programa;
	}

	@Column(name = "fecha_emision")
	public LocalDate getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(LocalDate fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idjoke", unique = true)
	public Jokes getJokes() {
		return jokes;
	}

	public void setJokes(Jokes jokes) {
		this.jokes = jokes;
	}
}

