# jOCDS - The Java based Open Contracting Data Standard (OCDS) Validator

## Introduction

Open Contracting Data Standard (OCDS) is an emerging JSON based standard to track
contracting and tendering processes in a standardized format.
As the number of parties and institutions that are publishing tendering
and contracting data into the OCDS format is growing, there is a great need for
various tools to better validate and help with data standard compliance.

This need is amplified by the existence of many extensions to the standard,
some official and many that have been contributed by the community
but also by the several version releases of the standard itself, which is still in
active development. Vendors and institions will sometimes choose to export into an
OCDS version (say 1.0.2) and expect tools to comply with this exact version.
Updates to latest standard format are sometimes not possible due to difficulties
financing support contracts to keep the export feeds always up to date.

Although the OCDS team makes great efforts to keep the standard backwards compatible,
it is not always 100% possible to respect this since human error can and should be
addressed and sometimes the nature of the fixes may have proprity over
backwards compatibility.

## Meet jOCDS

jOCDS is a Java implementation of an OCDS Validator tool.

Key features:
- one single file, java based
- can be started as a web server and use as a JSON REST API service to validate
incoming json data (see validator-web project).
- can be started from the command line and used as a command tool to validate OCDS
files and URLs.
- works in offline mode with all OCDS versions including the minor and patch versions.
Supported formats include 1.0.0, 1.0.1, 1.0.2, 1.1.0, 1.1.1
- supports in offline mode all OCDS core extensions
- supports any other kind of extension that can be given as a URL
- perfoms extension compatiblity check, verifying if an extension is compliant or
not with the OCDS standard
- autodetects OCDS version from the release file, or enforces a specific OCDS version.
This can be helpful to check for backwards or forwards compatibility issues before
an actual data migration to the new OCDS version is performed
- works with release entities alone or in release packages
- caches all schemas after the given extensions are applied, therefore one should
expect good performance when validating large amounts of data, for example when the
tool is used as a web-based validator
- can be used as part of the OC Explorer suite of tools, or as a separate standalone tool

## Architecture

jOCDS is designed as a Spring service. It is packcaged as a Spring Boot
[fat executable JAR](https://docs.spring.io/spring-boot/docs/current/reference/html/howto-build.html#howto-create-an-executable-jar-with-maven)
which does not have any dependencies other than Java Runtime Edition (JRE) installed
on the host machine and can be started from the command line.




