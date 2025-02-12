package com.jose.springboot.backend.eventos.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jose.springboot.backend.eventos.models.entity.DatosUsuario;

public interface IDatosUsuario extends JpaRepository<DatosUsuario, Integer> {

}
