<img src="http://www.nerodesk.com/logo_large.png" width="384px" height="85px"/>

[![Made By Teamed.io](http://img.teamed.io/btn.svg)](http://www.teamed.io)
[![DevOps By Rultor.com](http://www.rultor.com/b/nerodesk/nerodesk)](http://www.rultor.com/p/nerodesk/nerodesk)

[![Build Status](https://travis-ci.org/nerodesk/nerodesk.svg?branch=master)](https://travis-ci.org/nerodesk/nerodesk)

Open-Source Cloud Storage

Main features:

 - user can create an account via web panel
 - user can delete their account
 - user can upload a file, download it back and delete it
 - user can upload multiple files in a single batch.
 - user can share any file with a few other users
 - user can see all their files in the web panel
 - user can manage file folders
 - user can share files with shortened URLs
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

### File and Folder Visibility

By default, files and folders are private, i.e. visible only to the owner. However, file
and folder visibility may be set to public by the owner. Shared folders will
automatically cascade their visibility to any contained files and subfolders.

## Limitations

**File Size**.
Maximum size of the size a user can upload is 5Tb, because this is the
maximum size of Amazon S3 object, see http://aws.amazon.com/s3/faqs/.


## Graphic design
Resized [nerodesk logo](http://www.nerodesk.com/logo_large.png) should be placed
on top of all site pages (centered horizontally), just above site information
and login/logout link.


## How to contribute

Fork repository, make changes, send us a pull request. We will review
your changes and apply them to the `master` branch shortly, provided
they don't violate our quality standards. To avoid frustration, before
sending us your pull request please run full Maven build:

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

## Got questions?

If you have questions or general suggestions, don't hesitate to submit
a new [Github issue](https://github.com/nerodesk/nerodesk/issues/new).
