# highway-to-urhell

[![Build Status](https://travis-ci.org/highway-to-urhell/highway-to-urhell.svg)](https://travis-ci.org/highway-to-urhell/highway-to-urhell)

## Build

highway-to-urhell requires Java 7.

You can build it using Maven 3+ : 

`mvn install`

## How to use it

 * Unzip highway-to-url distribution in a directory of your choice
 * Configure agent in JAVA_OPTS (see above)

### Configuring agent on Windows machines :  
```
set JAVA_OPTS=%JAVA_OPTS -javaagent:/path/to/h2hell-agent.jar -Djava.ext.dirs=/path/to/h2h
```

### Configuring agent on  Unix machines : 
```
export JAVA_OPTS=$JAVA_OPTS -javaagent:/path/to/h2hell-agent.jar -Djava.ext.dirs=/path/to/h2h
```

## Contributing

Contributions are welcome, fork the repo, push your changes to a branch and send a Pull Request, or create an issue on GitHub to initiate the discussion.
