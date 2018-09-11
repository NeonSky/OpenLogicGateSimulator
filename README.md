Open Logic Gate Simulator
=========================

[![Build Status](https://travis-ci.com/NeonSky/OpenLogicGateSimulator.svg?token=XyHcbxehB8TtpGq4DuFW&branch=master)](https://travis-ci.com/NeonSky/OpenLogicGateSimulator)

### Setting up IntelliJ
#### Importing

1. Clone the repository.
2. Start IntelliJ. From the welcome screen, select *Import Project*. Locate and select the `build.gradle` file in the repository root, then press *OK*.
3. Click *OK* in the Import Project from Gradle window.
4. If asked about overwriting the `.idea` project file, select *Yes*.

The project is now imported into IntelliJ.

#### Running
To be able to run the project, we need to add a run configuration.

1. From the menu select *Run->Edit configurations*. Click the plus (+) button and select Gradle.
2. Set the following values:
   * *Name*: OpenLogicGateSimulator
   * *Gradle project*: Press the folder icon and select the OpenLogicGateSimulator project.
   * *Tasks:*: `run` (autocompletion should list this task).
3. Press *Apply*, then *OK*. You can now select the configuration and run it.

#### Unit tests
Instructions for setting up and running the unit test gradle task:  

1. From the menu select *Run->Edit configurations*. Click the plus (+) button and select Gradle.
2. Set the following values:
   * *Name*: Unit tests
   * *Gradle project*: Press the folder icon and select the OpenLogicGateSimulator project.
   * *Tasks:*: `test` (autocompletion should list this task).
3. Press *Apply*, then *OK*. You can now select the configuration and run it.
