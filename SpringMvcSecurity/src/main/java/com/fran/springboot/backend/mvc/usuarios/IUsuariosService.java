package com.fran.springboot.backend.mvc.usuarios;

import java.util.List;

import com.fran.springboot.backend.mvc.auth.dto.LoginDto;

public interface IUsuariosService {

	public List<Usuario> getAll();
	public Usuario getById(int id);
	public Usuario login(LoginDto loginDto);
	public boolean insert(Usuario u);
	public void delete(int idUsuario);
	
}
