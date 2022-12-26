# MusalaSoft Technical Test
Technical test for Musala Soft recruitment

## NB

1. All commands indicated below must be run from the root of the project directory, on your terminal.
2. All values denoted by `${value}` are variables whose values should be substituted.

## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.musala.drones.DronesApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

The application starts on port `8080`

## Building the application

You can run the command below to generate a JAR file (in the `target` directory):

```shell
mvn clean install
```

## Running the application from the JAR file

```shell
java -jar target/drones-0.0.1-SNAPSHOT.jar
```

The application also starts on port `8080`

## Testing the application endpoints

### Finding preloaded data

Preloaded data can be found in the `src/main/resources/data.sql` file. You can find drone serial numbers or medication codes that you can use to perform tests.

### Base application endpoint
`var baseUrl = http://localhost:8080/api`

### Register a drone
To register a drone, you need to provide data in the following JSON format:

```
{
    "model": "Heavyweight",
    "weightLimit": 20,
    "batteryCapacity": 20,
    "state": "IDLE"
}
```
Send this data to `${baseUrl}/drones` in a `POST` request

### Load a drone with medication items
To load a drone with medication items, pass an array of medication item codes to the endpoint 
`${baseUrl}/drones/${droneSerialNumber}/medications`
in a `POST` request.
Example request body:

```
[
    "ABC_4",
    "ABC_5"
]
```

### Check loaded medication items for a given drone
To check the loaded medication items for for a given drone send a `GET` request to the following endpoint:
`${baseUrl}/drones/${droneSerialNumber}/medications`

### Check available drones for loading
To find available for loading (so drones in `IDLE` state), send a `GET` request to the following endpoint:
`${baseUrl}/drones?state=IDLE`

### Check drone battery level for a given drone
To check the battery level of a given drone, send a `GET` request to the following endpoint:
`${baseUrl}/drones/${droneSerialNumber}`

### Upload a medication image
You can upload an image for a medication you intend to save by sending a `POST` request with the header `Content-Type: multipart/form-data` to the following endpoint:
`${baseUrl}/medications/upload-image`.
The key for the image file should be `image`.
The response for this request contains the uploaded image's URL.

### Save a medication
To save a medication, you need to provide data in the following JSON format:

```
{
    "code": "AB_C4",
    "name": "AB_C-4",
    "weight": 10,
    "image": "${imageUrl}"
}
```
Send this data to `${baseUrl}/medications` in a `POST` request.

### List medications
To list saved medications, send a `GET` request to the following endpoint:
`${baseUrl}/medications`

## Executing unit tests

To execute the tests in an IDE such as SpringToolSuite4, right click on the root project directory (`drones`) -> select `Run As` -> select `JUnit Test`.
