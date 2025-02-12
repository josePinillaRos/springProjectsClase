package com.jose.springboot.backend.mvc.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jose.springboot.backend.mvc.models.dao.ITelefonosDAO;
import com.jose.springboot.backend.mvc.models.entity.Telefonos;

/**
 * TelefonosServiceImpl
 * Implementa la interfaz ITelefonosService
 * 
 * @autor Jose Pinilla
 */
@Service
public class TelefonosServiceImpl implements ITelefonosService {

	@Autowired
	private ITelefonosDAO telefonosDAO;
	
	@Override
	@Transactional(readOnly = true)
	public List<Telefonos> findAll() {
		return (List<Telefonos>)telefonosDAO.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Telefonos findById(Long id) {
		return telefonosDAO.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Telefonos save(Telefonos telefonos) {
		return telefonosDAO.save(telefonos);
	}
}
