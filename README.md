Overview
========

This app has been developed for the Insight Data Engineering program (fall 2016). More information can be found here: https://github.com/InsightDataScience/coding-challenge).
<br/>
<br/>
Summary of the requirements:
----------------------------

- Use Venmo payments that stream in to build a graph of users and their relationship with one another.
- Calculate the median degree of a vertex in a graph and update this each time a new Venmo payment appears. You will be calculating the median degree across a 60-second sliding window.
- The vertices on the graph represent Venmo users and whenever one user pays another user, an edge is formed between the two users.

Example of the input data:

          actor = Jordan-Gruber,      target = Jamie-Korn,        created_time: 2016-04-07T03:33:19Z
          actor = Maryann-Berry,      target = Jamie-Korn,        created_time: 2016-04-07T03:33:19Z
          actor = Ying-Mo,            target = Maryann-Berry,     created_time: 2016-04-07T03:33:19Z
          actor = Jamie-Korn,         target = Ying-Mo,           created_time: 2016-04-07T03:34:18Z

<br/>
Implementation details
----------------------

<p>This app has been written in Scala, using IntelliJ IDEA.</p>
<p>A sbt project was created in IntelliJ. The source code of the sbt project can be found under /src/sbt_project <br/>
The main scala program can be found under /src/sbt_project/src/main/scala/media_degree.scala <br/></p>

Java version used: 

        java version "1.7.0_79"
        Java(TM) SE Runtime Environment (build 1.7.0_79-b15)

<p>Scala version: 2.11.8</p>

<p>A jar file was generated and placed under /src/. Name of the jar file is: insight_2016_intellij_2.11-1.0.jar </p>

<br/>
<br/>
Repo Structure
==============

    ├── README.md 
    ├── run.sh
    ├── src
    │       ├── insight_2016_intellij_2.11-1.0.jar
    │       ├── lib
    │       │      ├── joda-convert-1.2.jar
    │       │      ├── json4s-core_2.11-3.3.0.jar
    │       │      ├── nscala-time_2.11-2.12.0.jar
    │       │      ├── scala-xml_2.11-1.0.5.jar
    │       │      ├── joda-time-2.9.3.jar
    │       │      ├── json4s-native_2.11-3.3.0.jar
    │       │      ├── paranamer-2.8.jar
    │       │      ├── json4s-ast_2.11-3.3.0.jar
    │       │      ├── json4s-scalap_2.11-3.3.0.jar
    │       │      ├── scala-library-2.11.8.jar
    │       ├── sbt_project
    │           └── src
    │                   └── main
    │                         └── scala
    │                                └── media_degree.scala
    │
    │
    ├── venmo_input
    │   └── venmo-trans.txt
    ├── venmo_output
    │   └── output.txt
    └── insight_testsuite
           ├── run_tests.sh
           └── tests

<br/>
<br/>
Dependencies
============

Libraries used within the program:

> - joda-convert-1.2.jar
> - json4s-core_2.11-3.3.0.jar
> - nscala-time_2.11-2.12.0.jar
> - scala-xml_2.11-1.0.5.jar
> - joda-time-2.9.3.jar
> - json4s-native_2.11-3.3.0.jar
> - paranamer-2.8.jar
> - json4s-ast_2.11-3.3.0.jar
> - json4s-scalap_2.11-3.3.0.jar
> - scala-library-2.11.8.jar

These libraries have been placed under src/lib/ directory

<br/>
<br/>
Execution Steps:
================

1. Copy content of this repository to your local.

2. From the directory where you copied this repo content, execute “run.sh":

          $ bash run.sh

<br/>
<br/>
Tests:
======

Additional test cases have been added under /insight_testsuite.
Tests can be run by executing:  
           
          $ cd insight_testsuite
          $ bash run_tests.sh
<br/>
<br/>



