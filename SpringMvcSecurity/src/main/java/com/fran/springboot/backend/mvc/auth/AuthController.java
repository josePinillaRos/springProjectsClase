package com.fran.springboot.backend.mvc.auth;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fran.springboot.backend.mvc.auth.dto.LoginDto;
import com.fran.springboot.backend.mvc.auth.dto.RespuestaTokenDto;
import com.fran.springboot.backend.mvc.usuarios.Usuario;
import com.fran.springboot.backend.mvc.usuarios.UsuariosService;
import com.fran.springboot.backend.mvc.usuarios.dto.UsuarioInsertDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UsuariosService usuariosService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) throws NoSuchAlgorithmException {
        Usuario user = usuariosService.login(loginDto);
        if(user !=null)
        	return ResponseEntity.ok().body(new RespuestaTokenDto(getToken(user)));
        else
        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("usuario y/o contraseña incorrectos");
    }

    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> registro(@RequestBody UsuarioInsertDto userDto) {
        boolean resultado = usuariosService.insert(new Usuario(userDto));
        System.out.println(resultado);
        if(resultado) {
			return ResponseEntity.status(HttpStatus.CREATED).body(null); 
		}else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
    }

    private String getToken(Usuario user) {
        Algorithm algorithm = Algorithm.HMAC256("token101");
        String token = JWT.create()
                .withIssuer("frangarcia")
                .withClaim("id", user.getId())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000))) // Caduca en un día
                .sign(algorithm);

        return token;
    }
}