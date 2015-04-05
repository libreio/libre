<img src="http://www.nerodesk.com/logo.png" width="320px" height="45px"/>

[![Made By Teamed.io](http://img.teamed.io/btn.svg)](http://www.teamed.io)
[![DevOps By Rultor.com](http://www.rultor.com/b/teamed/nerodesk)](http://www.rultor.com/p/teamed/nerodesk)

[![Build Status](https://travis-ci.org/teamed/nerodesk.svg?branch=master)](https://travis-ci.org/teamed/nerodesk)

Open-Source Cloud Storage

Main features:

 - user can create an account via web panel
 - user can delete their account
 - user can upload a file, download it back and delete it
 - user can upload multiple files in a single batch.
 - user can share any file with a few other users
 - user can see all their files in the web panel
 - the entire code will be open source
 - the web panel will be hosted on Heroku
 - objects/files will be stored in Amazon S3 (in the future will be changed to Amazon EC2)

To start application using default port use command line specified below:

```
$ mvn clean package
$ java -cp target/nerodesk.jar:target/deps/* com.nerodesk.Launch --port=8080
```

If you're on Windows:

```
java -cp target\nerodesk.jar;target\deps\* com.nerodesk.Launch --port=8080
```

## Limitations

**File Size**.
Maximum size of the size a user can upload is 5Tb, because this is the
maximum size of Amazon S3 object, see http://aws.amazon.com/s3/faqs/.

## How to contribute

Fork repository, make changes, send us a pull request. We will review
your changes and apply them to the `master` branch shortly, provided
they don't violate our quality standards. To avoid frustration, before
sending us your pull request please run full Maven build:

```
$ mvn clean install -Pqulice,cobertura
```

To avoid build errors use maven 3.2+. Minimum Java version is 1.7.

To start a web server locally run:

```
$ mvn clean integration-test -Phit-refresh -Dport=8080
```

## Got questions?

If you have questions or general suggestions, don't hesitate to submit
a new [Github issue](https://github.com/teamed/nerodesk/issues/new).
