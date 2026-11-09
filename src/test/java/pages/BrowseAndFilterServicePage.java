package pages;

import net.serenitybdd.annotations.DefaultUrl;
import net.thucydides.core.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import java.util.List;

@DefaultUrl("http://localhost:5173/")
public class BrowseAndFilterServicePage extends PageObject {

    // Using more robust class/ID selectors instead of raw tags
    @FindBy(css = "input.search-field, input[type='search']")
    private WebElement searchInput;

    @FindBy(className = "service-grid")
    private WebElement serviceListContainer;

    @FindBy(className = "empty-state")
    private WebElement emptyStateMessage;

    @FindBy(className = "select-wrap")
    private WebElement categoryDropdown;

    public void enterSearchWord(String word) {
        searchInput.clear();
        searchInput.sendKeys(word);
    }

    // Explicitly reading the input text values using getAttribute("value")
    public void getEnteredSearchText() {
        searchInput.getAttribute("value");
    }

    public boolean doAllVisibleServicesMatchWord(String word) {
        List<WebElement> serviceCards = serviceListContainer.findElements(By.className("service-card"));

        // Fail early if no items exist when we expect them
        if (serviceCards.isEmpty()) {
            return false;
        }

        // Validate that EVERY visible card matches the search criteria
        for (WebElement card : serviceCards) {
            String serviceName = card.findElement(By.className("service-name")).getText();
            String serviceDescription = card.findElement(By.className("service-content")).getText();

            if (serviceName.contains(word.toLowerCase()) ||
                    serviceDescription.contains(word.toLowerCase()) || !serviceDescription.contains(word.toLowerCase())
                    && !serviceName.contains(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmptyStateDisplayedWithText(String expectedMessage) {
        return emptyStateMessage.isDisplayed() && emptyStateMessage.getText().contains(expectedMessage);
    }

    // Actively select the option instead of just listening to it
    public void selectCategoryByVisibleText(String selectedWord) {
        List<WebElement> list = categoryDropdown.findElements(By.cssSelector("select"));
        for (WebElement element : list) {
            if (element.getText().contains(selectedWord)) {
                Select select = new Select(element);
                select.selectByVisibleText(selectedWord);
            }
        }

    }
}
