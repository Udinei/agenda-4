package com.jvs.gd.baseusTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Essa classe verifica se os pacotes basicos da aplicação existem. Caso não
 * existão serão gerados automaticamentes, é possivel adicionar novos pacotes
 * 
 * PACOTES BASICOS: models, config, dto, repository, resources, resources.exceptions,
 * services, services.exceptions, servicesImpl *
 */
public class BaseUsCriaPkgAppTest {

	private static final String srcMainJava = "src.main.java";
	private static final String srcTestJava = "src.test.java";
	private static final String barra = "\\";
	
	private String pathDoPackages = "";
	private List<String> listNomePacotes = informeNomeDosPacotes();
	private String pkgRaizApp = "com.jvs.gd"; // nome também usado no pkg de teste 
		
	
	private List<String> informeNomeDosPacotes() {
		List<String> lpk = new ArrayList<>();
		lpk.add("baseus");
		lpk.add("models");
		lpk.add("config");
		lpk.add("dto");
		lpk.add("repository");
		lpk.add("resources");
		lpk.add("resources.exceptions");
		lpk.add("services");
		lpk.add("services.exceptions");
		lpk.add("servicesImpl");
		return lpk;
	}

	@Test
	public void criaPacotesBasicosDaApp() throws IOException {
		criarPacotesBasicosApp(srcMainJava); 
		criarPacotesBasicosApp(srcTestJava);
		
	}
	
	
	
	public void criarPacotesBasicosApp(String pastaRaiz) throws IOException {
		
		listNomePacotes = informeNomeDosPacotes();
		pathDoPackages = pathDeCriacaoDosPackages(pastaRaiz);
		
		System.out.println("\nCriando Pacotes em ...: " + pathDoPackages);
		criaPackages(listNomePacotes, pathDoPackages);
		
	}

	private void criaPackages(List<String> listNomePacotes, String pathDoPackages) {
		List<String> listPkgNaoCriados = new ArrayList<>();
		List<String> listPkgCriados = new ArrayList<>();

		String pathTmp = "";
		for (String nomePkg : listNomePacotes) {

			nomePkg = nomePkg.replace(".", barra);
			
			// acresenta "Test" no nome dos pacotes de test 
			if(pathDoPackages.contains("test")) {
		       nomePkg = nomePkg+"Test";	
			}
			
			pathTmp = pathDoPackages + nomePkg;
						
            //System.out.println(pathDoPackages);
            
			// criar pacotes do projeto (pastas)
			boolean file = new File(pathTmp).mkdir();

			if (!file) {
				listPkgNaoCriados.add(nomePkg);
			} else {
				listPkgCriados.add(nomePkg);
			}
		}

		if (listPkgNaoCriados != null) {
			System.out.println("Lista de pacote(s) não criados. Possívelmente já existem!: " + listPkgNaoCriados);
		}

		if (!listPkgCriados.isEmpty()) {
			System.out.println("Lista de pacote(s) criados: " + listPkgCriados);
		}

	}

	private String pathDeCriacaoDosPackages(String pathTarget) {
		// Path raiz da aplicacao 
		Path pathProjeto = Paths.get("");
		Path pathAbsProjeto = pathProjeto.toAbsolutePath();

		pathTarget = pathTarget.replace(".", "\\");
		pkgRaizApp = pkgRaizApp.replace(".", "\\");

		String pathDoPackages = pathAbsProjeto + barra + pathTarget + barra + pkgRaizApp + barra;
		return pathDoPackages;
	}

}