# language: en
Feature: Product Management
  As a system user
  I want to manage products
  So that I can keep the catalog up to date

  Background:
    Given the system is initialized

  Scenario: Create a new product successfully
    When I create a product with the following data:
      | name        | Artisan Burger                                         |
      | description | Burger with artisan bread, 180g meat and cheese       |
      | price       | 25.90                                                  |
      | category    | LANCHE                                                 |
    Then the product should be created successfully
    And the product should have the name "Artisan Burger"
    And the product should have the price of 25.90

  Scenario: Find product by ID
    Given a product exists
    When I search for the product by ID
    Then the product should be found
    And the product should have all correct data

  Scenario: List all products
    Given there are 3 registered products
    When I list all products
    Then I should receive a list with 3 products

  Scenario: Search products by category
    Given there are products in categories "LANCHE" and "BEBIDA"
    When I search for products in category "LANCHE"
    Then I should receive only products from category "LANCHE"

  Scenario: Search products on promotion
    Given there are products on promotion and without promotion
    When I search for products on promotion
    Then I should receive only products on promotion

  Scenario: Search products by name
    Given there is a product with name "Margherita Pizza"
    When I search for products with name "Pizza"
    Then I should find the product "Margherita Pizza"

  Scenario: Search products by category and price range
    Given there are products in category "LANCHE" with varied prices
    When I search for products in category "LANCHE" between 10.00 and 30.00
    Then I should receive only products in the specified price range

  Scenario: Delete a product
    Given a product exists
    When I delete the product
    Then the product should no longer exist in the system

  Scenario: Creating product with invalid price should fail
    When I try to create a product with invalid price
    Then it should return a validation error

  Scenario: Apply promotion to a product
    Given there is a product with price of 50.00
    When I apply a promotion of 35.00
    Then the effective price should be 35.00
    And the product should be marked as on promotion
