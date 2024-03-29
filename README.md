# Yildiz-Engine common-compress

This is the official repository of The Common compression library, part of the Yildiz-Engine project.
The common compression library is a set of utility and helper classes to handle easily compression and decompression of files.

## Features

* Zip compression and decompression.
* 7z compression and decompression.
* Archive file info list(hashes).
* ...

## Requirements

To build this module, you will need a java 17 JDK and Maven 3.

## Coding Style and other information

Project website:
https://engine.yildiz-games.be

Issue tracker:
https://github.com/yildiz-online/common-compression/issues

## License

All source code files are licensed under the permissive MIT license
(http://opensource.org/licenses/MIT) unless marked differently in a particular folder/file.

## Build instructions

Go to your root directory, where your POM file is located.

Then invoke maven

	mvn clean install

This will compile the source code, then run the unit tests, and finally build a jar file.

## Usage

In your maven project, add the dependency

```xml
<dependency>
    <groupId>be.yildiz-games</groupId>
    <artifactId>common-compression</artifactId>
    <version>2.4.7</version>
</dependency>
```

## Contact
Owner of this repository: Grégory Van den Borre
