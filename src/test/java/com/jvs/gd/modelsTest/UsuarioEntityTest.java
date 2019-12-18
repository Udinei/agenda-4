package com.jvs.gd.modelsTest;

import java.util.List;

import org.springframework.boot.test.context.SpringBootTest;

import com.jvs.gd.baseusTest.BaseUsEntityTest;
import com.jvs.gd.models.Usuario;


/**
 * 
 * @author Udinei
 * Essa classe testa as entidades de dominio, quanto a implementação dos atributos de projeto da entidade.
 *  Quanto a implementação dos metodos basicos do lombok realizados por suas anotations: @Data @Builder @AllArgsConstructor @NoArgsConstructor
 *  e o mapeamento da entidade para uma entidade de BD (@Entity, @Document) controlada pelo jpa.
 *
 */
@SpringBootTest
public class UsuarioEntityTest extends BaseUsEntityTest  {
	
	static Usuario usuario = new Usuario();
	
		
	// o atributo Id deve ser implementado pela superclasse AbstractModel
	public List<String> atributosProjetadosDaEntity() {
		
		at.add("private String nome");
		at.add("private String senha");
		at.add("private String email");
		return at;
    }
	
	
	public List<String> atributosProjetadosDaSuperAbstractModel() {
		annot.add("@Id");
		atSam.add("private String id");
		
		annot.add("@CreatedDate");
		atSam.add("private Date createdDate");
		
		annot.add("@LastModifiedDate");
		atSam.add("private Date lastModifiedDate");
		return atSam;
    }

	
	public UsuarioEntityTest() {
		   super(usuario);
		   at = atributosProjetadosDaEntity();
		   atSam = atributosProjetadosDaSuperAbstractModel();
		   testaSuperClass = true;
	} 
	
	
}
