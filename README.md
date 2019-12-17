# Teanity #

## What does it do?

We present Teanity as our framework, but in reality it's a collection of utilities and base-classes
that help with having your new project set-up and ready for the next venture.

## Why?

It started as small internal project which we often copied over to the new project and as you
might've guessed it took a lot of time and space. By the time you finished, the project was no longer
empty and as a novice you didn't even know where to start.

We think that having set of (relatively) standardized extensions, libraries and features ready with just one dependency is really nice, and YOU should try it too :)

## Contributions? Nice!

We accept pull requests, but we will hand pick them to suit our needs first. This is an internal project after all. 😏
There's ongoing effort to unit test everything to provide some degree of stability; you should have your features / bugfixes tested as well.

Unfortunatelly we dropped support for internal "app" module to test stuff out. You should default to unit tests at all times.

Code Requirements? ...just don't use "obsolete" Kotlin code style.

---

## Usage ##

Use preferred method "[`teanity-plugin`](https://github.com/skoumalcz/teanity-plugin/blob/master/readme.md)" for even easier setup, update management and feature-rich plugin.

-- OR --

Project root build.gradle
```groovy
allprojects {
    repositories {
        //...
        maven { url 'https://jitpack.io' }
    }
}
```

`ext.teanity = ` [![](https://jitpack.io/v/com.skoumal/teanity.svg?style=flat-square)](https://jitpack.io/#com.skoumal/teanity)

dependency
```groovy
dependencies {
    implementation("com.skoumal.teanity:core:" + ext.teanity)
    implementation("com.skoumal.teanity:di:" + ext.teanity)
    implementation("com.skoumal.teanity:network:" + ext.teanity)
    implementation("com.skoumal.teanity:persistence:" + ext.teanity)
    implementation("com.skoumal.teanity:test-ui:" + ext.teanity)
    implementation("com.skoumal.teanity:test:" + ext.teanity)
    implementation("com.skoumal.teanity:ui:" + ext.teanity)

    // or get the whole collection

    implementation("com.skoumal:teanity:" + ext.teanity)
}
```

---

### Starting a new project? ###

* [Windows / MAC / Linux](https://github.com/skoumalcz/teanity-quickstart/releases)
    
#### Usage

```
# Just call the script like so in your target directory (ie. ~/projects) :
./quickstart
```
