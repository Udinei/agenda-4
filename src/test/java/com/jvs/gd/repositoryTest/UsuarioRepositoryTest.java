package com.jvs.gd.repositoryTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.jvs.gd.baseusTest.BaseUsCrudBasicoEntityTest;
import com.jvs.gd.models.Usuario;
import com.jvs.gd.repository.UsuarioRepository;

/**
 * Classe de teste de integração da entidade UsuarioRepository. Esse tipo de
 * testes utiliza recursos externos a aplicação no caso o BD postgresql e
 * qualquer outro recurso. Metodos de testes nao possuem retorno.</br>
 * No caso deste teste sera utilizado o banco Nosql mongoDB
 * @AutoConfigureDataMongo
 * 
 */
@SpringBootTest
@AutoConfigureDataMongo
@ActiveProfiles("test")
public class UsuarioRepositoryTest extends BaseUsCrudBasicoEntityTest {

	@Autowired
	private UsuarioRepository entityRepository;

	private Optional<Usuario> entityRecuperadaOp;

	private String editarAtributo = "nome";

	private Usuario entity = new Usuario();

	private Usuario entidadeCriada = criarEntidade();

	// cria e preenche os dados da entidade, esse metodo deve ser alterado conforme a entidade
	public Usuario criarEntidade() {
		return Usuario.builder().nome("UsuarioTest").email("usuario@email.com").senha("123").build();
	}
		
	
	 /** -------------------------------------
	           Metodos de negocio - UC     
	   --------------------------------------*/

	
	@Test
	public void deveVerificarExistenciaDeUmEmail() {
		// cenario 1 - O usuario existe
		entity = entidadeCriada;
		entityRepository.save(entity);

		// acao/execucao
		boolean result = entityRepository.existsByEmail("usuario@email.com");

		// verificacao
		assertThat(result).isNotNull();
		entityRepository.delete(entity);
	}

	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {
		// acao
		boolean result = entityRepository.existsByEmail("usuario@email.com");

		// verificacao
		assertThat(result).isFalse();
	}

	
	@Test
	public void deveBuscarUmUsuarioPorEmail() {
		// cenario
		entity = entidadeCriada;
		entityRepository.save(entity);

		// acao
		entityRecuperadaOp = entityRepository.findByEmail("usuario@email.com");

		// verificacao
		assertThat(entityRecuperadaOp.isPresent()).isTrue();
		entityRepository.delete(entity);
	}

	
	@Test
	public void deveRetornarVazioAoBuscarUmUsuarioPorEmailQuandoNaoExisteNaBase() {
		// cenario
		// a base ja esta vazia

		// acao
		entityRecuperadaOp = entityRepository.findByEmail("usuario@email.com");

		// verificacao
		assertThat(entityRecuperadaOp.isPresent()).isFalse();
	}
	
	

	/** ---------------------------------------
              Metodos padrão - CRUD
     --------------------------------------- */
  
	@Test
	public void crudBasicoEntity() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
				
		deveSalvarEntidade(entity, entidadeCriada, entityRepository);
		/** Unico metodo a ser alterado. O nome do campo e seu valor */
		deveEditarEntidadeAtributo(editarAtributo, entity, entidadeCriada, entityRepository);
	    deveApagarUmaEntidade(entity, entidadeCriada, entityRepository, entityRecuperadaOp);
		deveBuscarUmaEntidadePorId(entity, entidadeCriada, entityRepository, entityRecuperadaOp);
	}

}
