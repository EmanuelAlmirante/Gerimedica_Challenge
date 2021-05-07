# Gerimedica Challenge

Gerimedica take home take challenge (2 hours). 

Due to the time constraints, I mostly focused on getting the main functionalities to work. There are still a lot that could be improved, as it is obvious, but I tried to keep the code as readable and maintainable as possible by following the clean code rules (or trying to as much as I could).

There is also the need to create tests, especially unit and integration tests. For this I would use JUnit and Mockito and would test that the functionalities are working as intended as well as the exceptions (good path and bad path).

### Tech Stack:

- Java 11
- Spring Boot
- Hibernate
- Swagger
- H2 Database (in-memory)

### Setup without Docker:

- Clone/extract project to a folder
- Run the application with:
    - _mvn clean install_
    - _mvn spring-boot:run_
- Package the application with _mvn package_


### Setup with Docker:

- Install Docker on your machine
- Launch Docker
- Run the command _sudo systemctl status docker_ to confirm Docker is running
- Open terminal in the project folder
- Run the command _sudo docker-compose build_
- Run the command _sudo docker-compose up -d_
- The container will be up and the application will be running inside


### Endpoints:

The documentation of this API can be found at _http://localhost:8080/swagger-ui.html_ (**Note: you need to initialize the application to access this link**).
