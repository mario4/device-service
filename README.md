# Devices-API

> This project is a simple Java application that implements a REST-API that can register devices in a network deployment represented in a tree structure.

---

## Table of Contents
- [Devices-API](#devices-api)
  - [Table of Contents](#table-of-contents)
  - [Features](#features)
  - [Installation](#installation)
    - [Clone the repository](#clone-the-repository)
  - [Build](#build)
  - [Run the application](#run-the-application)
    - [Prerequisites](#prerequisites)
  - [Testing](#testing)
  - [Usage](#usage)

---

## Features
- REST API 
- Unit and End-to-End Tests  

---



---

## Installation
### Clone the repository

```bash
git clone https://github.com/mario4/device-service.git

cd device-service

```

## Build 

./gradlew build

## Run the application

### Prerequisites

- Java 17+  
- Gradle  
- Browser to access Swagger UI
./gradlew bootRun

```bash
./gradlew bootRun
```

alternatively, download the binaries from the latest release, unzip them and run the application by executing the command below:

```bash

cd <unzipped binary folder>

 ./bin/app --server-port=8081 
 
```

If the default port 8080 is already assigned to another service, use another port like so:

```bash
 ./bin/app --server-port=8081 
```


## Testing

`./gradlew test`

## Usage 

After running the application, head over to http://localhost:8080/swagger-ui/index.html#/ in the browser.
