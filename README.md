# Rx Extensions for Discord4J [![Download](https://jitpack.io/v/Discord4J-Addons/D4J-Rx.svg?style=flat-square)](https://jitpack.io/#Discord4J-Addons/D4J-Rx)

This module contains adapters for the EventDispatcher to utilize [Reactive Streams](http://www.reactive-streams.org/).

## Using this

This module includes the `IEventAdapter` interface which provides a common abstraction for adapting Discord4J events to 
reactive streams. This can either be implemented yourself or by using a default implementation in one of the modules.

The `IEventAdapter` has two methods, `IEventAdapter#steamAll()` and `IEventAdapter#stream(Class<? extends Event> event)`
which is used to transform events into a reactive stream.

## Modules

### RxJava module

This module supports RxJava 2.x. Instantiating a `RxJavaEventAdapter` class will provide streams in the form of `Flowable<? extends Event>`.

### Reactor modules

This module supports Reactor 3.x. Instantiating a `ReactorEventAdapter` class will provide streams in the form of `Flux<? extends Event>`.

## Adding this as a dependency

### Core module
#### Maven
```xml
  <dependency>
    <groupId>com.github.Discord4J-Addons.D4J-Rx</groupId>
    <artifactId>rx-extensions-core</artifactId>
    <version>VERSION_HERE</version>
  </dependency>
```
#### Gradle
```groovy
dependencies {
  compile 'com.github.Discord4J-Addons.D4J-Rx:rx-extensions-core:VERSION_HERE'
}
```

### Reactor module
#### Maven
```xml
  <dependency>
    <groupId>com.github.Discord4J-Addons.D4J-Rx</groupId>
    <artifactId>rx-extensions-reactor</artifactId>
    <version>VERSION_HERE</version>
  </dependency>
```
#### Gradle
```groovy
dependencies {
  compile 'com.github.Discord4J-Addons.D4J-Rx:rx-extensions-reactor:VERSION_HERE'
}
```

### RxJava module
#### Maven
```xml
  <dependency>
    <groupId>com.github.Discord4J-Addons.D4J-Rx</groupId>
    <artifactId>rx-extensions-rxjava</artifactId>
    <version>VERSION_HERE</version>
  </dependency>
```
#### Gradle
```groovy
dependencies {
  compile 'com.github.Discord4J-Addons.D4J-Rx:rx-extensions-rxjava:VERSION_HERE'
}
```
