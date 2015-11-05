# dg-toolkit

[![Build Status](https://travis-ci.org/devgateway/dg-toolkit.svg?branch=master)](https://travis-ci.org/devgateway/dg-toolkit)

This project has the aim of creating a boilerplate template for small and medium sized DG projects.

It is created as a mavenized multi-module project. Each module can be started independently of the rest. All modules are based on [Spring Boot](http://projects.spring.io/spring-boot/) templates. Modules currently present:

# Modules

- [persistence](https://github.com/devgateway/dg-toolkit/tree/master/persistence) - this is a module responsible with [JPA 2.0](https://en.wikipedia.org/wiki/Java_Persistence_API) data persistence. It is also provides [HATEOAS](https://en.wikipedia.org/wiki/HATEOAS) services on top of the existing entities.

- [web](https://github.com/devgateway/dg-toolkit/tree/master/web) - this module provides REST endpoints for the services needed, as well as basic security. It depends on the **persistence** module.

- [forms](https://github.com/devgateway/dg-toolkit/tree/master/forms) - this module provides a basic toolkit for quickly building forms over the entities defined in the persistence module. It uses [Apache Wicket](http://wicket.apache.org/) as the backend.

- [reporting](https://github.com/devgateway/dg-toolkit/tree/master/reporting) - this module provides reporting services - generation of static reports that can be exportable in XLS, PDF, DOC and HTML. It uses [Pentaho Reporting Classic](http://community.pentaho.com/projects/reporting/) suite. This does NOT include Mondrian as a backend. It currently depends on the **forms** module because the reporting framework require filters which are defined using the forms components.

- [ui](https://github.com/devgateway/dg-toolkit/tree/master/ui) - this module is a template for building front-end functionality. It is supposed to work in conjunction with the **web** module as the back-end. It is based on [React](https://facebook.github.io/react/) and [NuclearJS](https://optimizely.github.io/nuclear-js/). The Maven build integration is assured by [frontend-maven-plugin](https://github.com/eirslett/frontend-maven-plugin) which invokes [npm](https://www.npmjs.com/) and [webpack](https://webpack.github.io/). The UI module can be used by both UI developers, with no need of knowledge or Java/Maven local setup and by the back-end developers, with no need to have [node](https://nodejs.org/) installed on their system.

# Building

As prerequisites, we need Java 8 and Maven 3.3.x.
In order to build the project, invoke:

```
mvn install
```

inside the root project folder.

# Debugging

You can import dg-toolkit as a Maven project inside your favorite IDE, it should work in IDEA/STS/Eclipse but you will need Maven 3.3.x.

## Debugging in STS

Since all the modules are Spring Boot, debugging in STS is really easy. In Spring Boot 1.3 and later there is something called [spring-boot-devtools](https://spring.io/blog/2015/06/17/devtools-in-spring-boot-1-3).
This dependency is already included in the pom.xml of the modules, uncomment it if you plan to use it. If you do, then in STS you can do Debug As->Spring DevTools Client.

If you have JRebel license, then don't use spring-boot-devtools. Best is to start the modules by invokingn Debug As->Spring Boot App

## Debugging fat jars

[Fat jars](http://docs.spring.io/spring-boot/docs/current/reference/html/howto-build.html) are the way to go when it comes to java micro-services these days. While the pre-built maven project can be easily debugged as described above, a already packaged jar requires remote debugging. You can start the fat jar with remote debugging parameters by invoking:

`java -Xverify:none -noverify -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005 -jar fat.jar`

... And then for example in STS you can add a new Debug Configuration->Remote Java Application and specify localhost and port 5005.
