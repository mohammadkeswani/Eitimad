package Jodayn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeTest;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class Setup {
		WebDriver driver = new ChromeDriver();
		String url = "https://tenders.etimad.sa/Tender/AllTendersForVisitor?PageNumber=1";
		String [] SearchType = {"برمجيات"};
		int o =0;
		ExtentReports extent;
 

		

		//Create report
		public static ExtentReports report() {

			 String path = System.getProperty("user.dir") + "/reports/index.html";
				ExtentSparkReporter reporter = new ExtentSparkReporter(path);
				reporter.config().setReportName("Mohammad Alkeswani Report");
				reporter.config().setDocumentTitle("Title Alkeswani");
				ExtentReports extent = new ExtentReports();
				extent.attachReporter(reporter);
				extent.setSystemInfo("Tester", "Alkeswani");
				return extent;
			
		}
		

		
		
		@BeforeTest
		public void openWebSite() {
			
		   
			
			driver.manage().window().maximize();
			driver.get(url);
		}
}
