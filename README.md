# Codelitt
Codelitt - Technical Exercise.

# Instructions
The goal of this exercise is to create a backend using Java with the Spring Boot framework.

# The Task
In this task, we are building backend of an application that helps us managing our team.

# Stack
| Technology | Version |
|--|--|
| **Java** | 17.0.1 2021-10-19 |
| **Spring Boot** | 3.0.2.RELEASE |
| **Project Lombok** | 1.18.12 |
| **Jupiter JUnit 5** | 5.6.2 |
| **Model Mapper** | 3.1.0 |
| **Springdoc OpenAPI Swagger** | 2.0.2 |

### Accessing Swagger | Open API:
Once with the application running: http://localhost:8080/swagger-ui.html

### Docker
Exists a Dockerfile prepared to download a OpenJDK 17 Slim and install the application.

- Run the command: `docker build -t codelitt/codelitt:release .`
- Run the command: `docker run -p port:port <IMG_TAG>`
- Example: `docker run -p 8080:8080 8fb870f41548`
- Or download the image `docker pull samuelcatalano/codelitt:release`

### Docker Compose
- Go to the docker-compose.yaml file
- Run the command: `docker-compose up -d`
- It will generate a docker image instance with the PostgreSQL version 12 and the codelitt database

### Database
- As soon as you start the application, the .ddl will create the tables automatically.
- You have the option to create the tables manually if you prefer accessing the folder `db_scripts` and run the sql files  

### Running the application:
> IDE (IntelliJ, Eclipse, NetBeans):
- Importing the project as Maven project on your favourite IDE.
- Build project using Java 17
- Run/Debug project from Main Application Class :: `Application.class`

> Terminal:
- `mvn spring-boot:run`

### Running the tests
> Terminal:
- `mvn test`

### APIs:
The basic URL path is: http://localhost:8080/api/members

* GET:  (findAll): http://localhost:8080/api/members/
* GET:  (findById) http://localhost:8080/api/members/1


* POST: (create new member) http://localhost:8080/api/members
> JSON Body example
```javascript{
{
   "firstName":"Sam",
   "lastName":"Catalano",
   "salary":5000,
   "type":"EMPLOYEE",
   "contractDuration":36,
   "role":"Software Developer",
   "tags":[
      "Java",
      "Spring",
      "Microservices"
   ],
   "country":"brasil"
}
```

* PUT:  (update existent member) http://localhost:8080/api/members/1
> JSON Body example
```javascript{
{
   "firstName":"Sam",
   "lastName":"Catalano",
   "salary":15000,
   "type":"CONTRACTOR",
   "contractDuration":12,
   "role":"Software Developer",
   "tags":[
      "Backend",
      "DevOps",
      "Microservices"
   ],
   "country":"italy"
}
```

* DELETE:  (deleteById) http://localhost:8080/api/members/1

# Possible Improvements
### Sharing thoughts on what I could improve on my code given more time and incentives:

1) I left the field role as String because, in almost all companies that I've worked, the roles were positions well defined by the company, which means that an enum probably is not the best option but maybe an entity with fields like id, name, level, etc would be good but once that it was not on the requirements, I left as String.
2) Also, I left the tags as a list of String mapped by @ElementCollection annotation because we need that for mapping non-entities as a list. Maybe a tag could also be an entity with fields well defined like id, name, etc. But I don't see problems in storing it as element collections at this moment. In the future, converting it to an entity could make it easier for the filtering process.
3) Another idea is updating the dependencies with the native-image support to use GraalVM and with that create an image faster and lighter.
4) Implementing the spring-security part to make the application more secure.
5) Implement a database migration tool like Flyway or Liquibase
6) Create a CI/CD pipeline for automating the deployment process and possible integrations.
7) I didn't deploy the app on a server because Heroku left it to be free and in my last tech challenge I used all my free credits in the AWS account and now I have to pay for it. I'm more than glad to explain how to make this deploy in a possible tech interview 

