package com.jose.springboot.backend.mvc.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.jose.springboot.backend.mvc.models.entity.PrimeraVez;

/**
 * interfaz IPrimeraVezDAO que extiende de CrudRepository
 *
 *@author Jose Pinilla
 */
public interface IPrimeraVezDAO extends CrudRepository<PrimeraVez, Long> {
	@Query("SELECT p FROM PrimeraVez p WHERE p.jokes.id = :jokeId")
	PrimeraVez findByJokeId(@Param("jokeId") Integer jokeId);
}