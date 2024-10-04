package tests;

import org.example.pages.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.TakesScreenshot;
import org.apache.commons.io.FileUtils;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;

public class CheckoutTests {
    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com");
    }

    @DataProvider(name = "checkoutData")
    public Object[][] checkoutData() {
        return new Object[][] {
                { "John", "Doe", "12345" },
                { "Jane", "Smith", "54321" }
        };
    }
    @DataProvider(name = "loginUserData")
    public Object[][] loginUserData() {
        return new Object[][] {
                { "John", "Doe", "12345" },
                { "Jane", "Smith", "54321" }
        };
    }

    @Test(dataProvider = "checkoutData")
    public void testCheckoutProcess(String firstName, String lastName, String zipCode) {
        Login loginPage = new Login(driver);
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();

        Product productPage = new Product(driver);
        productPage.addFirstProductToCart();
        productPage.clickCartIcon();

        Cart cartPage = new Cart(driver);
        cartPage.clickCheckout();

        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.enterFirstName(firstName);
        checkoutPage.enterLastName(lastName);
        checkoutPage.enterZipCode(zipCode);
        checkoutPage.clickContinue();

        CheckoutOverview overviewPage = new CheckoutOverview(driver);
        Assert.assertTrue(overviewPage.getItemNames().contains("Sauce Labs Backpack"));
        Assert.assertTrue(overviewPage.getItemPrices().contains("$29.99"));

        overviewPage.clickFinish();
        Assert.assertEquals(overviewPage.getCompleteMessage(), "Thank you for your order!");
    }

    @Test
    public void testBackButtonFunctionality() {

        Login loginPage = new Login(driver);
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();

        Product productPage = new Product(driver);
        productPage.addFirstProductToCart();
        productPage.clickCartIcon();

        Cart cartPage = new Cart(driver);
        cartPage.clickCheckout();

        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.enterFirstName("John");
        checkoutPage.enterLastName("Doe");
        checkoutPage.enterZipCode("12345");
        checkoutPage.clickContinue();

        CheckoutOverview overviewPage = new CheckoutOverview(driver);
        overviewPage.clickFinish();
        Assert.assertEquals(overviewPage.getCompleteMessage(), "Thank you for your order!");


        overviewPage.goBack();


    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (ITestResult.FAILURE == result.getStatus()) {
            try {
                File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(screenshot, new File("C:\\Users\\birsa\\Desktop\\ABCD\\screenshot_" + result.getName() + ".png"));
                System.out.println("Screenshot taken for failed test: " + result.getName());
            } catch (IOException e) {
                System.out.println("Exception while taking screenshot: " + e.getMessage());
            }
        }
        if (driver != null) {
            driver.quit();
        }
    }
}
