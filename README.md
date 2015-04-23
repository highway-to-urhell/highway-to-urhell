# highway-to-URhell

[![Build Status](https://travis-ci.org/highway-to-urhell/highway-to-urhell.svg)](https://travis-ci.org/highway-to-urhell/highway-to-urhell)

## H2H

H2H is a component that detects entry points paths in your project.

### H2H - Java (JVM)

For a JVM (Java virtual machine), H2H is a javagent. This Java agent will scan your application at runtime for each component and framework.

H2H supports (for Java)
* JSF 2
* Spring
* Struts 1
* Struts 2
* JEE & JAX-RS
* RestX (in progress)
* Jersey (in progress)

H2H comes with a webapp that will collect and present all detected entry points in applications that subscribed to H2H service.

### H2H - PHP

H2H php agent must be copied in PHP project. We now support
* Symfony (PHP)
* Zend (in progress)

For PHP technology you can see the entries path via url : http://server:port/uriRoot/h2h/checkPath/

### H2H - NodeJS

We are working with Nashorn to fully support NodeJS application. Supported frameworks are
* Express - NodeJS (in progress)
* SailJS - NodeJS (in progress)

Whatever the technology, if you want centralize the results, you can live monitor results (entry points) and save data. Please have a look to h2hell-web.

## H2H - Web

H2H-Web is a web application designed to
* Display all entry paths for each application
* Monitor activity for each entry point
* Save each pentesting

Demo : http://62.210.222.197:8180/h2hell-web/

Tips :
* Launch real application : "Launch app" icon
* "See statistics" to have a precise view of all detected entry points


## Build Java Agent

highway-to-urhell requires Java 7.

You can build it using Maven 3+ :

`mvn install`

### How to use it

 * Unzip highway-to-url distribution in a directory of your choice
 * Configure agent in JAVA_OPTS (see above)

### Add file in your application : config.properties
REMOTE send data to h2hell-web, MEMORY data in memory agent
```
outputSystem=REMOTE or Memory
urlapplication=url of your application reachable for H2H-web
nameapplication=name
urlh2hweb=http://server-h2h-web:port/core/api/ThunderApp (only REMOTE)
description=
pathSource=path to source
versionApp=version
```
### Configuring agent on  Unix machines :
```
export JAVA_OPTS=$JAVA_OPTS -javaagent:/path/to/h2hell-core.jar -Djava.ext.dirs=/path/to/h2h -DH2H_CONFIG=/path/to/file/config.properties
```

### Configuring agent on Windows machines :  
```
set JAVA_OPTS=%JAVA_OPTS% -javaagent:/path/to/h2hell-core.jar -Djava.ext.dirs=/path/to/h2h -DH2H_CONFIG=/path/to/file/config.properties
```

## Configuration for PHP SYMFONY - beta version

* Copy H2h in src/
* mv H2h/Controller/H2hController to path/src/Controller
* add in Resources/config/routing.yml
```
_h2h:
    resource: "@AcmeDemoBundle/Controller/H2hController.php"
    type:     annotation
```
* Start symfony (example php app/console server:run)
* Launch the viewer http://host:port/h2h/checkPath
* Launch the transformer http://host:port/h2h/launch
* Change the hostandport for h2hserver

## Launch h2hell-web
Juste type
```
mvn
```
in h2hell-web directory.

You can edit h2h.properties to change : Swagger url , DB connexion, Hibernate configuration
