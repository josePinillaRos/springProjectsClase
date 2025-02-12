package com.jose.springboot.backend.eventos.models.projection;

import org.springframework.data.rest.core.config.Projection;

import com.jose.springboot.backend.eventos.models.entity.DatosUsuario;
import com.jose.springboot.backend.eventos.models.entity.Usuario;

@Projection(name = "conDatosUsuario", types = {Usuario.class })
public interface ConDatosUsuario {
	public String getUsuario();
	public DatosUsuario getDatosUsuario();
}
