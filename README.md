less4j-release-tests
====================

Less4j-release-tests project contains long running tests for [less4j](https://github.com/SomMeri/less4j). There is no reason to run them together with unit tests, but they should be run before each Less4j release.   

## Installation and Configuration
Less4j-release-tests works only if: 
* latest less4j code is stored in the same directory,
* git is installed and accessible from command line,
* maven is installed and accessible from command line,
* java is installed and accessible from command line.

## Test Case
### Twitter Bootstrap 
Compiles [Twitter Bootstrap](http://twitter.github.com/bootstrap/) v2.3.1 with both less-1.3.3.js and latest less4j. Checks generated css files for compatibility.

Compiled Bootstrap files:
* bootstrap/less/bootstrap.less
* bootstrap/less/responsive.less

Steps:
* clone or update Twitter Bootstrap repository,
* checkout tag,
* compile with less.js,
* compile with less4j,
* normalize and compare results. 

## Issues Tracking
Issues are tracked in Less4j [issues tracker](https://github.com/SomMeri/less4j/issues).

## TODO
* TODO: remove target directory
* TODO: add license 
* TODO: write readme
* TODO: add info into tests page on less4j
* TODO: tweet


