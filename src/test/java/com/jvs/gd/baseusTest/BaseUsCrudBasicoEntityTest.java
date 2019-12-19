package com.jvs.gd.baseusTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Essa classe implementa os metodos basicos de CRUD de uma entidade. Permitindo
 * descobrir potenciais erros de implementação das entidades ou do repository da
 * entidade criadas
 * 
 */
public class BaseUsCrudBasicoEntityTest {

	public BaseUsCrudBasicoEntityTest() {
		super();
	}

	public void deveSalvarEntidade(Object entity, Object entidadeCriada, MongoRepository entityRepository) {
		// cenario - existe uma entidade com dados
		entity = entidadeCriada; //criarEntidade();

		// acao/execucao
		entity = entityRepository.save(entity);
		String id = invokeDaEntityMetodoGet(entity, "id");

		// verificacao
		assertThat(id).isNotNull();
		entityRepository.delete(entity); // apaga entity do BD
	}

	public void deveApagarUmaEntidade(Object entity, Object entidadeCriada, MongoRepository entityRepository,
			Optional entityRecuperadaOp) {
		// cenario
		entity = entidadeCriada; //criarEntidade();
		entity = entityRepository.save(entity);

		// acao/execucao
		entityRepository.delete(entity); // limpa BD
		String id = invokeDaEntityMetodoGet(entity, "id");

		// verificacao
		entityRecuperadaOp = entityRepository.findById(id);
		assertFalse(entityRecuperadaOp.isPresent());
	}

	public void deveBuscarUmaEntidadePorId(Object entity, Object entidadeCriada, MongoRepository entityRepository,
			Optional entityRecuperadaOp) {

		// cenario
		entity = entidadeCriada; // criarEntidade();
		entity = entityRepository.save(entity);

		// acao/execucao
		String id = invokeDaEntityMetodoGet(entity, "id");
		entityRecuperadaOp = entityRepository.findById(id);

		// verificacao
		assertTrue(entityRecuperadaOp.isPresent());
		entityRepository.delete(entity); // limpa BD

	}

	public String invokeDaEntityMetodoGet(Object entity, String atributo) {
		String tmp = "";
		// transforma primeiro caracter do atributo em maiusculo
		atributo = atributo.substring(0, 1).toUpperCase() + atributo.substring(1);

		for (Method mtd : entity.getClass().getMethods()) {
			// executa o metodo set da entidade e atributo passado como parametro
			if (mtd.toString().contains("get" + atributo)) {
				try {

					tmp = (String) mtd.invoke(entity);

				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}

			}

		}

		return tmp;
	}

	public void deveEditarEntidadeAtributo(String atributo, Object entity, Object entidadeCriada,
			MongoRepository entityRepository) {

		// cenario
		entity = entidadeCriada; // criarEntidade();
		entity = entityRepository.save(entity);

		// acao/execucao
		//é o mesmo que fazer entity.set<NomeAtributo>("nomeAlterado");
		invokeDaEntityMetodoSet(atributo, "Alt", entity);
		entity = entityRepository.save(entity);

		// verificacao
		//assertTrue("nomeAlterado".equals(entity.getNome()));
		String nome = invokeDaEntityMetodoGet(entity, atributo);

		assertTrue("Alt".equals(nome));
		entityRepository.delete(entity); // limpa BD
	}

	private void invokeDaEntityMetodoSet(String atributo, String valor, Object entity) {

		// transforma primeiro caracter do atributo em maiusculo
		atributo = atributo.substring(0, 1).toUpperCase() + atributo.substring(1);

		// executa o metodo set da entidade e atributo passado como parametro
		for (Method mtd : entity.getClass().getMethods()) {

			if (mtd.toString().contains("set" + atributo)) {
				try {
					mtd.invoke(entity, valor);

				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}

		}
	}

}