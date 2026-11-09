package steps;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.annotations.Managed;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import pages.BrowseAndFilterServicePage;

public class BrowseAndFilterServiceStepDefs {

    @Managed
    BrowseAndFilterServicePage browseAndFilterServicePage;

    @Given("I am on the booking homepage")
    public void iAmOnTheBookingHomepage() {
        browseAndFilterServicePage.open();
    }

    @When("I view the service list")
    public void iViewTheServiceList() {
        browseAndFilterServicePage.getEnteredSearchText();
    }

    @Then("I must see a list of services matching the criteria")
    public void iMustSeeAListOfServices() {
        MatcherAssert.assertThat(
                browseAndFilterServicePage.doAllVisibleServicesMatchWord(""),
                Matchers.is(true)
        );
    }

    @When("I search for a word {string}")
    public void iSearchForAWord(String word) {
        browseAndFilterServicePage.enterSearchWord(word);
    }

    @Then("I must see the list of service names matching {string}")
    public void iMustSeeTheListOfServiceNamesMatching(String word) {
        MatcherAssert.assertThat(
                browseAndFilterServicePage.doAllVisibleServicesMatchWord(word),
                Matchers.is(true)
        );
    }

    @Then("I must see the empty state message {string}")
    public void iMustSeeTheEmptyStateMessage(String expectedMessage) {
        MatcherAssert.assertThat(browseAndFilterServicePage.isEmptyStateDisplayedWithText(expectedMessage), Matchers.is(true));
     }

    @When("I select a specific category {string}")
    public void iSelectASpecificCategory(String category) {
        browseAndFilterServicePage.selectCategoryByVisibleText(category);
    }

    @When("I filter by their name, {string}")
    public void iFilterByTheirName(String name) {
       browseAndFilterServicePage.selectCategoryByVisibleText(name);
    }

    @Then("only the services they provide should be displayed with name {string}")
    public void onlyTheServicesTheyProvideShouldBeDisplayedWithName(String name) {
        MatcherAssert.assertThat(browseAndFilterServicePage.doAllVisibleServicesMatchWord(name), Matchers.is(true));
    }
}
