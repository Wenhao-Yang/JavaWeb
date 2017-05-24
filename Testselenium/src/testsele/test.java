package testsele;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

public class test {
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@Before
	public void setUp() throws Exception {
		System.setProperty("webdriver.firefox.bin", "E:/firefox/firefox.exe");
		driver = new FirefoxDriver();
		baseUrl = "http://121.193.130.195:8080";
		
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void test() throws Exception {
		
		String[] str = { "" };
		String inString = "";
		String tmpString = "";
		String[] sourceStrArray;
		int num=0;
		File inFile = new File(
				"C://Users/mjm/Desktop/作业/软件测试/lab2/inputgit.csv");//读取文件
		try {
			BufferedReader reader = new BufferedReader(new FileReader(inFile));
			CsvReader creader = new CsvReader(reader, ',');
			while (creader.readRecord()) {
				inString = creader.getRawRecord();// 读取一行数据
				int maxSplit = 3;
				sourceStrArray = inString.split(",", maxSplit);//按照，分割字符串
				String a = sourceStrArray[0];//获取到学号
				if (a.length() > 6) {
					String b = a.substring(a.length() - 6, a.length());//获取学号后六位为密码
					driver.get(baseUrl + "/");
					driver.findElement(By.id("pwd")).clear();
					driver.findElement(By.id("name")).clear();
					driver.findElement(By.id("name")).sendKeys(sourceStrArray[0]);
					driver.findElement(By.id("pwd")).sendKeys(b);
					driver.findElement(By.id("submit")).click();
					assertEquals(sourceStrArray[2],driver.findElement(By.xpath("//tbody/tr[3]/td[2]")).getText());
				}
				
			}
			creader.close();
			
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}

	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}
}
