Embedded-Jetty + RESTEasy + Guice example
========================== 

Hello world example project that integrates:
- Jetty embedded 9.x
- RESTEasy 3.x (JAX-RS 2.0)
- Guice 3.0


It uses:
- modified GuiceResteasyBootstrapServletContextListener class (injects guice injector and use that one)
- different media types
- gson provider
- asynchronous requests
- user role authorization
- simple filter


## Usage

`mvn exec:java -Dexec.args="pl"`
<br /> (for polish hello world)

`mvn exec:java -Dexec.args="fi"`
<br /> (for finnish hello world)

### Web browser or REST client

[http://localhost:8080/hello.json] (http://localhost:8080/hello.json)

[http://localhost:8080/hello] (http://localhost:8080/hello)
<br /> (Responses with 403 Forbidden)

[http://localhost:8080/hello?user=bob] (http://localhost:8080/hello?user=bob)

[http://localhost:8080/async-hello?d=2] (http://localhost:8080/async-hello?d=2)
<br /> (Responses after 2 seconds)

