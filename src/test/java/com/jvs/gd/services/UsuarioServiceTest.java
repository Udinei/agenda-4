package com.jvs.gd.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.jvs.gd.models.Usuario;
import com.jvs.gd.repository.UsuarioRepository;
import com.jvs.gd.resources.exceptions.BussinesExcepion;
import com.jvs.gd.services.Impl.UsuarioServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	LocalDate data = LocalDate.of(1972,03,17);
	
	@MockBean
	UsuarioService service;
	
	@MockBean
	UsuarioRepository repository;
		
	
	@BeforeEach
	public void setUp() {
		// recebe a instancia do repository mock
	   this.service = new UsuarioServiceImpl( repository );	
	}
	
	
	@Test
	@DisplayName("Deve salvar um usuario")
	public void saveUsuarioTest() {
		// cenario
		Usuario  usuario = createUsuario();
		
		// quando o metodo existsByEmail for chamado retorna true
		Mockito.when( repository.existsByEmail(Mockito.anyString()) ).thenReturn(false);

		
		// simula UsuarioRepository ao chamar o metodo save, e retorna a instancia do usuario fake  
        Mockito.when( repository.save(usuario) ).thenReturn(
             		Usuario.builder().id("10").nome("usuario").email("email@gmail.com").senha("senha").dataNascimento(data).build()
            		);
        
		
		// execução
		Usuario savedUsuario = service.save(usuario);
		
		// verificacao
		assertThat(savedUsuario.getId()).isNotNull();
		assertThat(savedUsuario.getNome()).isEqualTo("usuario");
		assertThat(savedUsuario.getEmail()).isEqualTo("email@gmail.com");
		assertThat(savedUsuario.getSenha()).isEqualTo("senha");
		assertThat(savedUsuario.getDataNascimento()).isEqualTo("1972-03-17");
				
	}


	private Usuario createUsuario() {
		return Usuario.builder().nome("usuario").email("email@gmail.com").senha("senha").dataNascimento(data).build();
	}
	
	@Test
	@DisplayName("Deve lançar erro de negocio ao tentar salvar um usuario com email já utilizado")
	public void shouldNotSaveUsuarioWithDuplicateEmail() {
	
		// cenario
		Usuario  usuario = createUsuario();
		// quando o metodo existsByEmail for chamado retorna true
		Mockito.when( repository.existsByEmail(Mockito.anyString()) ).thenReturn(true);
		
		// execucao 
		// lanca exception ao tentar salvar o usuario  
		Throwable exception = Assertions.catchThrowable(() -> service.save(usuario));
		
		// verificacoes - 
		// verifica se a exception BussinesExcepion foi lancada
		assertThat(exception)
		.isInstanceOf(BussinesExcepion.class)
		.hasMessage("Email já cadastrado.");
		
		// verifica se esse metodo nunca foi executado
		Mockito.verify(repository, Mockito.never()).save(usuario);
		
	}
}
