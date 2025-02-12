package com.jose.springboot.backend.mvc.models.services;

import java.util.List;
import com.jose.springboot.backend.mvc.models.entity.PrimeraVez;

/**
 * Interfaz para servicios de PrimeraVez.
 * 
 * @autor Jose Pinilla
 */
public interface IPrimeraVezService {
    
	public List<PrimeraVez> findAll();
	public PrimeraVez findById(Long id);
	public PrimeraVez save(PrimeraVez primeraVez);
	public PrimeraVez findByJokeId(Integer jokeId);
}
