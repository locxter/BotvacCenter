# BotvacCenter

## Overview

![Overview](overview.png)

This is a Kotlin Multiplatform app for controlling and experimenting with Neato Botvac D85 robot vacuums. It is loosely based on [btvccntrl-ng](https://github.com/locxter/btvccntrl-ng) and focuses on making everyday tasks easier (like setting the clock or starting the robot) as well as exploring SLAM (Simultaneous Localization And Mapping).

**Prebuilt packages for Linux (.deb) and Android (.apk) can be found under [Releases](https://github.com/locxter/BotvacCenter/releases).**

Also take a look at the project video on YouTube:

[![Making the WORLD'S SMARTEST ROBOT vacuum DUMBER by building my own crude SLAM stack](https://img.youtube.com/vi/RbZyXtjPLz4/maxresdefault.jpg)](https://www.youtube.com/watch?v=RbZyXtjPLz4)

## Dependencies

I generally try to minimize dependencies, but I'm a one man crew and can therefore only support Debian-based Linux distributions as I'm running one myself. Anyway, you need to have the following packages installed for everything to work properly:

- SDKMAN! for managing all the JVM dependencies. Install it via the [installation guide](https://sdkman.io/install).
- JDK for running the bytecode. Install it with `sdk install java`.
- Kotlin for developing the program. Install it with `sdk install kotlin`.
- Gradle for building the whole thing. Install it with `sdk install gradle`.
- Android Studio as the necessary IDE for Android development. Install it via the [installation guide](https://developer.android.com/studio/install).
- Kotlin Multiplatform plugin for Android Studio. Install it from [here](https://kotlinlang.org/docs/multiplatform-plugin-releases.html).

## Features

- [x] Start and stop house or spot cleanings remotely.
- [x] Access crucial statistics (battery charge and total runtime)  at a moments glance.
- [x] Manually drive the robot like a RC car from one location to another.
- [x] Check, modify and enable/disable the cleaning schedule.
- [x] Quickly set the current date and time with a single button press.
- [x] Get extensive diagnostics data for troubleshooting.
- [x] Easily experiment with SLAM via inbuilt sensors (LIDAR and wheel odometry). 

Possible future ones:

- [ ] Support for multiple robots (profiles)
- [ ] Persist UI state for smoother experience (might need a DI framework or other navigation library?)
- [ ] Path planning for autonomous room cleaning in straight lines (maybe fill area with spaced-apart points and determine route between them?)
- [ ] Always clean the parameter of the room first (needs some sort of wall detection)

## SLAM

### Proposed algorithm

1. Initial scan of the environment.
2. If the target position isn't equal to the current position:
   1. Calculate the path from current to target via a modified A* path finding algorithm including a cost map (prefer routes far away from obstacles).
   2. Follow the first 50 cm (or whatever distance between scans proves to be reliable) of the path.
   3. Perform another scan.
   4. If needed, perform some post-processing to clean the scan.
   5. Match the new scan to the last scan with the ICP (Iterative Closest Point) algorithm to get a LIDAR transformation estimate.
   6. Combine the LIDAR and wheel odometry transformation estimates to update the robot's absolute position.
   7. Add the current scan to the map based on the determined robot position.
   8. If needed, perform some post-processing to clean the map.
3. Wait for the user to either set a new target position and then go the step 2 or stop the mapping process.

### Example mapping runs

[Mapping early](mapping-early.mp4)

https://github.com/user-attachments/assets/45cc72df-398e-46a4-860a-3ac96b8b1be7

[Mapping later](mapping-later.mp4)

https://github.com/user-attachments/assets/cfbb268d-37dc-40b2-a895-026f322fc209

### Useful resources

- Really good introduction to SLAM: https://www.youtube.com/watch?v=xqjVTE7QvOg
- https://en.wikipedia.org/wiki/A*_search_algorithm
- https://en.wikipedia.org/wiki/Iterative_closest_point
- https://andrewjkramer.net/intro-to-lidar-slam/
- https://andrewjkramer.net/lidar-odometry-with-icp/
- https://github.com/1988kramer/intel_dataset
- https://github.com/tomhsu1990/Iterative-Closest-Point-ICP
- https://www.land-of-kain.de/docs/icp/
- https://www.cs.columbia.edu/~allen/F19/NOTES/slam_pka.pdf
- https://dspace.mit.edu/bitstream/handle/1721.1/119149/16-412j-spring-2005/contents/projects/1aslam_blas_repo.pdf
- https://www.sciencedirect.com/science/article/pii/S0924271621002586?fr=RR-2&rr=8abb9c92c8c82681

## How to use it

After modding your Neato Botvac D85 with [btvcbrdg](https://github.com/locxter/btvcbrdg) and writing down its IP address as well as login credentials (username and password), you can immediately run the desktop version via `gradle desktopRun -DmainClass=MainKt --quiet` and enter your specific information in the `Settings` to get started. Alternatively you can create a packaged version of the app via `gradle packageDistributionForCurrentOS` and install that - the final binary will be placed under `composeApp/build/compose/binaries/main/deb`. To run it as an Android app you can either just run a `debug` build in Android Studio's inbuilt emulator, create an Android package by selecting `debug` under `Build/Select Build Variant` and then running `Build App Bundle(s) / APK(s)/Build APK(s)` or build a fully-secure `release` build with your own signing keys via `Build/Generate Signed App Bundle / APK`.

## Helpful stuff

- Convert `.png` to `.ico` via `convert file.png -define icon:auto-resize=256,64,48,32,16 file.ico`.
- Convert `.png` to `.icns` via `convert file.png file.icns`.
- If the app throws a `ClassNotFoundException`, see this [section](https://github.com/JetBrains/compose-multiplatform/blob/master/tutorials/Native_distributions_and_local_execution/README.md#configuring-included-jdk-modules) in the native distributions tutorial.
