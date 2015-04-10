# highway-to-urhell

[![Build Status](https://travis-ci.org/highway-to-urhell/highway-to-urhell.svg)](https://travis-ci.org/highway-to-urhell/highway-to-urhell)

## H2H

H2H is a component for detecting entries paths in your project.

### H2H - JVM

For an application JVM, H2h is a javagent. The Javaagent scan application at runtime for each component and framework.

H2H supports for Java 
* JSF 2 
* Spring 
* Struts 1 
* Struts 2 
* JEE & JAX-RS 
* RestX (in progress)
* Jersey (in progress)

For Java technology you can see the entries path via url : http://server:port/uriRoot/h2h/?classGenerate=io.highway.to.urhell.generator.impl.HTMLGenerator

### H2H - PHP

For an application, we must copy the source into your project. Actually we support only Symfony actually.
* Symfony (PHP)
* Zend (in progress)

For PHP technology you can see the entries path via url : http://server:port/uriRoot/h2h/checkPath/

### H2H - NodeJS

For an application NodeJS we work actually with Nashorn for integration
* Express - NodeJS (in progress)
* SailJS - NodeJS (in progress)

Whatever the technology if you want centralize the results, monitore in live each entry point and save the data you can use H2H-Web (see docs)

## H2H - Web

H2H-Web is a web application for
* Display the all entries path for each application
* Monitoring activity for each entry point
* Save each pentesting.

Demo : http://62.210.222.197:8180/h2hell-web/
Tips : 
* Launch display the real application
* Stat display all entries path


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

## Configuring h2hell-web
```
mvn jetty:run -Djetty.port=8090
```
h2h.properties for changing : url of swagger, connexion to db, configuration hibernate

