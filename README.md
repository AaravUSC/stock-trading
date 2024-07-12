# stock-trading

This repository is a Spring Boot web application for trading stocks in real time using fake money. It's suitable for people who want to try their hand at trading without commiting real cash, and it can support multiple users with persistent trades via a MySQL database.

## Setup

After cloning the repository, install the requirements:
> $ mvn clean install

On MySQL workbench, run the create.sql file provided in the repositoryl.

Obtain a free Finnhub API key [here](https://finnhub.io/register) for real-time stock quotes.

Once the database is setup, go to the 'resources' directory in the repo and create a .env file.
> $ cd src/main/resources/

In the .env file, set the following variables:
'''
SQLUSER = {Your MySQL username}
SQLPASSWORD = {Your MySQL password}
FINNHUBAPI = {Your Finnhub API key}
JDBCURL = {Your MySQL host url}
'''
