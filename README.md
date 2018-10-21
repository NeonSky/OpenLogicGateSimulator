Open Logic Gate Simulator
=========================

[![Build Status](https://travis-ci.com/NeonSky/OpenLogicGateSimulator.svg?token=XyHcbxehB8TtpGq4DuFW&branch=dev)](https://travis-ci.com/NeonSky/OpenLogicGateSimulator)

# Importing the repo to IntelliJ

You will need to have the Lombok Plugin installed, and the Gradle and JUnit plugins are highly recommended.

1. Clone the repository.
2. Start IntelliJ. From the welcome screen, select *Import Project*. Locate and select the `build.gradle` file in the repository root, then press *OK*.
3. Click *OK* in the Import Project from Gradle window.
4. If asked about overwriting the `.idea` project file, select *Yes*.

The project is now imported into IntelliJ.

# Executing
## Running the program
To be able to run the project, we need to add a run configuration.

1. From the menu select *Run->Edit configurations*. Click the plus (+) button and select Gradle.
2. Set the following values:
   * *Name*: OpenLogicGateSimulator
   * *Gradle project*: Press the folder icon and select the OpenLogicGateSimulator project.
   * *Tasks*: `run` (autocompletion should list this task).
3. Press *Apply*, then *OK*. You can now select the configuration and run it.

## Running all checks (including unit tests)
Instructions for setting up and running the check gradle task:  

1. From the menu select *Run->Edit configurations*. Click the plus (+) button and select Gradle.
2. Set the following values:
   * *Name*: OLGS checks
   * *Gradle project*: Press the folder icon and select the OpenLogicGateSimulator project.
   * *Tasks*: `clean check`.
3. Press *Apply*, then *OK*. You can now select the configuration and run it.

Reports from the different tools can be found in build/reports/. 

## Generating coverage reports in HTML format
Instructions for setting up and running the tests and generating a report:  

1. From the menu select *Run->Edit configurations*. Click the plus (+) button and select Gradle.
2. Set the following values:
   * *Name*: OLGS coverage report
   * *Gradle project*: Press the folder icon and select the OpenLogicGateSimulator project.
   * *Tasks*: `test jacocoTestReport`.
3. Press *Apply*, then *OK*. You can now select the configuration and run it.

The coverage report can be found in build/reports/jacoco/test/html. Open index.html to browse.

# Notes to examiner

Documentation files, including RAD, SDD, meeting protocols and diagrams can be found in the [docs/](docs) directory.

The project includes nearly all dependencies through Gradle, but one dependency that is not available there is included in the repository tree at [src/main/java/net/javainthebox/caraibe/svg]. The group would like to point out that this code is not written by us, and should therefore also be excluded from gitinspector and other checks.
