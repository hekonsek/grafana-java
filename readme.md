# Spring Boot Docker Spotify

[![Version](https://img.shields.io/badge/Spring%20Boot%20Docker%20Spotify-0.3-blue.svg)](https://github.com/hekonsek/spring-boot-docker-spotify/releases)
[![Build](https://api.travis-ci.org/hekonsek/spring-boot-docker-spotify.svg)](https://travis-ci.org/hekonsek/spring-boot-docker-spotify)

**Spring Boot Docker Spotify** provides template around [Spotify Docker client](https://github.com/spotify/docker-client) simplifying common
operations over Docker containers.

## Maven setup

I highly recommend to import spring-boot-docker-spotify BOM instead of Spring Boot BOM. Otherwise Jersey version defined in Spring Boot BOM will conflict 
with Jersey version used by Spotify Docker Client. spring-boot-docker-spotify BOM imports Spring Boot BOM on your behalf, but downgrades Jersey.

```
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>com.github.hekonsek</groupId>
      <artifactId>spring-boot-docker-spotify</artifactId>
      <version>0.3</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
</dependencyManagement>
```

Then add an actual spring-boot-docker-spotify jar into your classpath:

    <dependency>
      <groupId>com.github.hekonsek</groupId>
      <artifactId>spring-boot-docker-spotify</artifactId>
      <version>0.3</version>
    </dependency>

## Injecting Docker template

In order to use Docker template, just inject it into your bean of choice:

```
@Autowired
DockerTemplate dockerTemplate;
```

## Docker template operations

Executing Docker container and returning stdout+stderr as a list of Strings:

```
ContainerConfig container = ContainerConfig.builder().image("fedora:26").cmd("echo", "foo").build();
List<String> output = dockerTemplate.execute(container);
```