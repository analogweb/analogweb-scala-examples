Analogweb Framework Scala Examples
===============================================

[![Heroku](https://heroku-badge.herokuapp.com/?app=analogweb-scala&root=ping&style=flat)](https://analogweb-scala.herokuapp.com/helloworld)

# Quick Start

[Analogweb](https://analogweb.github.io) is a tiny HTTP oriented web framework.

Installing [Scala](http://www.scala-lang.org/) and [sbt](http://www.scala-sbt.org) then run.

```
$ sbt run 
```

And you will get

```
$ curl localhost:8000/ping
$ PONG!
```

# Build and run on Docker container

```
$ docker build . -t analogweb-scala-example
$ docker run -p 8000:8000 analogweb-scala-example
```
