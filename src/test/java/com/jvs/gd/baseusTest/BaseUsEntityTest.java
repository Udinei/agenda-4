package com.jvs.gd.baseusTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.SerializationUtils;

import com.jvs.gd.Agenda4Application;

/**
  Essa classe valida nas entidades e na superclasse abstractModel a existencia de elementos
  basicos para seu funcionamento na arquitetura tais como: anotations, serializacao,
  metodos get, set, buider, metodo construtor padrao, metodos construtor com todos os
  atributos etc..Toda entidade de negocio do sistema deve herdar essa classe.
*/

public class BaseUsEntityTest {

	@Autowired
	private ApplicationContext applicationContext;

	private Object objeto = null; // classe que sera validada
	protected boolean testaSuperClass;
	AnnotationUtils utils;

	// lista de atributos projetados
	protected List<String> atE = new ArrayList<>(); // atributos da  entity
	protected List<String> atS = new ArrayList<>(); // atributos da super class

	protected Class<Agenda4Application> auditaMongoApp;

	public BaseUsEntityTest(Object objeto) {
		super();
		this.objeto = objeto;
	}

	/** Esse metodo testa se a classe entidade esta implementada com os atributos basicos projetados  
	 *  e os metodos do lombok */
	@SuppressWarnings("unchecked")
	@Test
	public void TestaImplementacaoEntity() {

		assertTrue(!atE.isEmpty());// Teste não passa se a lista de Atributos Projetados estiver vazia
		List<String> listAtributosProjetados = atE;

		List<String> listAtributosNaEntity = getListAtributosImplentadosNaEntity(objeto);

		boolean atributosImplementados = todosAtributosProjetasdoForamImplementados(listAtributosProjetados,
				listAtributosNaEntity);
		assertTrue(atributosImplementados);

		boolean possuiMetodosLombok = metodosBasicosPadraoDoLombokImplementadosNaEntity(objeto);
		assertTrue(possuiMetodosLombok);

		entityHeSerializavelDeserializavel(objeto);

	}

	/** Esse metodo testa se a classe de auditoria AbstractModel foi implementada com os atributos
	 *  e annotations basicos */
	@Test
	public void TestaSuperClasseAbstractModel() {

		if (testaSuperClass) {
			assertTrue(!atS.isEmpty());// A lista de Atributos Projetados Abstrac Model deve estar preenchida
			List<String> listAtributosProjetados = atS; // A lista de atributos projetados da superclasse

			objeto = obtemSuperClasse(objeto);

			List<String> listAtributosNaEntity = getListAtributosImplentadosNaSuper((Class) objeto);

			boolean atributosImplementados = todosAtributosProjetasdoForamImplementados(listAtributosProjetados,
					listAtributosNaEntity);
			assertTrue(atributosImplementados);

		}
	}

	@Test
	public void annotationDeAuditoriaDoMongoPresente() {
		AnnotatedElement element =auditaMongoApp;
		Annotation[] tmp = element.getAnnotations();
				
		for (Annotation anot : tmp) {
			if (anot.toString().contains("EnableMongoAuditing")) {
				assertTrue(true);
			}
		}
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

	protected List<String> getListAtributosImplentadosNaEntity(Object classe) {
		List<String> atributosNaEntidade = new ArrayList<>();
		boolean temSerialVersionUID = false;

		String declaracaoFinal = "";
		String anotacoesFormatadas = "";
		String atributoFormatado = "";

		for (Field atributo : getListAtributosImplementadosNaSuperClasse(classe.getClass())) {

			anotacoesFormatadas = formataAnotacaoDoAtributo(getAnnotacaoDoAtributo(atributo));

			atributoFormatado = formataDeclaracaoAtributo(atributo);

			declaracaoFinal = anotacoesFormatadas + atributoFormatado;

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

	protected List<String> getListAtributosImplentadosNaSuper(Object classe) {
		List<String> atributosNaEntidade = new ArrayList<>();

		String declaracaoFinal = "", anotacoesFormatadas = "", atributoFormatado = "";

		for (Field atributo : getListAtributosImplementadosNaSuperClasse((Class) classe)) {

			anotacoesFormatadas = formataAnotacaoDoAtributo(getAnnotacaoDoAtributo(atributo));

			atributoFormatado = formataDeclaracaoAtributo(atributo);

			declaracaoFinal = anotacoesFormatadas + atributoFormatado;

			atributosNaEntidade.add(declaracaoFinal);
		}

		return atributosNaEntidade;
	}

	public String formataAnotacaoDoAtributo(List listAnotacoes) {

		String tmp = "", tmp2 = "", arroba = "", anotacoes = "";

		for (int i = 0; i < listAnotacoes.size(); i++) {

			tmp = listAnotacoes.get(i).toString(); // TODO: Implementar futuramente leitura das opçoes das annotations
			arroba = tmp.substring(1, 2);

			tmp2 = tmp.substring(1, tmp.lastIndexOf("("));

			tmp2 = tmp2.substring(tmp2.lastIndexOf(".") + 1);

			anotacoes = anotacoes + arroba + tmp2 + " ";

		}

		return anotacoes;
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

		List<String> newList = new ArrayList<>();

		for (int i = 0; i < atributosNaEntity.size(); i++) {

			// pega um atributo que existe na entidade
			String atributoAtual = atributosNaEntity.get(i);

			for (String string : atributosProjetados) {

				// coloca na lista, se atributo implementado for igual a dos atributos projetado 
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

	public Class obtemSuperClasse(Object objeto) {

		Class superClasseAuditoria = objeto.getClass().getSuperclass();
		return superClasseAuditoria;

	}

	// retorna lista de abributos da classe
	private List<Field> getListAtributosImplementadosNaSuperClasse(Class clazz) {

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

	@SuppressWarnings("unchecked")
	public List getAnnotacaoDoAtributo(Field field) {
		List tmp = new ArrayList<>();

		Annotation[] annotations = field.getDeclaredAnnotations();

		for (Annotation annotation : annotations) {
			tmp.add(Arrays.asList(annotation.toString()));
		}

		return tmp;
	}

	@SuppressWarnings("unchecked")
	public static boolean compareList(List ls1, List ls2) {
		return ls1.containsAll(ls2) && ls1.size() == ls2.size() ? true : false;
	}

	Function<String, String> reverse = s -> new StringBuilder(s).reverse().toString();

	static String reverse1(String original) {
		return new StringBuilder(original).reverse().toString();
	}

}