Reactive Flight Service
==============
This is a reactive implementation of a service provides information about airlines, airports and flight connections. It is implemented as a Spring Boot application with a REST API for retrieving the data in JSON format from a MongoDB Repository.

### Running the application locally
A Maven Wrapper is provided which can be used to build and start the application.
Build the application: ./mvnw clean install 
Run the application: ./mvnw spring-boot:run

The raw airlines, airports and flight connections data are plain csv files which are located in the folder .\src\main\resources\data. The data can be loaded into the service. For this purpose the service provides a graphical user interface which can be used for uploading data.
The UI is locally accessed via the following URL: http://localhost:8081/csvUpload
