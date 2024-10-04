package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class CheckoutOverview {
    private WebDriver driver;
    private WebDriverWait wait;

    private By itemName = By.className("inventory_item_name");
    private By itemPrice = By.className("inventory_item_price");
    private By finishButton = By.cssSelector(".btn_action.cart_button");
    private By completeMessage = By.className("complete-header");

    public CheckoutOverview(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("inventory_item_name")));
    }

    public List<String> getItemNames() {
        return driver.findElements(itemName).stream().map(e -> e.getText()).collect(Collectors.toList());
    }

    public List<String> getItemPrices() {
        return driver.findElements(itemPrice).stream().map(e -> e.getText()).collect(Collectors.toList());
    }

    public void clickFinish() {

        wait.until(ExpectedConditions.visibilityOfElementLocated(finishButton));
        wait.until(ExpectedConditions.elementToBeClickable(finishButton)).click();
    }

    public String getCompleteMessage() {
        return driver.findElement(completeMessage).getText();
    }

    public void goBack() {
        driver.navigate().back();
    }



}