# Trafic du CERN

This is the repository for the project "Trafic au CERN". Our professors are Prof. Bastien Chopard and Prof. Pierre Leone. Our contact at CERN is Frédéric Magnin.

## Environment

The simulation is running on C++. We are both running on Ubuntu and working with Geany.

## Model

We will use the model presented by Bastien Chopard and Alexandre Dupuis in "Networks and Spatial Economics, 3: (2003) 9–21". This is a cellular automata for simulation of traffic.

## Personnal reminders

### Updating local folder from online repository (repo -> local)

```sh
  git pull origin master
```

### Upload local changes into master branch (local -> repo)

```sh
  git add .
  git commit -m "Some message"
  git push origin master
```

### Update repo after changing .gitignore (local -> repo)

First `add` and `commit` your changes (at least the .gitignore file). Then

```sh
  rm -r --cached .
  git add .
  git commit -m "Fixed untracked files"
  git push origin master
```
