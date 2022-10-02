# 2022-PFSD-CaseStudy-01-Functional

Based on the solution given
at: [2022-PFSD-CaseStudy-01-Imperative](https://github.com/PFSD-ECI/2022-PFSD-CaseStudy-01-Imperative) you should
develop in this project the same solution in Scala using the _Functional Paradigm_.

You are expected to demonstrate that you can convert it from the Imperative solution using:

* Lambda functions
* filter
* map
* flatMap
* Pattern matching
* Option
* Future
* Either
* Lazy evaluation

This solution provides a basic _CsvReader_, so you can have out of the box the list of transactions present in the
_transactions.csv_ file under the **csvs** folder.

Also, a _TransactionDAO_ that has the functionality to persist/read values from/to PostgreSQL, but **it can be
improved** as well. Do not limit to implement the defined _TODOs_.

You are free to create the tests you think would be the best. Some ideas are handling transactions in different
currencies, or different types.

Do your own tests based on the _**RepositoryIntegrationTest**_ class, which currently
has a test working for a tiny file that contains 10 records (_10records.csv_). **These tests are going to be
evaluated.**


**Don't fork this repo, create branches or Pull Requests**. If you want to have your own version in GitHub create a
private
repo under your personal account, _**otherwise others will have access to your implementation**_.

You can send the development of the case study as a link to your own private repo in your personal GitHub, or you can
send a zip file with the development of the case
to [robert.martinez@escuelaing.edu.co](mailto:robert.martinez@escuelaing.edu.co).