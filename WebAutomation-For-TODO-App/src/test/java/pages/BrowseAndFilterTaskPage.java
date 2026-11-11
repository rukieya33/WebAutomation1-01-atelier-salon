package pages;

import net.thucydides.core.annotations.DefaultUrl;
import net.thucydides.core.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@DefaultUrl("http://127.0.0.1:4171/")
public class BrowseAndFilterTaskPage extends PageObject {
    @FindBy(id = "search")
    WebElement searchBox;

    @FindBy(className = "task-list")
    WebElement taskList;

    @FindBy(id = "task-filter")
    WebElement taskFilter;

    @FindBy(id = "task-sort")
    WebElement taskSort;

    private Select select, select_two;

    public void initializeSelect(){
        select = new Select(taskFilter);
        select_two = new Select(taskSort);
    }
    public void enterTask(String task){
        searchBox.sendKeys(task);
    }

    public void sortBy(String sortText){
        initializeSelect();
        select_two.selectByVisibleText(sortText);
    }

    public void chooseAFilterOptionPriority(){
       initializeSelect();
       select.selectByIndex(1);
    }

    public void chooseAFilterOptionDate(){
        initializeSelect();
        select.selectByIndex(2);
    }

    public void chooseAFilterOptionAllTasks(){
        initializeSelect();
        select.selectByIndex(0);
    }

    public boolean findTask(){
        initializeSelect();
        List<WebElement> task_list = taskList.findElements(By.className("task-row"));

        boolean found = false;
        for(WebElement element : task_list) {
            if (element.findElement(By.className("task-main")).findElement(By.className("task-title-button")).getText().toLowerCase().contains(searchBox.getAttribute("value"))) {
                found = true;
            }
                else if(!task_list.isEmpty() || element.findElement(By.className("task-right")).findElement(By.className("priority")).getText().contains(select.getFirstSelectedOption().getAttribute("value"))
            ){
                found = true;
            }
        }

        if(task_list.isEmpty()){
            found = true;
        }
        return found;
    }

    public boolean findByFilterSelected(){
        return findTask();
    }

    public List<String> listInAscendingOrDescendingOrder() {
        List<WebElement> listTask = taskList.findElements(By.className("task-row"));
        List<String> date = new ArrayList<>();
        List<String> priority = new ArrayList<>();
        List<String> recentAdd = new ArrayList<>();
        List<String> list = new ArrayList<>();

        for (WebElement element : listTask) {
            if (select_two.getFirstSelectedOption().getText().toLowerCase().contains("due")) {
                date.add(element.findElement(By.className("task-main")).findElement(By.className("task-meta")).findElement(By.className("task-date")).getText().split(" ")[1]);
                list = date;
            }
            else if(element.findElement(By.className("task-right")).findElement(By.className("priority")).getText().contains("High") ||
                    element.findElement(By.className("task-right")).findElement(By.className("priority")).getText().contains("Low") ||
                    element.findElement(By.className("task-right")).findElement(By.className("priority")).getText().contains("Medium")){
                priority.add(element.findElement(By.className("task-right")).findElement(By.className("priority")).getText());
                list = priority;
            }
            else {
                recentAdd.add(element.findElement(By.className("task-main")).findElement(By.className("task-title-button")).getText());
                list = recentAdd;
            }
        }
        return list;
    }

    public int getMaxNum (){
        List<String> arr = listInAscendingOrDescendingOrder();
        LocalDate today = LocalDate.now();

        int max = 0;
        for (String s : arr) {
            if (!s.equals("Tomorrow")) {
                max = Math.max(Integer.parseInt(s), today.getDayOfMonth());
            } else {
                max = Math.max(today.getDayOfMonth() + 1, today.getDayOfMonth());
            }
        }

        return max;
    }
}
