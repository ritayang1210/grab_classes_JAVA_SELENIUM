package ritaCourse;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AcornPage {

  private static final int INDEX_COURSE = 2;
  /*specified by the course you need*/
  private static final String COUSE_CODE_CSC384H1 = "CSC384H1";
  private static final String CLASS_HEADER = "header";
  private static final String ID_SEARCH = "searchBox";
  private static String LINK_TEXT_ENROL_MANAGE = "Enrol & Manage";
  private static String LINK_TEXT_TERM =
      "ASPRGHBSC 2015-2016 Fall/Winter";
  private static String LINK_TEXT_COURSES = "Courses";

  private WebDriver driver;

  private boolean enrolFinished = false;

  public AcornPage(WebDriver driver) {
    this.driver = driver;

    getEnrolManageButton();
  }

  public AcornPage enrolManage() {
    getEnrolManageButton().click();

    return this;
  }

  public AcornPage selectTerm() {
    new WebDriverWait(driver, 8).until(
        ExpectedConditions.elementToBeClickable(By
            .linkText(LINK_TEXT_TERM))).click();

    return this;
  }

  public AcornPage clickCourse() {
    new WebDriverWait(driver, 8).until(
        ExpectedConditions.elementToBeClickable(By
            .linkText(LINK_TEXT_COURSES))).click();

    return this;
  }

  public AcornPage searchCSC384() {
    WebElement searchBox =
        new WebDriverWait(driver, 8).until(ExpectedConditions
            .elementToBeClickable(By.id(ID_SEARCH)));
    searchBox.clear();
    searchBox.sendKeys(COUSE_CODE_CSC384H1);

    new WebDriverWait(driver, 8)
        .until(
            ExpectedConditions.elementToBeClickable(By.xpath(String
                .format(
                    "//*[@id=\"course_search_results_list\"]/li[%d]/a/span[1]",
                    INDEX_COURSE)))).click();

    return this;
  }

  public AcornPage enrolCSC384IfAvailable() {
    WebElement courseTable =
        new WebDriverWait(driver, 8)
            .until(ExpectedConditions.visibilityOfElementLocated(By
                .xpath("//*[@id=\"modalCourseBox\"]/div[2]/div[3]/div[1]/div/form/table[1]")));

    List<WebElement> courseRows =
        courseTable.findElements(By.xpath("./tbody"));
    for (WebElement courseRow : courseRows) {
      if (CLASS_HEADER.equalsIgnoreCase(courseRow
          .getAttribute("class"))) {
        continue;
      }

      WebElement enrolCheckbox =
          courseRow.findElement(By.name("primaryActivity"));
      if (enrolCheckbox.isEnabled()) {
        enrolCheckbox.click();
        new WebDriverWait(driver, 8).until(ExpectedConditions
            .elementToBeSelected(enrolCheckbox));

        List<WebElement> buttons =
            driver.findElements(By
                .xpath("//*[@id=\"tooltip\"]/div[1]/button/span"));

        for (WebElement button : buttons) {
          if (button.isDisplayed() && button.isEnabled()) {
            button.click();
            enrolFinished = true;
            return this;
          }
        }
      }
    }
    new WebDriverWait(driver, 8).until(
        ExpectedConditions.elementToBeClickable(By
            .xpath("//*[@id=\"tooltip\"]/div[1]/button[4]"))).click();
    return this;
  }

  public boolean isEnrolFinished() {
    return enrolFinished;
  }

  private WebElement getEnrolManageButton() {
    return new WebDriverWait(driver, 8).until(ExpectedConditions
        .elementToBeClickable(By.linkText(LINK_TEXT_ENROL_MANAGE)));
  }
}
