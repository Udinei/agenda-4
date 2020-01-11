package com.jvs.gd.resources;

import java.time.LocalDate;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jvs.gd.dto.UsuarioDTO;
import com.jvs.gd.models.Usuario;
import com.jvs.gd.resources.exceptions.ApiErrors;
import com.jvs.gd.resources.exceptions.BussinesExcepion;
import com.jvs.gd.services.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResources {
	
	LocalDate data = LocalDate.of(1972,03,17);
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UsuarioService service;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UsuarioDTO create(@RequestBody @Valid UsuarioDTO dto) {
	
		Usuario entity = modelMapper.map(dto, Usuario.class); 
					
		entity = service.save(entity);
		
		return modelMapper.map(entity, UsuarioDTO.class);		
	}

	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors handleValidationExceptions(MethodArgumentNotValidException ex) {
		BindingResult bindingResult = ex.getBindingResult();
				
		return new ApiErrors(bindingResult);
	
	}
	
	@ExceptionHandler(BussinesExcepion.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors handleBussinesException(BussinesExcepion ex) {
		return new ApiErrors(ex);
	}
	
}