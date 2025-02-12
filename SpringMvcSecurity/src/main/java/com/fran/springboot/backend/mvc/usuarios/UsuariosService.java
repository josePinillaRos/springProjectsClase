package com.fran.springboot.backend.mvc.usuarios;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.fran.springboot.backend.mvc.auth.dto.LoginDto;
import com.fran.springboot.backend.mvc.utilidades.ImageUtils;
import com.fran.springboot.backend.mvc.utilidades.SecurityUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuariosService implements IUsuariosService {
	
	@Autowired
	SecurityUtils securityUtils;
	@Autowired
	ImageUtils imageUtils;
	
    private final UsuariosRepository usuariosRepository;

    @Override
    @Transactional(readOnly=true)
    public List<Usuario> getAll() {
        return (List<Usuario>) usuariosRepository.findAll();
    }

    @Override
    @Transactional(readOnly=true)
    public Usuario getById(int id) {
        return usuariosRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }
    @Override
    @Transactional(readOnly=true)
    public Usuario login(LoginDto loginDto){
    	try {
    		return usuariosRepository.findByCorreoAndPassword(loginDto.getCorreo(), securityUtils.encodePassword(loginDto.getPassword())).get();
    	}catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
    }

    @Override
    @Transactional
    public boolean insert(Usuario u) {
    	try {
			if(u.getAvatar()!=null && u.getAvatar().length()>0)
				u.setAvatar(imageUtils.saveImageBase64("users", u.getAvatar()));				
			else 
				u.setAvatar("");
	
			u.setPassword(securityUtils.encodePassword(u.getPassword()));
			usuariosRepository.save(u);
			return true;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    }

    @Override
    @Transactional
    public void delete(int idUsuario) {
        usuariosRepository.deleteById(idUsuario);
    }

}