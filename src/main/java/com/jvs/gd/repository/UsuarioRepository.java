package com.jvs.gd.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.jvs.gd.models.Usuario;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {

	 boolean existsByEmail(String email);
	 
	 Optional<Usuario> findByEmail(String email);
	 
}
