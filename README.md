
# PainCare

PainCare is an m-Health solution that has been developed with the aim of providing comprehensive resources and support to women battling endometriosis.


## Run Locally

Go to the project directory

```bash
  cd pain-care
```

Change the database information in `src/main/resources/application.yml`

Create a paincare database: `CREATE DATABASE paincare`

Run the project

```bash
  gradlew bootRun
```

The website will be listening on `localhost:8080`


## Tech Stack

- Spring Boot
- Spring Security
- Thymeleaf
- MySQL
- Bootstrap
- Gradle


## Primary Features
- A home page with information about endometriosis
- A community where users can post and comment
- A pain recording function with a graph to show pain evolution
- A diagnostic test with a score

## Secondary Features
- Security
- High response time
- Simple UI/UX

