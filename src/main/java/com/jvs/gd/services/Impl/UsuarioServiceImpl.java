package com.jvs.gd.services.Impl;

import org.springframework.stereotype.Service;

import com.jvs.gd.models.Usuario;
import com.jvs.gd.repository.UsuarioRepository;
import com.jvs.gd.resources.exceptions.BussinesExcepion;
import com.jvs.gd.services.UsuarioService;


@Service
public class UsuarioServiceImpl implements UsuarioService {

	
	private UsuarioRepository repository;
	
	public UsuarioServiceImpl(UsuarioRepository repository) {
		this.repository = repository;
	}

	@Override
	public Usuario save(Usuario usuario) {
		if(repository.existsByEmail(usuario.getEmail())) {
		     throw new BussinesExcepion("Email já cadastrado.");	
		}
		
		return repository.save(usuario);
	}

	
}
