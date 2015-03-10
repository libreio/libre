<img src="http://www.nerodesk.com/logo.png" width="320px" height="45px"/>

[![Made By Teamed.io](http://img.teamed.io/btn.svg)](http://www.teamed.io)
[![DevOps By Rultor.com](http://www.rultor.com/b/teamed/nerodesk)](http://www.rultor.com/p/teamed/nerodesk)

[![Build Status](https://travis-ci.org/teamed/nerodesk.svg?branch=master)](https://travis-ci.org/teamed/nerodesk)

Nerodesk is an Open-Source Cloud Storage service.

 - File management system with uploads, downloads, and secure file deletion
 - Share and tag files with other Nerodesk users
 - User messaging and file commenting
 - & more

To start application using default port use command line specified below:

```
$ java -jar target/nerodesk-jar-with-dependencies.jar
```

You might want to start this app on different port, just add port number as parameter:

```
$ java -jar target/nerodesk-jar-with-dependencies.jar 9000
```

## How to contribute

Fork repository, make changes, send us a pull request. We will review
your changes and apply them to the `master` branch shortly, provided
they don't violate our quality standards. To avoid frustration, before
sending us your pull request please run full Maven build:

```
$ mvn clean install -Pqulice,cobertura
```

To avoid build errors use maven 3.2+

## Got questions?

If you have questions or general suggestions, don't hesitate to submit
a new [Github issue](https://github.com/teamed/nerodesk/issues/new).

