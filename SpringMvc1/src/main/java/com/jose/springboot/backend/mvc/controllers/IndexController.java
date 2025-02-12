package com.jose.springboot.backend.mvc.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clientes")
public class IndexController {

	@GetMapping({"/", ""})
	public String index() {
		return "clientes/index";
	}
}
