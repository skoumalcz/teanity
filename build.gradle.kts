buildscript {
    repositories { Repositories.with(this) }

    dependencies {
        classpath(Lib.androidx.build)
        classpath(Lib.kotlin.gradle)
        classpath(Lib.navigation.build)
    }
}

allprojects { repositories { Repositories.with(this) } }

apply(from = "helper.gradle")

tasks.register("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}

/** @deprecated use terminal, this takes way too much time */
tasks.register("generateChangelog") {
    doLast {
        try {
            exec { commandLine("git-chglog", "-o", "CHANGELOG.md") }
        } catch (e: Exception) {
            println(">> You don't have git-chglog installed, please visit https://github.com/git-chglog/git-chglog for installation instructions.")
        }
    }
}