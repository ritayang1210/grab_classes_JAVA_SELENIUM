package ritaCourse;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

  private static final String ID_LOGIN_FORM = "query";
  /*replaced by your utorid passward*/
  private static final String PASSWORD = "...";
  private static final String ID_PASSWORD = "inputPassword";
  /*replaced by your utorid*/
  private static final String USER_NAME = "...";
  private static final String ID_USER_NAME = "inputID";

  private static String LINK_TEXT_LOGIN_BUTTON = "Login to ACORN";

  private WebDriver driver;

  public LoginPage(WebDriver driver) {
    this.driver = driver;

    this.driver.get("http://www.rosi.utoronto.ca/");
    getLoginButton();
  }

  public AcornPage login() {
    getLoginButton().click();

    new WebDriverWait(driver, 8)
        .until(
            ExpectedConditions.visibilityOfElementLocated(By.id(ID_USER_NAME)))
        .sendKeys(USER_NAME);
    new WebDriverWait(driver, 8)
        .until(
            ExpectedConditions.visibilityOfElementLocated(By.id(ID_PASSWORD)))
        .sendKeys(PASSWORD);

    new WebDriverWait(driver, 8)
        .until(
            ExpectedConditions.visibilityOfElementLocated(By.id(ID_LOGIN_FORM)))
        .submit();

    return new AcornPage(driver);
  }

  public void close() {
    driver.quit();
  }

  private WebElement getLoginButton() {
    return new WebDriverWait(driver, 10).until(ExpectedConditions
        .elementToBeClickable(By.linkText(LINK_TEXT_LOGIN_BUTTON)));
  }

  public static void main(String[] args) throws InterruptedException {
    LoginPage loginPage = new LoginPage(new ChromeDriver());
    try {
      AcornPage acornPage =
          loginPage.login().enrolManage().clickCourse().selectTerm();

      while (!acornPage.isEnrolFinished()) {
        acornPage.searchCSC384().enrolCSC384IfAvailable();
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      // Thread.sleep(5000);
      // loginPage.close();
    }
  }
}
