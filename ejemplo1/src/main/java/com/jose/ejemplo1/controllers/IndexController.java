package com.jose.ejemplo1.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app")
public class IndexController {
	
	@GetMapping({"/", "/index", "", "/home"})
	public String index(Model model) {
		model.addAttribute("titulo", "Hola Mundo con Spring Boot");
		model.addAttribute("mensaje", "Bienvenido a mi aplicación web con Spring Boot");
		return "index"; //Busca el archivo index.html en la carpeta resources/templates
	}
}
