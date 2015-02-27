<img src="http://beta.nerodesk.com/img/logo.svg" width="64px" height="64px"/>

[![Made By Teamed.io](http://img.teamed.io/btn.svg)](http://www.teamed.io)
[![DevOps By Rultor.com](http://www.rultor.com/b/teamed/nerodesk)](http://www.rultor.com/p/teamed/nerodesk)

[![Build Status](https://travis-ci.org/teamed/nerodesk.svg?branch=master)](https://travis-ci.org/teamed/nerodesk)

It is a cloud file sharing system, similar to Dropbox. Our key difference
is that we're fully open source. Main features:

 - user can create an account via web panel
 - user can delete his account
 - user can upload a file, download it back and delete it
 - user can share any file with a few other users
 - user can see all his files in the web panel
 - the entire code will be open source
 - the web panel will be hosted on Heroku
 - objects/files will be stored in Amazon S3 (in the future will be changed to Amazon EC2)

To start application using default port use command line specified below;

```
$ java -jar target/nerodesk-jar-with-dependencies.jar
```

You might want to start this app on different port, just add port number as parameter;

```
$ java -jar target/nerodesk-jar-with-dependencies.jar 9000
```

## How to contribute

Fork repository, make changes, send us a pull request. We will review
your changes and apply them to the `master` branch shortly, provided
they don't violate our quality standards. To avoid frustration, before
sending us your pull request please run full Maven build:

```
$ mvn clean install -Pqulice
```

To avoid build errors use maven 3.2+

## Got questions?

If you have questions or general suggestions, don't hesitate to submit
a new [Github issue](https://github.com/teamed/nerodesk/issues/new).

