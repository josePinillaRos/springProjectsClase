package com.jose.springboot.backend.eventos.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.jose.springboot.backend.eventos.models.entity.Usuario;

public interface IUsuario extends JpaRepository<Usuario, Integer> {
	//PARA PROHIBIR EL BORRADO
	@Override
	@RestResource(exported = false)
	void deleteById(Integer id);
}
