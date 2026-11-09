Feature: Browse And Filter Service

  Background:
    Given I am on the booking homepage

  @HappyPath
  Scenario: View all available services
    When I view the service list
    Then I must see a list of services matching the criteria

  @HappyPath
  Scenario Outline: Search returns matching values
    When I search for a word "<word>"
    Then I must see the list of service names matching "<word>"
    Examples:

      | word                |
      | Signature Haircut   |
      | Haircut & Style     |
      | Deep Tissue Massage |

  @SadPath
  Scenario Outline: Search returns no results
    When I search for a word "<word>"
    Then I must see the empty state message "No services found"
    Examples:

      | word      |
      | Signiture |
      | bread     |

  @HappyPath
  Scenario Outline: Filter services by category
    When I select a specific category "<category>"
    Then I must see the list of service names matching "<category>"
    Examples:

      | category |
      | Hair     |
      | Massage  |
      | All categories|
      | Skin          |

  @HappyPath
  Scenario Outline: Filter services by name
    When I filter by their name, "<name>"
    Then only the services they provide should be displayed with name "<name>"
    Examples:
      | name |
      |Any specialist |
      | Amara Lewis  |
      | Maya Chen|
      | Nina Patel          |
      | Theo James          |
