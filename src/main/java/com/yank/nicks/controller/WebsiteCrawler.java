package com.yank.nicks.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByClassName;
import org.openqa.selenium.By.ByCssSelector;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.amazonaws.services.kinesisanalytics.model.Input;
import com.yank.nicks.entidade.Nick;

public class WebsiteCrawler {
	private WebDriver driver;

	public WebsiteCrawler() {
		// Localização do chromedriver
		System.setProperty("webdriver.chrome.driver", "/programas/chromedriver.exe");
		driver = new ChromeDriver();

	}

	public void obtendoNomeCpfNicks() {

		List<Nick> nicks = new ArrayList<Nick>();

		driver.navigate().to("https://www.4devs.com.br/gerador_de_nicks");

		// Preenche o campo método o valor random
		Select metodo = new Select(driver.findElement(By.id("method")));
		metodo.selectByVisibleText("Aleatório");

		// Preenche nº de nicks
		driver.findElement(By.id("quantity")).clear();
		driver.findElement(By.id("quantity")).sendKeys("50");

		// Preenche número de letras
		Select letras = new Select(driver.findElement(By.id("limit")));
		letras.selectByVisibleText("8");

		// Gera os nomes dos nicks
		driver.findElement(By.id("bt_gerar_nick")).click();

		String xPath = "//div[@id = 'nicks']/ul/li/span";
		List<WebElement> results = (new WebDriverWait(driver, 50))
				.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(xPath)));

		for (WebElement webElement : results) {
			Nick nickNome = new Nick();
			String nome = webElement.getText();
			nickNome.setNome(nome);
			nicks.add(nickNome);
		}

		driver.navigate().to("https://www.4devs.com.br/gerador_de_cpf");

		for (Nick nick : nicks) {
			driver.findElement(By.id("bt_gerar_cpf")).click();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String cpf = driver.findElement(By.id("texto_cpf")).getText();			
			nick.setCpf(cpf);
		}

		gravarArquivo(nicks);

		close();
	}

	public void gravarArquivo(List<Nick> nicks) {

		// Cria arquivo
		File file = new File("C:/programas/teste.txt");

		// Se o arquivo nao existir, ele gera
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			// Prepara para escrever no arquivo
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			// Escreve e fecha arquivo

			for (Nick nick : nicks) {
				bw.write(nick.getNome() + ";" + nick.getCpf());
				bw.newLine();
				bw.flush();
			}
			bw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close() {
		// Fechar após completar
		driver.close();
		driver.quit();
	}
}
