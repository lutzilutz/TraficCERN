# Src directory

Contains all the source files, e.g. the .java files and all the images.

## data/

Contains the java files managing the input data of the simulator. It computes the raw data and inserts it in roads and rides so that the simulator knows how much vehicles to generate at each point of entrance.

## elements/

Contains the java files representing the basic building blocks of the network : cells, roads, round-about, crossroad, ... You can find in the Docs folder an UML diagram presenting the global structure of these classes.

## graphics/

Contains the java files managing the graphical interface. It mainly contains all the needed assets, image loader and text renderer.

## input/

Contains the java files managing the user input : keyboard and mouse.

## main/

This is the main folder, containing the core of the simulator.

## network/

Contains the representation of the network. The NetworkComputing and NetworkRendering are the static classes allowing us to compute the evolution of the network, and the display of it.

## resources/

Contains the png files of the button (in the navigation bar). All resources that aren't java files should be in this directory.

## states/

Contains the different states of the simulator : menu, settings and simulation. The simulator can and must only be in one of these states at a given time.

## ui/

Contains all the UI java files : the multiple kinds of buttons and sliders.

## utils/

Contains the utilitaries (all the small functions that aren't directly linked to any of the other classes).
