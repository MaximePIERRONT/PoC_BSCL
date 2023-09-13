Feature: Authentication

  Scenario: Get nonce for an address
    Given I have a valid address
    When I request a nonce for the address
    Then the system should return a nonce for the address

  Scenario: Authenticate with a valid signed nonce
    Given I have already get a nonce for my address and sign it
    When I authenticate using the address and the signed nonce
    Then the system should return a JWT token

  Scenario: Fail authentication with an invalid signed nonce
    Given I have a valid address and an invalid signed nonce
    When I attempt to authenticate using the address and the invalid signed nonce
    Then the system should return an error 406, Invalid signature