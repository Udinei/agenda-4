package com.jvs.gd.baseusTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Essa classe testa a arquitetura básica da aplicação: Framework spring MVC Drivers de BD
 *  (deve ter ao menos um instalado H2, etc..). E o ambiente de testes de aceitação com 
 *  Selenium Webdriver.
 *
 */
@SpringBootTest
public class BaseUsArquiteturaAppTest {

	@Autowired
	protected Environment environment;

	private WebDriver driver;
	private WebElement element;
	private WebDriverWait wait;
	private Actions action;

	@BeforeEach
	public void openBrowser() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, 10);
		action = new Actions(driver);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	}

	@AfterEach
	public void closeBrowser() {
		driver.quit();
	}

	@Test
	public void deveExibirHaPaginaDeTesteCustomizadaDoSpringBoot() {

		String textoDeBusca = environment.getProperty("texto.de.busca.teste.arqui");
		driver.get(urlDaPaginaDeExecucaoDaApp()); // endereco da pagina de teste 
		assertTrue(existeNaPaginaHoTexto(textoDeBusca));

	}

	private String urlDaPaginaDeExecucaoDaApp() {
		String devUrl = environment.getProperty("environments.dev.url");
		String serverPort = environment.getProperty("server.porta.teste");
		String urlTeste = devUrl.concat(":").concat(serverPort);
		return urlTeste;
	}

	public boolean existeNaPaginaHoTexto(String texto) {
		boolean textoEncontrado = false;
		element = wait.until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(),'" + texto + "')]")));

		if (element.isDisplayed()) {
			//exibe por 3 segundos
			action.pause(2000).perform();
			textoEncontrado = true;
		}

		return textoEncontrado;
	}
}
