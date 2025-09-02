# Overview

This application contain the following endpoints.
• GET /api/holidays/last3/{countryCode} (get last 3 of holidays)
• GET /api/holidays/counts (get count of holiday only coming on weekdays for given country codes)
• GET /api/holidays/commonHolidays (get common holidays for given 2 country code)

assumptions: 
. External API is running with 2 parameters countryCode and year . So for finding last 3 holidays for given 
    countryCode using current year.
. Added Async for second count api , as there can be multiple external api call possible.

inclusions:
. Added code coverage and check style.
. Added docker script for containerization of the application.
. Added monitoring and tracing.
. Added grafana dashboard .

# Local setup

To build and run the application locally, you will need the following:

* [JDK 21](https://www.oracle.com/java/technologies/downloads/#jdk21-windows) or mac
* [Maven](https://maven.apache.org/download.cgi)
* 
### Compile and Build

This can be done using the following:
Maven commands:
mvn clean compile
mvn install

## Build Docker image 
run docker engine and then run command:
docker build -t holiday-service .

### Running the Application

To run the application, you can either use the below commands or the `Run App against Default` configuration in your
IDE:

First, navigate to the `src` directory:

run Holidayapp using default application.properties.

or

 run dockerfile

### Monitoring 
Added actuator api to check the health, metrics and prometheus of the application.
http://localhost:8086/actuator/health
http://localhost:8086/actuator/metrics
http://localhost:8086/actuator/prometheus

### Prometheus and grafana integration
after running the app in container run:
docker-compose up -d

From generated metrics , save the holiday-dashboard.json and import it into grafana UI.
(I have copied one of the json in repo)
and open grafana dashboard using http://localhost:3000
Login admin /admin

upload the json to grafana dashboard .

select prometheus data source.

### API Documentation with openAPI
http://localhost:8086/v3/api-docs
