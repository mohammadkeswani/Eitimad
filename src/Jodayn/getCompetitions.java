package Jodayn;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Function;

public class getCompetitions extends Setup {

	@Test()
	public void SearchForCompetition() throws InterruptedException, IOException {
		// Create a new workbook and sheet
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Competition Data");

		// Define FluentWait
		Wait<WebDriver> fluentWait = new FluentWait<>(driver).withTimeout(Duration.ofSeconds(15))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(NoSuchElementException.class);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		
	      
        // view 24 element
        WebElement elementCountView =  driver.findElement(By.id("itemsPerPage"));
        elementCountView.click();
        Select select = new Select(elementCountView);
        select.selectByValue("24");

		

		// Click on search button
		WebElement searchButton = driver.findElement(By.id("searchBtnColaps"));
		searchButton.click();

		// Write 'برمجيات' in the search field
		WebElement searchField = driver.findElement(By.id("txtMultipleSearch"));
		searchField.click();
		searchField.sendKeys("برمجيات");

		// Wait for the tender category button to be clickable and click it
		WebElement tenderCategoryButton = fluentWait.until(new Function<WebDriver, WebElement>() {
			@Override
			public WebElement apply(WebDriver driver) {
				return driver.findElement(
						By.cssSelector("button[aria-labelledby='TenderCategory'] span.filter-option.pull-left"));
			}
		});
		tenderCategoryButton.click();

		// Wait for the dropdown to be visible and find all <li> elements
		WebElement ul = fluentWait.until(new Function<WebDriver, WebElement>() {
			@Override
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.xpath("//ul[@aria-expanded='true']"));
			}
		});
		List<WebElement> liElements = ul.findElements(By.tagName("li"));
		// Click on "المنافسات النشطة"
		if (liElements.size() > 2) {
			WebElement secondLiElement = fluentWait.until(new Function<WebDriver, WebElement>() {
				@Override
				public WebElement apply(WebDriver driver) {
					return liElements.get(2);
				}
			});
			Thread.sleep(1000);
			secondLiElement.click();
		}

		// Scroll down to the search button
		Actions actions = new Actions(driver);
		WebElement scrollForSearch = driver.findElement(By.id("searchBtn"));
		actions.moveToElement(scrollForSearch).perform();

		// Click on "لاتصالات وتقنية المعلومات"
		Thread.sleep(1000);
		driver.findElement(By.cssSelector(
				"body > div:nth-child(10) > div:nth-child(2) > div:nth-child(1) > form:nth-child(4) > div:nth-child(2) > div:nth-child(3) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(4) > div:nth-child(1) > div:nth-child(2)"))
				.click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"basicInfo\"]/div/div[4]/div/div/div/ul/li[10]")).click();

		// Click on the search button
		WebElement finalSearchButton = driver.findElement(By.id("searchBtn"));
		finalSearchButton.click();
		Thread.sleep(1000);
		// Create header row
		Row headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("Title");
		headerRow.createCell(1).setCellValue("Link href");
		headerRow.createCell(2).setCellValue("Main Activity Result");
		headerRow.createCell(3).setCellValue("Information");
		headerRow.createCell(4).setCellValue("Cost");

		int rowNum = 1;
		boolean hasNextPage = true;
		Set<String> uniquePageContent = new HashSet<>(); // To keep track of page content

		while (hasNextPage) {
			// Extract data from the current page
			List<WebElement> titleList = driver.findElements(By.cssSelector("div h3 a"));
			List<WebElement> mainActivityResult = driver.findElements(By.cssSelector("div[class$='col-12 pt-2']"));
			List<WebElement> information = driver.findElements(By.cssSelector(".tender-date.border-left"));
			List<WebElement> cost = driver.findElements(By.cssSelector(".tender-coast"));

			// Print the number of elements found
			System.out.println("Found " + titleList.size() + " elements on this page.");

			if (titleList.size() == 0) {
				System.out.println("No elements found on this page. Stopping...");
				break; // Exit loop if no elements are found
			}

			for (int i = 0; i < titleList.size(); i++) {
				Row row = sheet.createRow(rowNum++);

				WebElement title = titleList.get(i);

				// Print the text of the <h3> element
				String titleText = title.getText();
				row.createCell(0).setCellValue(titleText);

				// Try to find the href attribute of <a> element
				String hrefValue = "";
				try {
					hrefValue = title.getAttribute("href");
				} catch (NoSuchElementException e) {
					// Handle exception or leave hrefValue as an empty string
				}
				row.createCell(1).setCellValue(hrefValue);

				// Print Main Activity Result
				String mainActivityText = mainActivityResult.size() > i ? mainActivityResult.get(i).getText() : "";
				row.createCell(2).setCellValue(mainActivityText);

				String infoText = information.size() > i ? information.get(i).getText() : "";
				row.createCell(3).setCellValue(infoText);

				String costText = cost.size() > i ? cost.get(i).getText() : "";
				row.createCell(4).setCellValue(costText);
			}

			// Add current page content to uniquePageContent set
			String currentPageContent = driver.getPageSource();
			if (uniquePageContent.contains(currentPageContent)) {
				System.out.println("No change in page content. Stopping...");
				break; // Exit loop if no content change detected
			}
			uniquePageContent.add(currentPageContent);

			// Scroll down to the bottom of the page to ensure "Next" button is in view
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			Thread.sleep(1000); // Wait for a bit to make sure the scrolling is complete and elements are loaded

			// Check if "Next" button exists and is clickable
			try {
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
				WebElement nextPageButton = wait.until(new Function<WebDriver, WebElement>() {
					@Override
					public WebElement apply(WebDriver driver) {
						List<WebElement> buttons = driver
								.findElements(By.cssSelector("button[aria-label='Next'] span[aria-hidden='true']"));
						return buttons.size() > 0 ? buttons.get(0) : null;
					}
				});

				// Check if the button is disabled or not
				boolean isNextButtonDisabled = nextPageButton.getAttribute("class").contains("disabled")
						|| !nextPageButton.isEnabled();

				if (nextPageButton != null && !isNextButtonDisabled) {
					nextPageButton.click(); // Click on the "Next" button
					System.out.println("Navigated to the next page.");
					// Wait for the next page to load completely
					Thread.sleep(1000);

				} else {
					System.out.println("No 'Next' button found or it is not clickable. Stopping...");
					hasNextPage = false; // Exit the loop if there is no "Next" button
				}
			} catch (Exception e) {
				System.out.println("Error while finding 'Next' button: " + e.getMessage());
				hasNextPage = false; // Exit the loop if an error occurs
			}
		}

		// Write the output to a file
		try (FileOutputStream fileOut = new FileOutputStream("C:\\details\\console_output.xlsx")) {
			workbook.write(fileOut);
		} finally {
			workbook.close();
		}
	}
}