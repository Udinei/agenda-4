package com.jvs.gd.repositoryTest;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.jvs.gd.repository.UsuarioRepository;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataMongoTest
public class UsuarioRepository_Test {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	
	@Test
	@DisplayName("Deve retornar verdadeiro quando existir um usuario com o email informado")
	public void returnTrueWhenEmailUsuarioExists() {
		// given
		// cria um objeto complexo com  chave e valor 
		DBObject objectToSave = BasicDBObjectBuilder.start().add("nome", "usuario")
					                                             .add("email","email6@gmail.com")
					                                             .add("senha", "senha")
					                                             .add("dataNascimento", "1972-03-17")
					                                             .get();
		
		
		// salva um objeto da colection usuario que contenha as chave e valor definidos em objectToSave 
		mongoTemplate.save(objectToSave, "usuario");
		
		// when
		boolean exists = usuarioRepository.existsByEmail("email6@gmail.com");
		
		//then
		assertThat(exists).isTrue();
		
	}

	
	@Test
	@DisplayName("Deve retornar falso quando nao existir um usuario com o email informado")
	public void returnFalseWhenDoesntExistsUsuarioWithThisEmail() {
		// given
		// cria um objeto complexo com  chave e valor 
		DBObject objectToSave = BasicDBObjectBuilder.start().add("nome", "usuario")
					                                             .add("email","email6@gmail.com")
					                                             .add("senha", "senha")
					                                             .add("dataNascimento", "1972-03-17")
					                                             .get();
		
		
		// salva um objeto da colection usuario que contenha as chave e valor definidos em objectToSave 
		mongoTemplate.save(objectToSave, "usuario");
		
		// when
		boolean exists = usuarioRepository.existsByEmail("email@gmail.com");
		
		//then
		assertThat(exists).isFalse();
		
	}
	
	/*@Test
	public void saveUsuario() {
		
		// given
		DBObject objectToSave = BasicDBObjectBuilder.start().add("nome", "usuario")
				                                             .add("email","email6@gmail.com")
				                                             .add("senha", "senha")
				                                             .add("dataNascimento", "1972-03-17")
				                                             .get();

		// when
		mongoTemplate.save(objectToSave, "usuario");

		// then
		assertThat(mongoTemplate.findAll(DBObject.class, "usuario")).extracting("nome").containsOnly("usuario");
	}*/
}
