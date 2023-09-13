Feature: Transaction Processing

  Scenario: Submitting a valid transaction
    Given I have a valid transaction
    When I submit the transaction
    Then I should receive a confirmation response

  Scenario: Submitting an invalid transaction
    Given I have an invalid transaction
    When I submit the transaction
    Then I should receive an error response
