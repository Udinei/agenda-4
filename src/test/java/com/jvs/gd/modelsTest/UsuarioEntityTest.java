package com.jvs.gd.modelsTest;

import java.util.List;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AnnotationUtils;

import com.jvs.gd.Agenda4Application;
import com.jvs.gd.baseusTest.BaseUsEntityTest;
import com.jvs.gd.models.Usuario;

/**
 * 
 * @author Udinei
 * Essa classe testa as entidades de dominio, e sua super classe BaseUsAbstractModel
 * Testa a implementação dos atributos de projeto da entidade.
 * Testa os metodos implementados pelo lombok suas anotations:ex:  @Data @Builder @AllArgsConstructor @NoArgsConstructor
 * Serão validados somente atributos com annotaions sem passagem de parametros
 *
 */

@SpringBootTest
public class UsuarioEntityTest extends BaseUsEntityTest {

	static Usuario usuario = new Usuario();

	// o atributo Id deve ser implementado pela superclasse AbstractModel
	public List<String> atributosProjetadosDaEntity() {
		
		atE.add("@Id private String id");
		atE.add("private String nome");
		atE.add("private String senha");
		atE.add("private String email");
		
		return atE;
	}

	public List<String> atributosProjetadosDaSuperAbstractModel() {
		
		atS.add("@CreatedDate private Date createdDate");
		atS.add("@LastModifiedDate private Date lastModifiedDate");
		
		return atS;
	}

	public UsuarioEntityTest() {
		super(usuario);
		
		atE = atributosProjetadosDaEntity();
		atS = atributosProjetadosDaSuperAbstractModel();
		testaSuperClass = true;
		auditoriHabilitada = Agenda4Application.class;
	}

}
