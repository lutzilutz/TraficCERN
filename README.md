# Traffic at CERN

This is the repository for the project "Traffic at CERN". Our referent professors are Prof. Bastien Chopard and Prof. Pierre Leone. Our contact at CERN is Frédéric Magnin.

## Contextual introduction

### Current situation

The road network around the CERN (Geneva CH and St-Genis FR) is especially overloaded. Several thousands of people are passing by the CERN and at the same time several thousands of employees of the CERN need to get to the CERN buildings. These two flows of vehicle are generating a huge load of vehicles on the road and thus a lot of traffic jam. As the number of vehicles increases every year, the situation needs to be adjusted as quick as possible.

### Goal of this simulator

The main goal of this project is to give useful traffic informations to the local authorities, so that they can choose the best to optimize the road network not only for the local inhabitants but for CERN employees as well. We will analyze multiple scenarios of possible optimization, in terms of traffic jam, time that vehicles spend on the road, and flow of vehicle at certain points of the network.

## The simulator

### Environment

The simulator is running on Java (version 1.8). We are both running on Ubuntu and working with Eclipse. We are using the native Java graphical librairies, and everything else has been hand written by ourselves.

### Model

We will use the model presented by Bastien Chopard and Alexandre Dupuis in "*Networks and Spatial Economics, 3: (2003) 9–21*". This is a **cellular automata for simulation of traffic**. This is a somewhat simple model, but that represents efficiently the expected traffic behaviors (such as the accordion effect in traffic jams).

### Documentation

We will provide a complete documentation of the simulator, so that this project can be as useful as possible in the future.

### Authors

- Pavlos Tserevelakis, Bachelor of Computational Sciences, University of Geneva
- Raphaël Lutz, Bachelor of Computational Sciences, University of Geneva

### Current progression

We are currently in the final phase of the project. All expected behaviors and scenarios have been implemented, and we are now analyzing the simulation data and writting the final report. The 1.0 version will be soon released on this repo.

-----

## Personnal reminders

#### Updating local folder (repo -> local)

```sh
  git pull origin master
```

#### Upload local changes (local -> repo)

```sh
  git add .
  git commit -m "Description of the commit"
  git pull origin master
  git push origin master
```

#### Update repo after changing .gitignore (local -> repo)

First `add` and `commit` your changes (at least the .gitignore file). Then

```sh
  git rm -r --cached .
  git add .
  git commit -m "Fixed untracked files"
  git push origin master
```

### Branches

Go into someBranch :

```sh
  git checkout someBranch
```

Create new branch :

```sh
  git branch someBranch
```

Show current branch :

```sh
  git branch
```

