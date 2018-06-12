# CODA

## Getting Started

### Prerequisites

- [Maven](https://maven.apache.org/)
- [Java SDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Tomcat 8](https://tomcat.apache.org/download-80.cgi)

### Installing

1. Run the following command:

```
mvn -U clean package
```

2. Move `target/coda-*-SNAPSHOT.war` to Tomcat webapps directory.

3. Start the Tomcat server. The .war file will be automatically extracted and deployed.

4. Browse to http://localhost:8080/SameAsValidator

## Built With

- [Jena](https://jena.apache.org/) - RDF Framework
- [Maven](https://maven.apache.org/) - Dependency Management

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Authors

- [Lilian Barraud](https://github.com/BarraudLilian)
