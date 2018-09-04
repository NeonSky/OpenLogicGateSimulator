Open Logic Gate Simulator
=========================

### Setting up IntelliJ

These instructions cover how to set up an IntelliJ project after cloning the repository:
1. Open the sources_root/build.gradle file
1. Press OK through the next dialog(s) and wait for `gradle sync` to finish running
1. Click Run->Edit configurations. Click the plus (+) button and select Application.
1. Set the following values:
   * *Name*: Desktop
   * *Use classpath of module*: `desktop_main`
   * *Main class*: select `DesktopLauncher`
   * *Working directory*: `your_project_path/core/assets/`
1. Click Apply, then OK. You can now select the configuration and run it.
