# highway-to-urhell

[![Build Status](https://travis-ci.org/highway-to-urhell/highway-to-urhell.svg)](https://travis-ci.org/highway-to-urhell/highway-to-urhell)

## Build

highway-to-urhell requires Java 7.

You can build it using Maven 3+ : 

`mvn install`

## How to use it

 * Unzip highway-to-url distribution in a directory of your choice
 * Configure agent in JAVA_OPTS (see above)

### config.properties
REMOTE send data to h2hell-web, MEMORY data in memory agent
```
outputSystem=REMOTE||MEMORY
```
url of your application
```
urlapplication=http://localhost:8080/core/
```
name ...
```
nameapplication=struts2Demoh2H
```
url of h2hell-web
```
urlh2hweb=http://localhost:8090/core/api/ThunderApp
```
### Configuring agent on  Unix machines : 
```
export JAVA_OPTS=$JAVA_OPTS -javaagent:/path/to/h2hell-agent.jar -Djava.ext.dirs=/path/to/h2h -DH2H_CONFIG=/path/to/file/config.properties
```

### Configuring agent on Windows machines :  
```
set JAVA_OPTS=%JAVA_OPTS% -javaagent:/path/to/h2hell-agent.jar -Djava.ext.dirs=/path/to/h2h -DH2H_CONFIG=/path/to/file/config.properties
```

### Configuring h2hell-web
```
mvn jetty:run -Djetty.port=8090
```
h2h.properties for changing : url of swagger, connexion to db, configuration hibernate