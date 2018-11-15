# Fall 2018 Team Project: Food Query and Meal Analyzer

See Canvas assignment pages for details of the assignment.

## Download and install JDK 8

https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html

## Download and install Eclipse Oxygen or later

https://www.eclipse.org/oxygen/

## Install JavaFX tools to Eclipse

1. Launch Eclipse (Oxygen or later)
2. Help -> Install New Software
3. Search for e(fx)clipse
4. Select All items
5. Click  Install
6. Accept certificate
7. Restart Eclipse

## Create a JavaFX project

1. Launch Eclipse
2. File -> New -> Other -> JavaFX -> JavaFX Project
3. Enter name
4. Leave source files in *package application*

## Add starter files to your JavaFX project

### clone from GitHub

Students may clone this repository and do the following from a local terminal window to get files in correct place for Eclipse JavaFX project.  Note: execute commands on the left and not the comments as shown on the right.

```
cd ~/cs400-workspace/p5                                  # cd to your local project directory
git clone https://github.com/cs400-deppeler/FoodQuery    # git clone to get files from this site
mv FoodQuery/.git .                                      # move .git file into current directory
mv FoodQuery/.gitignore .                                # moves .gitignore file into current directory
mv FoodQuery/* .                                         # moves all source and data files into current directory
rmdir FoodQuery                                          # remove the FoodQuery sub-directory
```

### Or, download files as in previous assignments from the assignment page

https://pages.cs.wisc.edu/~deppeler/cs400/assignments/p5/files/

Benjamin Nisler edited...
