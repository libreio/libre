#libre

[![Made By Teamed.io](http://img.teamed.io/btn.svg)](http://www.teamed.io)

[![Build Status](https://travis-ci.org/libreio/libre.svg?branch=master)](https://travis-ci.org/libreio/libre)

Libre is a cloud storage platform that enables users to deploy a simple AWS gateway for storing and sharing files.

##Starting the application:

```
$ mvn clean package
$ java -cp target/libre.jar:target/deps/* com.libre.Launch --port=8080
```
##If you're on Windows:

```
java -cp target\libre.jar;target\deps\* com.libre.Launch --port=8080
```

##How to contribute

Fork repository, make changes, send us a pull request. We will review your changes and apply them to the `master` branch shortly, provided they don't violate our quality standards. Before sending us your pull request please run full Maven build:

```
$ mvn clean install -Pqulice,cobertura
```

Don't forget to set your default encoding to UTF-8. On Windows:

```
SET JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF-8
```

To avoid build errors use maven 3.2+. Minimum Java version is 1.7.

To start a web server locally run:

```
$ mvn clean integration-test -Phit-refresh -Dport=8080
```
If you have questions or general suggestions, don't hesitate to submit
a new [GitHub issue](https://github.com/libreio/libre/issues/new).
