package com.jose.springboot.backend.eventos.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jose.springboot.backend.eventos.models.entity.Evento;

@RepositoryRestResource(path = "eventos", collectionResourceRel = "eventos")
public interface IEvento extends JpaRepository<Evento, Integer> {

}
