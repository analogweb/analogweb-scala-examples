Analogweb Framework Scala Examples
===============================================

[![Heroku](https://heroku-badge.herokuapp.com/?app=analogweb-scala-examples&root=ping&style=flat)](https://analogweb-scala-examples.herokuapp.com/json)

# Quick Start

[Analogweb](https://analogweb.github.io) is a tiny HTTP oriented web framework.

Installing [Scala](http://www.scala-lang.org/) and [sbt](http://www.scala-sbt.org) then run.

```scala
$ sbt
...
sbt:analogweb-scala-example> reStart
```

And you will get

```bash
$ curl localhost:8000/json
$ {"message":"Hello, World!"}
```

Here is a third run of benchmark with wrk and [Analogweb](https://analogweb.github.io) + [Netty](http://netty.io/index.html) + [Circe](https://circe.github.io/circe/)
on [Mid-2014 MB Pro (2.9 GHz Intel Core i5 w/ 8G RAM)](https://support.apple.com/kb/SP703).

```bash
$ wrk -c160 -t40 http://localhost:8000/json -v --latency
wrk 4.0.0 [kqueue] Copyright (C) 2012 Will Glozer
Running 10s test @ http://localhost:8000/json
  40 threads and 160 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     9.35ms   17.35ms 131.86ms   90.53%
    Req/Sec     1.05k   405.65     3.08k    71.87%
  Latency Distribution
     50%    2.47ms
     75%    7.50ms
     90%   25.26ms
     99%   92.32ms
  419888 requests in 10.10s, 70.47MB read
Requests/sec:  41561.38
Transfer/sec:      6.98MB
```
