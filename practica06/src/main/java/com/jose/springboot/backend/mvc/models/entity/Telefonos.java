package com.jose.springboot.backend.mvc.models.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entidad que representa la tabla 'telefonos'.
 * 
 * @autor Jose Pinilla
 */
@Entity
@Table(name = "telefonos")
public class Telefonos implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String numero;
	private PrimeraVez primeraVez;

	public Telefonos() {
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

	@Column(name = "numero", nullable = false, length = 50)
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idprimeravez", nullable = false)
	public PrimeraVez getPrimeraVez() {
		return primeraVez;
	}

	public void setPrimeraVez(PrimeraVez primeraVez) {
		this.primeraVez = primeraVez;
	}
}
