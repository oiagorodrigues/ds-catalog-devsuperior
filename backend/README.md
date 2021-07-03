# DS Catalog API

## About the project

DS Catalog API is a project made in the 5º edition of the **DevSuperior Bootcamp** (BDS3.0). 
Event organized by [DevSuperior](https://devsuperior.com "DevSuperior website").

It delivers endpoints to be consumed by a Catalog Platform application.

## Technologies used
- Java
- Spring Boot 2.4.4
- JPA / Hibernate
- Maven
- H2 Database
- OAuth2
- JUnit
- Mockito

## How to execute the project

Requirements: Java 11

```bash
# clone the repository
git clone git@github.com:oiagorodrigues/ds-catalog-devsuperior.git

# go to the backend directory
cd backend

# run the project
./mvnw spring-boot:run

or
```

Open it in your favorite IDE. 
Recommended using [Intellij](https://www.jetbrains.com/pt-br/idea/), [STS](https://spring.io/tools) or [VSCode](https://code.visualstudio.com/).

## Learning topics

- Data Layer with Model, Repository and Service
- API layer with Controller and DTO
- Custom Exceptions, Errors and ExceptionHandlers
- Relational Database
- Database seeding
- Integration Tests
- Unit Tests
- OAuth2 Authorization Server configuration
- OAuth2 Resource Server configuration
- JWT
- Spring @Bean and @Configuration
- ORM with Hibernate JPA
- OneToMany, OneToOne, ManyToOne, uni and bidirectional Relations

## Conceptual model

<img src="https://user-images.githubusercontent.com/19571060/124338052-4382b800-db7c-11eb-9ce6-14e645b3e047.png" width="800">

#### Authorization Layer

<img src="https://user-images.githubusercontent.com/19571060/124338067-51383d80-db7c-11eb-9cef-93d019d501a7.png" width="800">

## TODO

- [ ] Finish Unit Tests
- [ ] Finish Integration Tests
- [ ] Integration with PostgreSQL
- [ ] Deploy

## Author

Iago Rodrigues Melo

https://www.linkedin.com/in/iago-rodrigues
