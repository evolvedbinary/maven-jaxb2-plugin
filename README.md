# JAXB2 Maven Plugin

## This is a fork of https://github.com/highsource/maven-jaxb2-plugin

Forked from 0.14.1-SNAPSHOT

I created it because of https://github.com/highsource/maven-jaxb2-plugin/issues/173

I kicked JAXB 2.0 and 2.1 because I don't need it

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
        <version>0.14.3</version>
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
* `com.helger.maven:maven-jaxb2-plugin` - "most actual version", at the moment same as `com.helger.maven:maven-jaxb23-plugin`.

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

## Disclaimer

This project is not developed, supported or in any other way affiliated with Apache. The `org.jvnet.jaxb2.maven2:maven-jaxb2-plugin` is not an Apache product (and does not pretend to be one), it is a completely independent development.

This project is also *not* developed by or affiliated with Oracle or Sun. Even if it is featured on [https://jaxb.java.net/](https://jaxb.java.net) pages, 

**This plugin is in no way _official_ JAXB2 Maven plugin by Sun or Oracle.**

This is a completely independent development. [I](https://github.com/highsource) am *not* an Oracle employee.
