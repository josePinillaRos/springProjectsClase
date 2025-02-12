package com.jose.springboot.backend.mvc.models.services;

import java.util.List;

import com.jose.springboot.backend.mvc.models.entity.Telefonos;

/**
 * Interfaz para servicios de Telefonos.
 * 
 * @autor Jose Pinilla
 */
public interface ITelefonosService {
	public List<Telefonos> findAll();
	public Telefonos findById(Long id);
	public Telefonos save(Telefonos telefonos);
}
