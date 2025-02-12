package com.jose.springboot.backend.mvc.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jose.springboot.backend.mvc.models.dto.PrimeraVezCreateDTO;
import com.jose.springboot.backend.mvc.models.dto.PrimeraVezDTO;
import com.jose.springboot.backend.mvc.models.entity.Jokes;
import com.jose.springboot.backend.mvc.models.entity.PrimeraVez;
import com.jose.springboot.backend.mvc.models.entity.Telefonos;
import com.jose.springboot.backend.mvc.models.services.IJokesService;
import com.jose.springboot.backend.mvc.models.services.IPrimeraVezService;
import com.jose.springboot.backend.mvc.models.services.ITelefonosService;
import com.jose.springboot.backend.mvc.models.services.PrimeraVezServiceImpl;

import jakarta.validation.Valid;

/**
 * class PrimeraVezRestController
 * se encarga de manejar las peticiones HTTP de la entidad 'PrimeraVez'.
 * 
 * @author Jose Pinilla
 */
@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/primera-vez")
public class PrimeraVezRestController {

    @Autowired
    private IPrimeraVezService primeraVezService;

    @Autowired
    private IJokesService jokesService;

    @Autowired
    private ITelefonosService telefonosService;
    
    @Autowired
    private PrimeraVezServiceImpl primeraVezImpl;
    
    /**
     * GET /primera-vez
     * Devuelve todas las 'PrimeraVez' en forma de DTO.
     */
    @GetMapping({"/", ""})
    public List<PrimeraVezDTO> getAllPrimeraVez() {
        List<PrimeraVez> lista = primeraVezService.findAll();
        return lista.stream()
            .map(pv -> new PrimeraVezDTO(
                    pv.getId(),
                    pv.getPrograma(),
                    pv.getFechaEmision(),
                    (pv.getJokes() != null) ? pv.getJokes().getId() : null
            ))
            .collect(Collectors.toList());
    }

    /**
     * GET /primera-vez/{id}
     * Devuelve un 'PrimeraVezDTO' por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPrimeraVezById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        PrimeraVez entity = null;

        try {
            entity = primeraVezService.findById(id);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (entity == null) {
            response.put("mensaje", "La primera_vez con ID " + id + " no existe.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        PrimeraVezDTO dto = new PrimeraVezDTO(
                entity.getId(),
                entity.getPrograma(),
                entity.getFechaEmision(),
                (entity.getJokes() != null) ? entity.getJokes().getId() : null
        );

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    /**
     * POST /primera-vez
     * Crea una nueva 'PrimeraVez' obligando a introducir al menos 1 teléfono.
     */
    @PostMapping({"/", ""})
    public ResponseEntity<?> createPrimeraVez(
            @Valid @RequestBody PrimeraVezCreateDTO createDTO,
            BindingResult result
    ) {
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .toList();
            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Jokes jokesEntity = jokesService.findById(createDTO.getJokeId());
        if (jokesEntity == null) {
            response.put("mensaje", "No existe un Joke con ID " + createDTO.getJokeId());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (createDTO.getTelefonos() == null || createDTO.getTelefonos().isEmpty()) {
            response.put("mensaje", "Debes introducir al menos un teléfono");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        PrimeraVez checkPrimeraVez = primeraVezImpl.findByJokeId(createDTO.getJokeId());
        if (checkPrimeraVez != null) {
            response.put("mensaje", "El Joke con ID " 
                + createDTO.getJokeId() 
                + " ya tiene una primera vez creada.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        PrimeraVez nueva = new PrimeraVez();
        nueva.setPrograma(createDTO.getPrograma());
        nueva.setFechaEmision(createDTO.getFechaEmision());
        nueva.setJokes(jokesEntity);

        try {
            PrimeraVez guardada = primeraVezService.save(nueva);

            for (String numero : createDTO.getTelefonos()) {
                Telefonos tel = new Telefonos();
                tel.setNumero(numero);
                tel.setPrimeraVez(guardada);
                telefonosService.save(tel);
            }

            response.put("mensaje", "PrimeraVez creada con éxito, con al menos un teléfono.");
            response.put("primeraVezId", guardada.getId());
            response.put("jokeId", guardada.getJokes().getId());
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (DataAccessException e) {
            response.put("mensaje", "Error al guardar en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/search-by-joke/{jokeId}")
    public ResponseEntity<?> searchPrimeraVezByJokeId(@PathVariable Integer jokeId) {
        PrimeraVez pv = primeraVezService.findByJokeId(jokeId); 

        if (pv == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(pv); 
        }
    }
}

