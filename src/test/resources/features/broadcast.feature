Feature: Broadcast Resource Operations

  Scenario: Retrieve broadcasts by content ID
    Given I am authenticated as user
    When I request broadcasts for content ID "1"
    Then I should receive a list of broadcasts

  Scenario: Get raw transaction to reject a broadcast
    Given I have a broadcast with ID "1"
    And I am authenticated as user
    When I reject the broadcast with ID "1"
    Then I get a raw the transaction

  Scenario: Get raw transaction to approve a broadcast
    Given I have a broadcast with ID "1"
    And I am authenticated as user
    When I approve the broadcast with ID "1"
    Then I get a raw the transaction

  Scenario: Set broadcast owner provider
    Given I am authenticated as user
    When I set the broadcast owner provider to "0x8430fC8E90A103D4Fb78689dc76Fb8D90fe916BE"
    Then I get a raw the transaction
