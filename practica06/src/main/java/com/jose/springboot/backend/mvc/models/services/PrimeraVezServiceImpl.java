package com.jose.springboot.backend.mvc.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jose.springboot.backend.mvc.models.dao.IPrimeraVezDAO;
import com.jose.springboot.backend.mvc.models.entity.PrimeraVez;

/**
 * PrimeraVezServiceImpl
 * Implementa la interfaz IPrimeraVezService
 * 
 * @autor Jose Pinilla
 */
@Service
public class PrimeraVezServiceImpl implements IPrimeraVezService {

    @Autowired
    private IPrimeraVezDAO primeraVezDAO;

    @Override
    @Transactional(readOnly = true)
    public List<PrimeraVez> findAll() {
        return (List<PrimeraVez>) primeraVezDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public PrimeraVez findById(Long id) {
        return primeraVezDAO.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public PrimeraVez save(PrimeraVez primeraVez) {
        return primeraVezDAO.save(primeraVez);
    }

	@Override
	public PrimeraVez findByJokeId(Integer jokeId) {
		return primeraVezDAO.findByJokeId(jokeId);
	}
}
