<img src="http://www.nerodesk.com/logo_large.png" width="384px" height="85px"/>

[![Made By Teamed.io](http://img.teamed.io/btn.svg)](http://www.teamed.io)
 [![DevOps By Rultor.com](http://www.rultor.com/b/Nerodesk/nerodesk)](http://www.rultor.com/p/Nerodesk/nerodesk) [![Build Status](https://travis-ci.org/Nerodesk/nerodesk.svg?branch=master)](https://travis-ci.org/Nerodesk/nerodesk)

Fast, Simple, Open-Source Cloud Storage.

[www.nerodesk.com](http://www.nerodesk.com) | [twitter.com/nerodesk](https://twitter.com/nerodesk)

![nerodesk](http://www.nerodesk.com/github_readme/basic_panel.jpg)

To start application using default port use command line specified below:

```
$ mvn clean package
$ java -cp target/nerodesk.jar:target/deps/* com.nerodesk.Launch --port=8080
```
If you're on Windows:

```
java -cp target\nerodesk.jar;target\deps\* com.nerodesk.Launch --port=8080
```

## How to contribute

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
a new [GitHub issue](https://github.com/nerodesk/nerodesk/issues/new).

## License & Copyright

Copyright (c) 2015, nerodesk.com. All rights reserved.

License information can be found [here](https://github.com/Nerodesk/nerodesk/blob/master/LICENSE.txt).
