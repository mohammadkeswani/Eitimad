package Jodayn;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class Setup {

	WebDriver driver = new ChromeDriver();
	String url = "https://tenders.etimad.sa/Tender/AllTendersForVisitor?PageNumber=1";
	String[] SearchType = { "برمجيات" };
	int o = 0;

	public static void main(String[] args) {
	}

	@BeforeTest
	public void openWebSite() {
		driver.manage().window().maximize();
		driver.get(url);

	}

	// Create report
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

}
