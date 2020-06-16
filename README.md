<p align="center">
  <img src="art/logo.png" width="128px" />
</p>
<p align="center">
    <a href="https://jitpack.io/#com.skoumal/teanity"><img src="https://jitpack.io/v/com.skoumal/teanity.svg?style=flat-square" width="128px" /></a>
</p>

Central backbone of your project. Why choose _teanity_?

- **Modular**: You're in power of what modules will be present in your next project. Why include the
entire library? Mix and match what **you** need.
- **Light**: Although it has big ideas, it only adds ~500 methods on top of included libraries.
- **Dependable**: Fortified for exceptions of all kinds.
- **Proven**: It's already used and deployed in real projects.

### Functionality

Teanity provides wide variety of helpers, tools and architectural proposition. It heavily relies on
[**Coroutines**](https://developer.android.com/kotlin/coroutines) and Android's
[**Lifecycle**](https://developer.android.com/jetpack/androidx/releases/lifecycle) libraries. All
tools are oriented that way, however it doesn't discriminate against one tool or another. Use what
feels best for you.

Read additional info for all modules:

- [Components module](component/readme.md)
- [Core module](core/readme.md)
- [DI module](di/readme.md)
- [Network module](network/readme.md)
- [Persistence module](persistence/readme.md)
- [Test module](test/readme.md)
- [UI Test module](test-ui/readme.md)
- [UI module](ui/readme.md)

### Where to start?

#### Plugin (recommended)

Include our plugin in your project: _`$projectRoot/build.gradle`_

```groovy
buildscript {
    repositories {
        //...
        maven { url 'https://jitpack.io' }
    }

    dependencies {
        classpath("com.skoumal:teanity-plugin:+")
    }
}

allprojects {
    repositories {
        //...
        maven { url 'https://jitpack.io' }
    }
}
```

Set up on per-module basis: _`$projectRoot/module/build.gradle`_

```groovy
plugins {
    id("com.android.application") // or com.android.library
    // kotlin (-android-extensions and -kapt) will be added for you
    id("teanity")
}

teanity {
    modules {
        useAll()
    }
}
```

#### Manual dep

Include dependencies manually: _`$projectRoot/module/build.gradle`_

```groovy
dependencies {
    def vTeanity = "1.+" // todo replace with specific version
    implementation("com.skoumal.teanity:core:$vTeanity")
    implementation("com.skoumal.teanity:di:$vTeanity")
    implementation("com.skoumal.teanity:network:$vTeanity")
    implementation("com.skoumal.teanity:persistence:$vTeanity")
    implementation("com.skoumal.teanity:ui:$vTeanity")

    androidTestImplementation("com.skoumal.teanity:test-ui:$vTeanity")
    testImplementation("com.skoumal.teanity:test:$vTeanity")
}
```
