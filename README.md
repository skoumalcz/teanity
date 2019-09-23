# Teanity #

### What does it do?

We present Teanity as our framework, but in reality it's a collection of utilities and base-classes
that help with having your new project set-up and ready for the next venture.

### Why?

It started as small internal project which we often copied over to the new project and as you
might've guessed it took a lot of time and space. By the time you finished the project was no longer
empty and as a novice you didn't even know where to start.

#### Pre-github

Initially Teanity was completely written in Java and proposed MVP arch with tons and tons of
boilerplate. With Google presenting the "optimal way" - arch libraries - we decided that there's
little to no benefit of refactoring any further so it was torn down and rewritten from scratch as
a library.

#### 0.1 - 0.4

The all-new library had MVVM in mind alongside with some unspoken patterns - like repositories -,
but you could've used anything really, we were fine with that. Baby steps, right?

#### 1.0 - *

With the time passed and the client-side demands became increasingly tougher, the library must've
adapted as well. In the process it has been modularized - so you can split your `model`,
`persistence` (or `database` if you will) and `network` to separate, testable, reusable! modules.

On top of that we endorse using Use-Case pattern in order to create even smaller, disconnected,
pieces of code that can be reliably tested.

Since 1.0 we also endorse using LiveData instead of RxJava of any kind. In conjunction with
DataBinding it clears a lot of boilerplate otherwise needed to successfully map the correct types
to your views.

Furthermore we will focus on reducing boilerplate, so you can focus on the important things. We
welcome pull requests and praise feature requests and general feedback.

---

## Installation ##

Project root build.gradle
```groovy
allprojects {
    repositories {
        //...
        maven { url 'https://jitpack.io' }
    }
}
```

`ext.teanity = ` [![](https://jitpack.io/v/skoumalcz/teanity.svg)](https://jitpack.io/#skoumalcz/teanity)

dependency
```groovy
dependencies {
    implementation("com.github.skoumalcz.teanity:core:" + ext.teanity)
    implementation("com.github.skoumalcz.teanity:di:" + ext.teanity)
    implementation("com.github.skoumalcz.teanity:network:" + ext.teanity)
    implementation("com.github.skoumalcz.teanity:persistence:" + ext.teanity)
    implementation("com.github.skoumalcz.teanity:test-ui:" + ext.teanity)
    implementation("com.github.skoumalcz.teanity:test:" + ext.teanity)
    implementation("com.github.skoumalcz.teanity:ui:" + ext.teanity)

    // or get the whole collection

    implementation("com.github.skoumalcz:teanity:" + ext.teanity)
}
```

---

### Starting a new project? ###

#### _Disclaimer:_
_Might not be 100% up-to-date with the newest release of teanity and might require additional setup._

Try our cl tools:

* [MAC / Linux](https://github.com/diareuse/teanity-quickstart-linux)
    
* [Windows](https://gist.githubusercontent.com/diareuse/36b7aa4e544e1a47fdad999e493266dd/raw/5637e7cc2e7ad2041fd0d02301dbc395504fa1b5/quickstart.ps1)
