Feature: Content Management

  Scenario: List all contents
    Given I am a user with a valid token
    When I request a list of all contents
    Then I should receive a list of all contents

  Scenario: Register a new content
    Given I am a user with a valid token
    When I register a new content with price "2000" and contract link "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf" and contract hash "3df79d34abbca99308e79cb94461c1893582604d68329a41fd4bec1885e6adb4"
    Then I should receive a raw transaction

  Scenario: Purchase a content
    Given I am a user with a valid token
    When I purchase a content with ID "1" and video ID "video123"
    Then I should receive a raw transaction for the purchase

  Scenario: Set content as unavailable
    Given I am a user with a valid token
    When I set content with ID "1" as unavailable
    Then I should receive a valid response

  Scenario: Request my contents
    Given I am a user with a valid token
    When I request my contents
    Then I should receive a valid response of Contents