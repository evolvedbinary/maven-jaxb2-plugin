# JAXB Maven Plugin

[![CI](https://github.com/evolvedbinary/maven-jaxb2-plugin/workflows/CI/badge.svg)](https://github.com/evolvedbinary/maven-jaxb2-plugin/actions?query=workflow%3ACI)
[![Maven Central](https://img.shields.io/maven-central/v/com.helger.maven/maven-jaxb2-plugin-project.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.helger.maven%22%20AND%20a:%22maven-jaxb2-plugin-project%22)
[![License](https://img.shields.io/badge/license-BSD%202-blue.svg)](https://opensource.org/licenses/BSD-2-Clause)

* **Supports JAXB 2 and JAXB 3.**

## Provenance
This is a fork of the JAXB2 Maven Plugin from [phax/maven-jaxb2-plugin](https://github.com/evolvedbinary/maven-jaxb2-plugin),
which is itself a fork on [highsource/maven-jaxb2-plugin](https://github.com/highsource/maven-jaxb2-plugin), and we suspect
that was itself a fork of the [javaee/metro-maven-jaxb2-plugin](https://github.com/javaee/metro-maven-jaxb2-plugin) which originated
from *java.net*.

The plugin from which this was forked has been commonly known as the: *"jvmnet JAXB2 Plugin"*.

The purpose of our fork is to add support for JAXB 3.

### Notes on Licensing
The forks from which this project came claim that it is licensed under a [BSD-2 Clause](https://opensource.org/licenses/BSD-2-Clause) license.
However, there is also evidence of Apache 2.0 licensed code within the project. The code in the original
*java.net* project appears instead to have been dual-licensed as [CDDL 1.1](https://spdx.org/licenses/CDDL-1.1.html) and [GPL 2 + Classpath Exception](https://openjdk.java.net/legal/gplv2+ce.html) license.

Whilst we believe it is likely that this code is Open Source and resides under a mix of the above discussed licenses, the exact licensing of
the code remains murky at best. 

## Introduction

Welcome to the `com.helger.maven:maven-jaxb2-plugin`, the most advanced and feature-full Maven plugin for XML Schema compilation.

This Maven plugin wraps and enhances the [JAXB](https://jaxb.java.net/) [Schema Compiler (XJC)](http://docs.oracle.com/javase/6/docs/technotes/tools/share/xjc.html) and allows
compiling XML Schemas (as well as WSDL, DTDs, RELAX NG) into Java classes in Maven builds.

> If you are interested in the Mojohaus JAXB2 Maven Plugin (`org.codehaus.mojo:jaxb2-maven-plugin`),
> please follow [this link](https://github.com/mojohaus/jaxb2-maven-plugin) to the corresponding website.

## Quick start

* Put your schemas (`*.xsd`) and bindings (`*.xjb`) into the `src/main/resources` folder.
* Add the plugin to your `pom.xml`:

```xml
<project ...>
  ...
  <build>
    <plugins>
      ...
      <plugin>
        <groupId>com.helger.maven</groupId>
        <artifactId>maven-jaxb2-plugin</artifactId>
        <version>0.14.4</version>
        <executions>
          <execution>
            <goals>
              <goal>generate</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
      ...
    </plugins>
  </build>
  ...
</project>
```

### JAXB Versions

If you need a specific JAXB version, you can explicitly use one of the following variants:

* `com.helger.maven:maven-jaxb22-plugin` - JAXB 2.2.
* `com.helger.maven:maven-jaxb23-plugin` - JAXB 2.3.
* `com.helger.maven:maven-jaxb30-plugin` - JAXB 3.0.
* `com.helger.maven:maven-jaxb2-plugin` - "latest 2.x version", at the moment same as `com.helger.maven:maven-jaxb23-plugin`.

### Java versions

Supported Java versions are `1.8` and onwards.

## [Documentation](https://github.com/highsource/maven-jaxb2-plugin/wiki)

Please refer to the [Wiki](https://github.com/highsource/maven-jaxb2-plugin/wiki) for the full documentation.

* [User Guide](https://github.com/highsource/maven-jaxb2-plugin/wiki/User-Guide)
* Maven Documentation  (Work in progress)
* [Configuration Cheat Sheet](https://github.com/highsource/maven-jaxb2-plugin/wiki/Configuration-Cheat-Sheet)
* [Common Pitfalls and Problems](https://github.com/highsource/maven-jaxb2-plugin/wiki/Common-Pitfalls-and-Problems) (Work in progress)
* [Best Practices](https://github.com/highsource/maven-jaxb2-plugin/wiki/Best-Practices) (Work in progress)
* [FAQ](https://github.com/highsource/maven-jaxb2-plugin/wiki/FAQ)
* [Sample Projects](https://github.com/highsource/maven-jaxb2-plugin/wiki/Sample-Projects)
* [Support](https://github.com/highsource/maven-jaxb2-plugin/wiki/Support)
* [License](https://github.com/highsource/maven-jaxb2-plugin/blob/master/LICENSE)