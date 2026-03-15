# Restaurant Reservation System

## Overview

This project is a web application for restaurant table reservation and smart table recommendation.

The user can search for a table by date, time, party size, zone, and preferences. The system then shows the restaurant floor plan, highlights already reserved tables, and recommends the most suitable available table based on the selected criteria.

## Features

### Implemented Features

* Search available tables by:

  * date
  * time
  * party size
  * zone
  * preferences:
    * window seat
    * quiet area
    * accessible
    * near kids area
* Visual restaurant floor plan
* Randomly generated reserved tables for demonstration
* Table recommendation logic
* Distinction between:
  * reserved tables
  * best recommendation
  * good alternatives
  * other available tables
* Selected table details panel
* Recommendation summary with reasons
* Reservation creation
* Validation for invalid requests
* Automated backend tests for core business logic


## Tech Stack

### Backend

* Java 21
* Spring Boot
* Spring Data JPA
* H2 in-memory database
* Maven

### Frontend

* React
* Vite
* JavaScript
* CSS

### Other

* Git
* GitHub


## Project Structure

```
restaurant-reservation-system
├── frontend
│   ├── src
│   │   ├── components
│   │   └── services
│   ├── index.html
│   └── package.json
│
├── src
│   ├── main
│   │   ├── java/ee.ut.jurits13.restaurant.backend
│   │   │   ├── controller
│   │   │   ├── service
│   │   │   ├── repository
│   │   │   ├── entity
│   │   │   ├── dto
│   │   │   ├── config
│   │   │   └── exception
│   │   └── resources
│   │       └── application.properties
│   │
│   └── test
│       └── service tests
│
├── http
│   └── API request examples
│
├── pom.xml
└── README.md

```


## How to Run

### Backend

Run the backend from the project root:

```
./mvnw spring-boot:run
```

or

```
mvn spring-boot:run
```

Backend runs on:

```
http://localhost:8080
```

H2 console:

```
http://localhost:8080/h2-console
```


### Frontend

Open another terminal:

```
cd frontend
npm install
npm run dev
```

Frontend runs on:

```
http://localhost:5173
```


## Running Tests

```
mvn test
```


## Recommendation Logic

Each available table gets a score based on:

1. Capacity fit

   * exact match = highest score
   * slightly larger = medium score
   * much larger = lower score

2. Preferences

   * matching preference increases score
   * missing preference reduces score

3. Availability

   * reserved tables are excluded

Search result returns:

* 1 best table
* 2 good alternatives
* other available tables


## Reservation Rules

* Each reservation lasts 2 hours
* Overlapping reservations are not allowed
* Party size must not exceed table capacity


## Validation

Bean Validation is used.

Examples:

* date required
* time required
* party size ≥ 1
* customer name required
* table id required

Invalid requests return 400 Bad Request.


## Tests

Tests cover:

* reservation overlap
* valid reservation
* recommendation scoring
* capacity preference
* reserved table exclusion

Run with:

```
mvn test
```


## Assumptions

* reservation duration = 2 hours
* layout is static
* reservations seeded randomly
* preferences affect score, not strict filter
* zone acts as filter
* no authentication required


## Limitations

* no login system
* no admin UI
* no table merging
* no persistent database
* fixed reservation duration
* static floor plan


## Possible Improvements

* Docker support
* Admin layout editor
* Table merging
* Persistent database
* Better UI styling
* Notifications
* User accounts


## Time Spent

Backend: ~8h
Frontend: ~4h
Logic + UI improvements: ~2h
Tests + cleanup + README: ~2h

Total: ~16 hours


## External Help

Used:

* Spring Boot docs
* React docs
* general web search
* AI assistance for debugging suggestions and documentation formatting
