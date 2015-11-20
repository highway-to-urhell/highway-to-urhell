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
* RMI server
* Scan port
* JMS 1.1
* ActiveMQ (JMS only)
* JMS 2.0 (in progress)
* RestX (in progress)


H2H comes with a webapp that will collect and present all detected entry points in applications that subscribed to H2H service.

## Build Java Agent

highway-to-urhell requires Java 7.

You can build it using Maven 3+ :

`mvn install`

## Download the last release

You can download the last release here : https://github.com/highway-to-urhell/highway-to-urhell/releases/download/1.0.4/h2hell-distribution-1.0.4-1.zip

The file config.properties is in the distribution.

### How to use it
 * Download the last release on github
 * Unzip highway-to-url distribution in a directory of your choice

### Create configuration file : config.properties
The configuration file define the parameter for the agent.
There's two configurations  :
* MEMORY (data are located into the agent)
* REMOTE send data to h2hell-web (see the project h2h-web)
Structure of the property file :
```
outputSystem=REMOTE or Memory
timer=REMOTE (if outputSystem is REMOTE)
performance=false||true (false by default)
urlapplication=url of your application reachable for H2H-web
nameapplication=name
urlh2hweb=http://server-h2h-web:port/h2hell-web/api/ThunderApp (only REMOTE)
description=
pathSource=path to source
versionApp=version
```
You can see example file in h2h-example project.

## Configuration via JAVA_OPTS

### Configuring agent on  Unix machines :
```
export JAVA_OPTS=$JAVA_OPTS -javaagent:/path/to/h2hell-core.jar -Djava.ext.dirs=/path/to/h2h -DH2H_CONFIG=/path/to/file/config.properties
```

### Configuring agent on Windows machines :  
```
set JAVA_OPTS=%JAVA_OPTS% -javaagent:/path/to/h2hell-core.jar -Djava.ext.dirs=/path/to/h2h -DH2H_CONFIG=/path/to/file/config.properties
```

### Configuration for tomcat
add setnv.sh in your_tomcat\bin\setenv.sh
```
export JAVA_OPTS="-javaagent:your_path/h2hell-distribution/h2hell-core.jar -Djava.ext.dirs=your_path/h2hell-distribution/ -DH2H_CONFIG=your_path/h2hell-distribution/config.properties -DH2H_PATH=your_path_tomcat/webapps/your_application/"

```
### Configuration for IBM WAS 
1. Log into the admin console.
2. Select Servers > Application servers > (selected server).
3. Select Configuration > Service Infrastructure > Java and Process Management > Process Definition > Additional Properties.
4. From the Process Definition > Additional Properties, select Java Virtual Machine.
5. On the Java Virtual Machine page, in the Generic JVM arguments textbox, add the -javaagent and paths.
```
-javaagent:your_path/h2hell-distribution/h2hell-core.jar -Djava.ext.dirs=your_path/h2hell-distribution/ -DH2H_CONFIG=your_path/h2hell-distribution/config.properties -DH2H_PATH=your_path_tomcat/webapps/your_application/
```
6. Select Apply, then select Save.
7. Restart your server.

add setnv.sh in your_tomcat\bin\setenv.sh

## Vizualisation
### Mode MEMORY
* Call the url for JSON result http://host:port/root_uri_of_your_application/h2h
* Call the url for HTML result http://host:port/root_uri_of_your_application/h2h?customGeneratorClass=com.highway2urhell.generator.impl.HTMLGenerator 
* Call the url for File result http://host:port/root_uri_of_your_application/h2h?customGeneratorClass=com.highway2urhell.generator.impl.FileGenerator 

### Mode Remote
* See H2H-Web project


## Troubleshooting
If your application server doesn't support servlet 3+, add the filter h2h (on the top) in web.xml of your application
```
 		<filter>
                <filter-name>h2h</filter-name>
                <filter-class>com.highway2urhell.filter.H2hellFilter</filter-class>
        </filter>

        <filter-mapping>
                <filter-name>h2h</filter-name>
                <url-pattern>/h2h/*</url-pattern>
        </filter-mapping>

```

## Add custom entry point

For add new entry point because we can't support your prefer framework you must create 2 services (class) : first retrieve the entrypoint and the second treat the list of entrypoint.

The first service must extends com.highway2urhell.transforme.AbstractLeechTransformer. 
You must implements doTransform with 2 requirements :
* Create the list of entrypoint ArrayList<EntryPathData>
* Send the data to CoreEngine with CoreEngine.getInstance().getFramework(your_framework).receiveData(listEntryPath) 
You must implements the constructor like :
```
super(package_class_modified_by_agent);// example "com/google/gwt/user/server/rpc/RemoteServiceServlet" 
addImportPackage(String... packages);// list package add by Agent for running the agent with your modification. example "java.lang.reflect", "java.util","org.reflections", "org.reflections.util", "java.util.Map"
```
For example, we recommand to see the actual transformer in package com.highway2urhell.transformer.

The second service must extends com.highway2urhell.service.AbstractLeechService with 1 requirement :
* The constructor must invoke like 
```
super(FRAMEWORK_NAME, VersionUtils.getVersion("package_class","groupid", "artifactId"));
```

## Add custom vizualisation

For add your custom vizualisation you must : 
* implements the interface com.highway2urhell.generator.TheJack
* add your class in highway-to-url distribution

## Supports
[![JetBrains](https://www.jetbrains.com/company/docs/logo_jetbrains.png?raw=true)](https://www.jetbrains.com/webstorm/)
- Thanks to JetBrains for providing licenses to build this project.
