package com.jvs.gd.baseusTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.aggregation.StringOperators.IndexOfBytes.SubstringBuilder;
import org.springframework.util.SerializationUtils;

/**
  Essa classe valida nas entidades e na superclasse abstractModel a existencia de elementos
  basicos para seu funcionamento na arquitetura tais como: anotations, serializacao,
  metodos get, set, buider, metodo construtor padrao, metodos construtor com todos os
  atributos etc..Toda entidade de negocio do sistema deve herdar essa classe.
*/

public class BaseUsEntityTest {

	private Object objeto = null; // classe que sera validada
	private String pkgSpring = "@org.springframework.data.annotation."; //nome do pacote basico para as annotations do springframework
	private boolean testaSuperClass;

	// lista de atributos projetados
	protected List<String> at = new ArrayList<>(); // atributos da  entity
	protected List<String> atSam = new ArrayList<>(); // atributos da super class
	protected List<String> annot = new ArrayList<>(); // anotation da supper class

	public BaseUsEntityTest(Object objeto) {
		super();
		this.objeto = objeto;
	}

	/** Esse metodo testa se a classe entidade esta implementada com os atributos basicos projetados e as 
	 *  e os metodos do lombok */
	@Test
	public void TestaImplementacaoEntity() {

		if (testaSuperClass) {

			assertTrue(!at.isEmpty());// Teste não passa se a lista de Atributos Projetados estiver vazia
			List<String> listAtributosProjetados = at;

			List<String> listAtributosNaEntity = listAtributosImplentadosNaEntity(objeto);

			boolean atributosImplementados = todosAtributosProjetasdoForamImplementados(listAtributosProjetados,
					listAtributosNaEntity);
			assertTrue(atributosImplementados);

			boolean possuiMetodosLombok = metodosBasicosPadraoDoLombokImplementadosNaEntity(objeto);
			assertTrue(possuiMetodosLombok);

			entityHeSerializavelDeserializavel(objeto);
		}

	}

	/** Esse metodo testa se a classe de auditoria AbstractModel foi implementada com os atributos
	 *  e annotations basicos */
	@Test
	public void TestaSuperClasseAbstractModel() {

		assertTrue(!atSam.isEmpty());// A lista de Atributos Projetados Abstrac Model deve estar preenchida
		List<String> listAtributosProjetados = atSam; // A lista de atributos projetados da superclasse

		assertTrue(!annot.isEmpty());
		List<String> ListAnnotationProjetados = annot; // lista de annotation projetados

		objeto = obtemSuperClasseAbstracModel(objeto);
		List<Field> listAtributosNaEntity = getListAtributoDaClasse((Class) objeto);

		List<String> listAtributosNaEntityFormatados = formataListaDeFields(listAtributosNaEntity);

		boolean atributosImplementados = todosAtributosProjetasdoForamImplementados(listAtributosProjetados,
				listAtributosNaEntityFormatados);
		assertTrue(atributosImplementados);

		boolean annotationSuperImplementadas = todosAnnotationDaSuperForamImplementados(annot, listAtributosNaEntity);
		assertTrue(annotationSuperImplementadas);

	}

	private boolean todosAnnotationDaSuperForamImplementados(List<String> annotsProjetadas,
			List<Field> listAtributosNaEntity) {
		List anotacoesDosAtributos = new ArrayList<>();

		for (Field field : listAtributosNaEntity) {
			anotacoesDosAtributos.addAll(obtemNomeAnotacaoSobreField(field));

		}

		List<String> newList = new ArrayList<>();

		for (int i = 0; i < anotacoesDosAtributos.size(); i++) {

			String annotFiedEntity = (String) anotacoesDosAtributos.get(i);

			for (String annotPrj : annotsProjetadas) {
				annotPrj = annotPrj.substring(1);

				annotPrj = pkgSpring + annotPrj + "()"; // TODO - alterar em caso de mudanca de pacotes do spring

				if (annotPrj.contains(annotFiedEntity)) {
					newList.add(annotPrj);
				}
				;
			}

		}

		return compareList(newList, anotacoesDosAtributos);
	}

	@SuppressWarnings("unchecked")
	public static boolean compareList(List ls1, List ls2) {
		return ls1.containsAll(ls2) && ls1.size() == ls2.size() ? true : false;
	}

	private List<String> formataListaDeFields(List<Field> listAtributosNaEntity) {
		List<String> listAtributosNaEntityFormatados = new ArrayList<>();

		for (Field object : listAtributosNaEntity) {
			listAtributosNaEntityFormatados.add(formataDeclaracaoAtributo(object));

		}
		return listAtributosNaEntityFormatados;
	}

	public void entityHeSerializavelDeserializavel(Object objeto) {
		byte[] data = SerializationUtils.serialize(objeto);
		Object objNew = SerializationUtils.deserialize(data);

		assertEquals(objNew.getClass().getName(), objeto.getClass().getName());
	}

	@SuppressWarnings("unchecked")
	public boolean metodosBasicosPadraoDoLombokImplementadosNaEntity(Object objeto) {

		// lista de metodos basicos implementados pelo lombok
		List<String> mts = new ArrayList<>();
		mts.add("equals");
		mts.add("toString");
		mts.add("hashCode");
		mts.add("builder");
		mts.add("canEqual");

		List<String> listMetodosPadrao = mts;
		List<String> listMetodosImplementados = new ArrayList<>();

		for (Method metodo : objeto.getClass().getDeclaredMethods()) {

			for (int i = 0; i < listMetodosPadrao.size(); i++) {
				if (metodo.getName().equals(listMetodosPadrao.get(i))) {
					listMetodosImplementados.add(metodo.getName());
				}
			}
		}

		return compareList(listMetodosPadrao, listMetodosImplementados);

	}

	protected List<String> listAtributosImplentadosNaEntity(Object classe) {
		List<String> atributosNaEntidade = new ArrayList<>();
		boolean temSerialVersionUID = false;

		String modif = "";
		String modif_static = "";
		String modif_final = "";
		String tipoAtributo = "";
		String nomeAtributo = "";
		String declaracaoFinal = "";

		for (Field atributo : getListAtributoDaClasse(classe.getClass())) {

			declaracaoFinal = formataDeclaracaoAtributo(atributo);

			if (atributo.toString().contains("serialVersionUID")) {
				temSerialVersionUID = true;
			}

			atributosNaEntidade.add(declaracaoFinal);
		}

		// falha se nao tiver campo serial versionUID
		if (!temSerialVersionUID) {
			fail("Campo serialVersionUID não implementado");
		}

		return atributosNaEntidade;
	}

	// transforma o field com pacote em apenas: O modificador, o tipo e nome exe: private String id
	private String formataDeclaracaoAtributo(Field atributo) {

		String declaracaoFinal = "";

		String modif;
		String modif_static;
		String modif_final;
		String tipoAtributo;
		String nomeAtributo;
		if (!atributo.toString().contains("static final")) {
			String[] vetor_campos = atributo.toString().split(" ");
			modif = vetor_campos[0];
			tipoAtributo = vetor_campos[1].substring(vetor_campos[1].lastIndexOf(".") + 1);
			nomeAtributo = vetor_campos[2].substring(vetor_campos[2].lastIndexOf(".") + 1);
			declaracaoFinal = modif + " " + tipoAtributo + " " + nomeAtributo;
			//System.out.println(" " + declaracaoFinal);

		}

		if (atributo.toString().contains("static final")) {
			String[] vetor_campos_static = atributo.toString().split(" ");
			modif = vetor_campos_static[0];
			modif_static = vetor_campos_static[1];
			modif_final = vetor_campos_static[2];
			tipoAtributo = vetor_campos_static[3];
			nomeAtributo = vetor_campos_static[4].substring(vetor_campos_static[4].lastIndexOf(".") + 1);
			declaracaoFinal = modif + " " + modif_static + " " + modif_final + " " + tipoAtributo + " " + nomeAtributo;
			//System.out.println(" " + declaracaoFinal);

		}
		return declaracaoFinal;
	}

	protected boolean todosAtributosProjetasdoForamImplementados(List<String> atributosProjetados,
			List<String> atributosNaEntity) {
		boolean atributosImplementados = false;

		List<String> newList = new ArrayList<>();

		for (int i = 0; i < atributosNaEntity.size(); i++) {

			// pega um atributo que existe na entidade
			String atributoAtual = atributosNaEntity.get(i);

			for (String string : atributosProjetados) {

				if (string.equals(atributoAtual)) {
					newList.add(atributoAtual);
				}
			}
		}

		return compareList(newList, atributosProjetados);
	}

	public void deveTerUmaAnotacaoDeMapeamentoDeEntityDeBD(Object objeto) {
		boolean temMapeamentoBDNaEntidade = false;

		for (Annotation anotation : objeto.getClass().getAnnotations()) {

			if (anotation.toString().contains("mongodb")) {
				temMapeamentoBDNaEntidade = true;

			} else if (anotation.toString().contains("Entity")) {
				temMapeamentoBDNaEntidade = true;
			}
		}

		assertTrue(temMapeamentoBDNaEntidade);
	}

	public Object obtemSuperClasseAbstracModel(Object objeto) {

		Class superClasseAuditoria = objeto.getClass().getSuperclass();
		return superClasseAuditoria;

	}

	// retorna lista de abributos da classe
	private List<Field> getListAtributoDaClasse(Class clazz) {

		List<Field> fields = new ArrayList<Field>();
		List<Field> listNamefields = new ArrayList<>();

		fields.addAll(Arrays.asList(clazz.getDeclaredFields()));

		for (Field field : fields) {
			listNamefields.add(field);

		}

		getListAnottationSpringSuperAbstracModel(clazz);
		return listNamefields;

	}

	// retorna lista de abributos da classe
	private List<Field> getListAnottationSpringSuperAbstracModel(Class clazz) {

		List<Field> fields = new ArrayList<Field>();
		List<Field> listNamefields = new ArrayList<>();

		fields.addAll(Arrays.asList(clazz.getDeclaredFields()));

		for (Field field : fields) {
			listNamefields.add(field);
		}

		return listNamefields;

	}

	public List obtemNomeAnotacaoSobreField(Field field) {
		List tmp = new ArrayList<>();

		Annotation[] annotations = field.getDeclaredAnnotations();

		for (Annotation annotation : annotations) {
			tmp.add(annotation.toString());

		}

		return tmp;
	}

}