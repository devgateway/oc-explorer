# dg-toolkit web module

This module provides REST endpoints for the services needed, as well as basic security. It depends on the **persistence** module.

It also provides full authentication and security using Spring Security.
The module is packaged as a jar and can be deployed as a [fat jar](http://docs.spring.io/spring-boot/docs/current/reference/html/howto-build.html).

# Building

The web module is part of its larger dg-toolkit parent build, so you need to build on the parent first.

# Starting the app

Because it gets packaged as a fat jar, starting it is piece of cake:

`java -jar target/web-0.0.1-SNAPSHOT.jar`

This will start everything, including an embedded Tomcat Web server and all the services attached it.
