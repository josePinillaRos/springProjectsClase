package com.jose.springboot.backend.mvc.models.dao;

import org.springframework.data.repository.CrudRepository;
import com.jose.springboot.backend.mvc.models.entity.Telefonos;

/**
 * Interfaz ITelefonosDAO que extiende de CrudRepository
 * 
 * @author Jose Pinilla
 */
public interface ITelefonosDAO extends CrudRepository<Telefonos, Long> {

}