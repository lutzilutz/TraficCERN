# Trafic du CERN

This is the repository for the project "Trafic au CERN". Our professors are Prof. Bastien Chopard and Prof. Pierre Leone. Our contact at CERN is Frédéric Magnin.

## Environment

The simulation is running on C++ (version c++11). We are both running on Ubuntu and working with Geany.

## Model

We will use the model presented by Bastien Chopard and Alexandre Dupuis in "*Networks and Spatial Economics, 3: (2003) 9–21*". This is a **cellular automata for simulation of traffic**.

## Authors

- Pavlos Tserevelakis, Bachelor of Computational Sciences, University of Geneva
- Raphaël Lutz, Bachelor of Computational Sciences, University of Geneva

-----

## Personnal reminders

#### Updating local folder (repo -> local)

From master branch :

```sh
  git pull origin master
```

From someBranch :

```bash
  git pull origin someBranch
```

#### Upload local changes (local -> repo)

From master branch :

```sh
  git add .
  git commit -m "Some message"
  git push origin master
```

From someBranch :

```sh
  git add .
  git commit -m "Some message"
  git push origin someBranch
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

#### Update repo after changing .gitignore (local -> repo)

First `add` and `commit` your changes (at least the .gitignore file). Then

```sh
  rm -r --cached .
  git add .
  git commit -m "Fixed untracked files"
  git push origin master
```
