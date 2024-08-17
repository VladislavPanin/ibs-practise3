package ru.ibs;


import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.ibs.services.UIBugMessCreatorService;

import java.time.Duration;
import java.util.stream.Stream;


/**
 * Практическое задание №3_Тестирование Java
 */
public class ProductWithUITest {

    private static WebDriver driver;
    private int idPreviousProduct;
    private int idCurrentWebElem;
    private final UIBugMessCreatorService bugMessCreatorService = new UIBugMessCreatorService();

    private static Stream<Arguments> productsParameters() {
        return Stream.of(
                Arguments.of("Ананас&","'Фрукт'", true),
                Arguments.of("Яблоко","'Фрукт'", false),
                Arguments.of("Помидорррррр", "'Овощ'", false),
                Arguments.of("Пепино", "'Овощ'", true)
        );
    }

    /**
     * ! Перед проверской практической работы не забудьте указать правильный путь к версии хрома для тестирования,
     * если это потребуется, а так же запустить тестовый стенд
     */
    @BeforeAll
    public static void setup() {

        ChromeOptions co = new ChromeOptions();
        co.setBinary("D:\\opt\\chrome-win64.chrome.exe");

        driver = new ChromeDriver();
        System.setProperty("webdriver.chromedriver.diver",
                "\\auto-testing-qualit-sandbox\\src\\test\\resources\\chromedriver.exe");

        driver.manage().window().maximize();
        driver.get("http://localhost:8080/food");
    }

    @BeforeEach
    void getPreviousProductId(){
        try {
            idPreviousProduct = Integer.parseInt(driver.findElement(By.xpath(
                    "(//tr)[last()]/th")).getText());
        } catch (Exception e){
            Assertions.fail("Invalid id value - cannot be converted to int.");
        }
    }


    @ParameterizedTest()
    @MethodSource("productsParameters")
    void addingProductTest(String currentProductName, String currentProductType, boolean currentExoticCheckbox){

        int uncriticalBugCounter = 0;
        StringBuilder testRes = new StringBuilder();
        boolean testFailedFlag = false;
        String XPathDot = ".,";
        int timeToWait = 5;

        WebElement bntAdd = driver.findElement(By.xpath("//button[contains(.,'Добавить')]"));
        bntAdd.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeToWait));
        try {
            WebElement ModalWindowLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                    "//h5[contains(., 'Добавление товара')]")));
        } catch (TimeoutException e){
            bugMessCreatorService.createOpenModalWindowErrorMessage(testRes, e, timeToWait);
            Assertions.fail(String.valueOf(testRes));
        }

        WebElement inputName = driver.findElement(By.id("name"));
        inputName.sendKeys(currentProductName);

        WebElement typeDropdown = driver.findElement(By.id("type"));
        typeDropdown.click();
        WebElement fruitOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                "//*[@id=\"type\"]/option[contains(" + XPathDot +  currentProductType + ")]")));
        fruitOption.click();

        WebElement exoticCheckbox = driver.findElement(By.id("exotic"));
        if (!exoticCheckbox.isSelected() && currentExoticCheckbox) {
            exoticCheckbox.click();
        }

        WebElement saveButton = driver.findElement(By.id("save"));
        saveButton.click();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            idCurrentWebElem  = Integer.parseInt(driver.findElement(By.xpath("" +
                    "(//tr)[last()]/th")).getText());
        }catch (Exception e) {
            Assertions.fail("Invalid id value - cannot be converted to int.");
        }
        if(idCurrentWebElem != idPreviousProduct + 1){
            bugMessCreatorService.createBugMessForId(idCurrentWebElem, idPreviousProduct, testRes);
            uncriticalBugCounter++;
            testFailedFlag = true;
        }

        String productName = driver.findElement(By.xpath("(//tr)[last()]/td[1]")).getText();
        if(!productName.equals(currentProductName)){
            bugMessCreatorService.createBugMessForName(productName, currentProductName, testRes);
            uncriticalBugCounter++;
            testFailedFlag = true;
        }

        String productType = driver.findElement(By.xpath("(//tr)[last()]/td[2]")).getText();
        currentProductType = currentProductType.substring(1, currentProductType.length() - 1);
        if(!productType.equals(currentProductType)){
            bugMessCreatorService.createBugMessForProductType(currentProductType, productType, testRes);
            uncriticalBugCounter++;
            testFailedFlag = true;
        }

        boolean exoticCheckboxBool = false;
        try {
            exoticCheckboxBool = Boolean.parseBoolean(driver.findElement(By.xpath(
                    "(//tr)[last()]/td[3]")).getText());
        }catch (Exception e) {
            Assertions.fail("The checkbox value could not be parsed. ");
        }

        if (!exoticCheckboxBool == currentExoticCheckbox){
            bugMessCreatorService.createBugMessForExoticCheckbox(exoticCheckboxBool, currentExoticCheckbox, testRes);
            uncriticalBugCounter++;
            testFailedFlag = true;
        }

        if (testFailedFlag){
            Assertions.fail(testRes + "\ncount of uncritical bugs: " + uncriticalBugCounter);
        }
    }

    @AfterAll
    public static void tearDown() {
        driver.quit();
    }
}
