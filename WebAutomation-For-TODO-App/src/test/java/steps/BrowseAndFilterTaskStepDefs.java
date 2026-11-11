package steps;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.thucydides.core.annotations.Managed;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.BrowseAndFilterTaskPage;

import java.util.List;

@ExtendWith(SerenityJUnit5Extension.class)
public class BrowseAndFilterTaskStepDefs {

    @Managed
    BrowseAndFilterTaskPage browseAndFilterTaskPage;

    @Given("I am on the homepage")
    public void iAmOnTheHomepage() {
        browseAndFilterTaskPage.open();
    }

    @When("I enter a Task called task {string} in the search bar")
    public void iEnterATaskCalledTaskInTheSearchBar(String task) {
       browseAndFilterTaskPage.enterTask(task);
    }

    @Then("I must see a list of tasks with name, description, title of this task")
    public void iMustSeeAListOfTasksWithNameDescriptionTitleOfThisTask() {
        MatcherAssert.assertThat(browseAndFilterTaskPage.findTask(), Matchers.is(true));
    }


    @When("I filter a task by priority")
    public void iFilterATaskByPriority() {
        browseAndFilterTaskPage.chooseAFilterOptionPriority();
    }


    @Then("I must see a list of tasks with name, description, title of the priority of the task")
    public void iMustSeeAListOfTasksWithNameDescriptionTitleOfThePriorityOfTheTask() {
        MatcherAssert.assertThat(browseAndFilterTaskPage.findByFilterSelected(), Matchers.is(true));
    }

    @When("I filter a task by date")
    public void iFilterATaskByDate() {
       browseAndFilterTaskPage.chooseAFilterOptionDate();
    }

    @Then("I must see a list of tasks with name, description, title of the date of the task")
    public void iMustSeeAListOfTasksWithNameDescriptionTitleOfTheDateOfTheTask() {
        MatcherAssert.assertThat(browseAndFilterTaskPage.findByFilterSelected(), Matchers.is(true));
    }

    @When("I filter a task by all tasks")
    public void iFilterATaskByAllTasks() {
       browseAndFilterTaskPage.chooseAFilterOptionAllTasks();
    }

    @Then("I must see a list of tasks with name, description, title of the all tasks of the task")
    public void iMustSeeAListOfTasksWithNameDescriptionTitleOfTheAllTasksOfTheTask() {
        MatcherAssert.assertThat(browseAndFilterTaskPage.findByFilterSelected(), Matchers.is(true));
    }

    @When("I sort tasks by date {string}")
    public void iSortTasksByDate(String sortOption) {
        browseAndFilterTaskPage.sortBy(sortOption);
    }

    @Then("i should see the tasks sorted")
    public void iShouldSeeTheTasksSorted() {
        MatcherAssert.assertThat(browseAndFilterTaskPage.listInAscendingOrDescendingOrder().getLast(), Matchers.is(String.valueOf(browseAndFilterTaskPage.getMaxNum())) );
    }


    @When("I sort tasks by priority {string}")
    public void iSortTasksByPriority(String priority) {
      browseAndFilterTaskPage.sortBy(priority);
    }


    @Then("i should see the tasks sorted by priority")
    public void iShouldSeeTheTasksSortedByPriority() {
        List<String> list = browseAndFilterTaskPage.listInAscendingOrDescendingOrder();
        MatcherAssert.assertThat(list.getLast(), Matchers.is("Low"));
        MatcherAssert.assertThat(list.getFirst(), Matchers.is("High"));

    }

    @When("I sort tasks by recently added {string}")
    public void iSortTasksByRecentlyAdded(String recentlyAdded) {
       browseAndFilterTaskPage.sortBy(recentlyAdded);
    }

    @Then("i should see the tasks sorted by recently added")
    public void iShouldSeeTheTasksSortedByRecentlyAdded() {
        MatcherAssert.assertThat(browseAndFilterTaskPage.listInAscendingOrDescendingOrder(), Matchers.containsInRelativeOrder(browseAndFilterTaskPage.listInAscendingOrDescendingOrder().getLast()));
    }

}
