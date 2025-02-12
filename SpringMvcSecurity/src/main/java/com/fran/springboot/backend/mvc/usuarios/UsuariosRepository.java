package com.fran.springboot.backend.mvc.usuarios;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UsuariosRepository extends CrudRepository<Usuario, Integer> {
    
    Optional<Usuario> findByCorreoAndPassword(String correo, String password);
}
