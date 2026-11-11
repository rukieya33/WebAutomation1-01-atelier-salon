Feature: Browse And Filter Tasks

  @HappyPath
    Scenario Outline:
      Given I am on the homepage
      When I enter a Task called task "<task>" in the search bar
      Then I must see a list of tasks with name, description, title of this task
    Examples:
     |task|
     |plan the week ahead|
     |book a dentist appointment|
     |pick up groceries         |
     |up                        |
     |appointment               |
     |plan                      |

  @HappyPath
  Scenario:
    Given I am on the homepage
    When I filter a task by priority
    Then I must see a list of tasks with name, description, title of the priority of the task

  @HappyPath
  Scenario:
    Given I am on the homepage
    When I filter a task by date
    Then I must see a list of tasks with name, description, title of the date of the task

  @HappyPath
  Scenario:
    Given I am on the homepage
    When I filter a task by all tasks
    Then I must see a list of tasks with name, description, title of the all tasks of the task

  @HappyPath
  Scenario Outline:
    Given I am on the homepage
    When I sort tasks by date "<sort>"
    Then i should see the tasks sorted
  Examples:
    |sort|
    |Due date|

  @HappyPath
  Scenario Outline:
    Given I am on the homepage
    When I sort tasks by priority "<sort>"
    Then i should see the tasks sorted by priority
    Examples:
      |sort|
      |Priority|


  @HappyPath
  Scenario Outline:
    Given I am on the homepage
    When I sort tasks by recently added "<sort>"
    Then i should see the tasks sorted by recently added
    Examples:
      |sort|
      |Recently added|