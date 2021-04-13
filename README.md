# New Bank

Group 12 Coursework 1 project in Software Engineering 2 - March 2021, University of Bath.

## Protocol

This section details the protocol for interacting with the NewBank server.


```SHOWMYACCOUNTS```

- To return a list of your accounts.

```RESETPASSWORD <new password> <new password>```

- To reset your password. Password must be entered twice to check they match.

```ADDACCOUNT <account name>```

- To create a new account.

```MOVE <Amount> <From> <To>```

- To move an amount of money from one account to another.

```TRANSFER <Amount> <From> <To>```

- To transfer an amount of money from the selected customer's account to any account in the bank.

```PAY <Person/Company> <Amount>```

- To pay an amount of money to another person or company.

```SHOWACCOUNT <Account Name>```

- To return the details and transactions to and from an account.

```SHOWTRANSACTIONS```

- To return a list of all your transactions to and from all of your accounts.

```SHOWACCOUNTINFO```

- To return a list of your personal details.

```EDITADDRESS <password> <new address>```

- To update your address.

```EDITPHONENUMBER <password> <new phone number>```

- To update your phone number.

```EDITFULLNAME <password> <new full name>```

- To update your Full Name.

```EDITSECURITYQUESTION <password> <new security question>```

- To update your security question.

```REQUESTLOAN <VALUE> <APR> <TERM>```

- Request a loan and have it submitted to the marketplace pending approval/funding.

```LOANMARKETPLACE```

- Prints out all requested loans.

```PICKLOAN <LOAN ID>```

- Allows you to pick a loan to fulfill the request.