buildscript {
    repositories {
        google()
        jcenter()
        maven(url = "https://maven.fabric.io/public")
        maven(url = "https://jitpack.io")
        maven(url = "http://oss.sonatype.org/content/repositories/snapshots")
    }

    dependencies {
        classpath("com.android.tools.build", "gradle", Config.Dependency.gradlePlugin)
        classpath(kotlin("gradle-plugin", version = Config.Dependency.kotlin))
        classpath("androidx.navigation", "navigation-safe-args-gradle-plugin", Config.Dependency.navigation)
        classpath("io.fabric.tools", "gradle", Config.Dependency.fabric)
        classpath("com.github.dcendents", "android-maven-gradle-plugin", "2.1")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven(url = "https://maven.fabric.io/public")
        maven(url = "https://jitpack.io")
    }
}

tasks.register("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}

tasks.register("generateChangelog") {
    doLast {
        try {
            exec { commandLine("git-chglog", "-o", "CHANGELOG.md") }
        } catch (e: Exception) {
            println(">> You don't have git-chglog installed, please visit https://github.com/git-chglog/git-chglog for installation instructions.")
        }
    }
}