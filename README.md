<img src="http://www.nerodesk.com/logo_large.png" width="384px" height="85px"/>

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
 - user can manage file folders
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

### File and Folder Sharing

By default, files and folders are visible only to the owner. However, files and folder
permissions may be set by the owner. He can make them available to other users as being
either read-only or read/write. He choose which users have access, or even make the file
or folder publicly available. Shared folders will automatically cascade their sharing
permissions to any files and subfolders.

Users can also generate download links to any files that they own. This download link
provides read-only access to files and can be used to share files with non-users, e.g.
over email, Facebook, or anywhere else that links can be shared.

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

To avoid build errors use maven 3.2+. Minimum Java version is 1.7.

To start a web server locally run:

```
$ mvn clean integration-test -Phit-refresh -Dport=8080
```

## Got questions?

If you have questions or general suggestions, don't hesitate to submit
a new [Github issue](https://github.com/teamed/nerodesk/issues/new).
