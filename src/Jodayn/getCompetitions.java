package Jodayn;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.testng.annotations.Test;

public class getCompetitions extends Setup {
	



    @Test()
    public void SearchForCompetition() throws InterruptedException, IOException {
    
   	 // Create a new workbook and sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Competition Data");        
                
     // Define FluentWait
        Wait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(15))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);
        
        // view 24 element
        WebElement elementCountView =  driver.findElement(By.id("itemsPerPage"));
        elementCountView.click();
        Select select = new Select(elementCountView);
        select.selectByValue("24");

        
        // Click on search button
        WebElement searchButton = driver.findElement(By.id("searchBtnColaps"));
        searchButton.click();
        Thread.sleep(2000);        
        // Write 'برمجيات' in the search field
        WebElement searchField = driver.findElement(By.id("txtMultipleSearch"));
        searchField.click();
        searchField.sendKeys("برمجيات");

        // Wait for the tender category button to be clickable and click it
        WebElement tenderCategoryButton = fluentWait.until(new Function<WebDriver, WebElement>() {
            @Override
            public WebElement apply(WebDriver driver) {
                return driver.findElement(By.cssSelector("button[aria-labelledby='TenderCategory'] span.filter-option.pull-left"));
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

        // Check if the list has at least 3 elements
        if (liElements.size() > 2) {
            // Wait for the specific <li> element to be clickable and click it
            WebElement secondLiElement = fluentWait.until(new Function<WebDriver, WebElement>() {
                @Override
                public WebElement apply(WebDriver driver) {
                    return liElements.get(1);
                }
            });
            secondLiElement.click();
        }

        // Scroll down to the search button
        Actions actions = new Actions(driver);
        WebElement scrollForSearch = driver.findElement(By.id("searchBtn"));
        actions.moveToElement(scrollForSearch).perform();

        // Click on "لاتصالات وتقنية المعلومات"
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.cssSelector(
                "body > div:nth-child(10) > div:nth-child(2) > div:nth-child(1) > form:nth-child(4) > div:nth-child(2) > div:nth-child(3) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(4) > div:nth-child(1) > div:nth-child(2)"))
                .click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.xpath("//*[@id=\"basicInfo\"]/div/div[4]/div/div/div/ul/li[10]")).click();

        // Click on the search button
        WebElement finalSearchButton = driver.findElement(By.id("searchBtn"));
        finalSearchButton.click();

        // Wait for the final search button to be clickable and click it
        WebElement FinalSearchButton = fluentWait.until(new Function<WebDriver, WebElement>() {
            @Override
            public WebElement apply(WebDriver driver) {
                return driver.findElement(By.id("searchBtn"));
            }
        });
        FinalSearchButton.click();
        
        
        // get the data 
    //    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div h3 a")));
        List<WebElement> titleList = driver.findElements(By.cssSelector("div h3 a"));
        List<WebElement> mainActivityResult = driver.findElements(By.cssSelector("div[class$='col-12 pt-2']"));
        List<WebElement> Information = driver.findElements(By.cssSelector(".tender-date.border-left"));
        List<WebElement> cost = driver.findElements(By.cssSelector(".tender-coast"));
        
        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Title");
        headerRow.createCell(1).setCellValue("Link href");
        headerRow.createCell(2).setCellValue("Main Activity Result");
        headerRow.createCell(3).setCellValue("Information");
        headerRow.createCell(4).setCellValue("Cost");

        int rowNum = 1;
        for (int i = 0; i < titleList.size(); i++) {
            Row row = sheet.createRow(rowNum++);

            WebElement title = titleList.get(i);

            // Print the text of the <h3> element
            String titleText = title.getText();
            row.createCell(0).setCellValue(titleText);

            // Try to find an <a> element within the current <h3>
            WebElement linkElement = title;
            if (i == titleList.size()) {
                try {
                    linkElement = title.findElement(By.tagName("a"));
                } catch (NoSuchElementException e) {
                    // Handle exception
                }
            }
            // If an <a> element is found, get the href attribute
            String hrefValue = linkElement.getAttribute("href");
            row.createCell(1).setCellValue(hrefValue);

            // print Main Activity Result
            String mainActivityText = mainActivityResult.get(i).getText();
            row.createCell(2).setCellValue(mainActivityText);
            row.createCell(3).setCellValue(Information.get(i).getText());
            row.createCell(4).setCellValue(cost.get(i).getText());
        }

        // Write the output to a file
        try (FileOutputStream fileOut = new FileOutputStream("C:\\details\\console_output.xlsx")) {
            workbook.write(fileOut);
        } finally {
            workbook.close();
        }
    }
}
