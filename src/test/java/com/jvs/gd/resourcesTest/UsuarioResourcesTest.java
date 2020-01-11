package com.jvs.gd.resourcesTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvs.gd.dto.UsuarioDTO;
import com.jvs.gd.models.Usuario;
import com.jvs.gd.resources.exceptions.BussinesExcepion;
import com.jvs.gd.services.UsuarioService;

/**
 * Essa classe realiza os testes unitarios dos resources api
 * 
 * */
@ExtendWith(SpringExtension.class) // cria um mini contexto de injecao de dependencia
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class UsuarioResourcesTest {
	
	static String BOOK_API = "/api/usuarios";
	LocalDate data = LocalDate.of(1972,03,17);
	
	@Autowired
	MockMvc mvc;
	
	@MockBean
	UsuarioService service;
	
	@Test
	@DisplayName("Deve criar um usuario com sucesso")
	public void deveCriarUmUsuarioComSucesso() throws Exception {
		// instancia um usuario
		UsuarioDTO usuarioDTO = createNewUser();
		 
		Usuario saveUsuario = Usuario.builder().id("10").nome("joao").email("email@gmail.com").senha("senha").dataNascimento(data).build();

		
		// simula, dado que ao chamar o metodo save da interface UsuarioService para salvar qualquer usuario, retorne o objeto fake(Usuario) setado em saveUsuario 
		BDDMockito.given(service.save(Mockito.any(Usuario.class))).willReturn(saveUsuario);
		
		
		// transforma qualquer objeto em json
		String json = new ObjectMapper().writeValueAsString(usuarioDTO); 
		
		// construindo a requisição da tipo Json - rota BOOK_API
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
		.post(BOOK_API)
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.content(json);
	
		// executando request - rjson de retorno que espero - andExpect são assertivas 
		mvc
			.perform(request) // rota da requisicao
			.andExpect( status().isCreated() ) // - o status foi 201 created
		    .andExpect( jsonPath( "id").value("10")) 
		    .andExpect( jsonPath("nome").value(usuarioDTO.getNome()))
		    .andExpect( jsonPath("email").value(usuarioDTO.getEmail()))
		    .andExpect( jsonPath("senha").value(usuarioDTO.getSenha()))
		    .andExpect( jsonPath("dataNascimento").value(usuarioDTO.getDataNascimento().toString()));
		    
			
	}

	
	@Test
	@DisplayName("Deve lançar erro de validação quando não houver dados suficientes para criação do usuario ")
	public void deveLancarErroDeValidacaoQuandoNaoHouverDados() throws Exception {
	   	
		// transforma qualquer objeto em json
		String json = new ObjectMapper().writeValueAsString(new UsuarioDTO());
		
		// construindo a requisição da tipo Json - rota BOOK_API
				MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(BOOK_API)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);
				
				// json de retorno que espero - andExpect são assertivas 
				mvc
					.perform(request) // rota da requisicao
					.andExpect( status().isBadRequest() ) // - o status foi 404 badRequest
				    .andExpect( jsonPath( "errors", hasSize(4)));				    
		
	}
	
	
	@Test
	@DisplayName("Deve lançar erro ao tentar cadastrar um usuario com email já utilizado")
	public void createUsuarioWithDuplicateEmail() throws Exception {
		// instancia um usuario
		UsuarioDTO usuarioDTO = createNewUser();
		
		// transforma qualquer objeto em json
		String json = new ObjectMapper().writeValueAsString(usuarioDTO); 
		
		String mensagemErro = "Email já cadastrado.";
		
		// ao tentar salvar qualquer objeto do tipo Usuario lança uma Exception 
		BDDMockito.given( service.save(Mockito.any(Usuario.class) )).willThrow(new BussinesExcepion(mensagemErro));
		
		
		// construindo a requisição da tipo Json - rota BOOK_API
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
		.post(BOOK_API)
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.content(json);
		
		
		// executando request 
		mvc
			.perform(request) // rota da requisicao
			.andExpect( status().isBadRequest() )
		    .andExpect( jsonPath( "errors", hasSize(1)))
		    .andExpect( jsonPath( "errors[0]").value(mensagemErro));
	}
		

	
	
	private UsuarioDTO createNewUser() {
		return UsuarioDTO.builder().nome("joao").email("email@gmail.com").senha("senha").dataNascimento(data).build();
	}	

}
