package com.fran.springboot.backend.mvc.auth.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class RespuestaTokenDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private String accessToken;
}