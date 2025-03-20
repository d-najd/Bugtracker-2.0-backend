# Bugtracker Backend
Backend for [android app](https://github.com/d-najd/Bugtracker-2.0-App) used for project management and issue tracking

# Architecture
![Architecture Image](./github/Backend_Architecture-modified.png)

* Some features are not fully implemented yet (like media storage) but I plan this to be the final architecture.

# Used technologies

* Spring Boot
  * Flyway
  * JPA
  * JPA Entity Graph
  * ShapeShift (entity mapping)
* Spring Security
  * OAuth 
  * JWT
* Docker 
  * Docker Compose
  * Docker Volumes
* MySQL
* Maven
* Kotlin
* Postman ([click to see get Collection](./github/Bugtracker2.postman_collection.json))

# Setup

1. Git clone the project

```shell
git clone https://github.com/d-najd/Bugtracker-2.0-backend.git
```

2. Install [docker compose](https://docs.docker.com/compose/install/)
3. Run [docker-compose.yml](docker-compose.yml) in root directory
