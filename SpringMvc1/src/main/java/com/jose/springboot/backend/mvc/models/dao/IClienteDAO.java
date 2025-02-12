package com.jose.springboot.backend.mvc.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.jose.springboot.backend.mvc.models.entity.Cliente;

public interface IClienteDAO extends CrudRepository<Cliente, Long> {

}
