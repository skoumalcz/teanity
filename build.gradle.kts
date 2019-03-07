buildscript {
    extra.set(
        "versions", mapOf(
            /* === LIB === */
            "kotlin" to "1.3.21",
            "appcompat" to "1.1.0-alpha02",
            "ktx" to "1.0.1",
            "material" to "1.1.0-alpha04",
            "room" to "2.0.0",
            "lifecycle" to "2.0.0",
            "navigation" to "1.0.0-rc02",
            "bca" to "3.0.0-beta1",/*please do not update; nested binding fails to resolve on newer versions*/
            "glide" to "4.8.0",
            "state" to "1.4.1",
            "rxkotlin" to "2.3.0",
            "rxandroid" to "2.1.0",
            "dexter" to "5.0.0",
            "ktx" to "1.0.0",
            "work" to "1.0.0",

            /* === APP === */
            "constraintLayout" to "2.0.0-alpha2",
            "gradlePlugin" to "3.3.1",
            "koin" to "1.0.2",
            "retrofit" to "2.5.0",
            "okhttp" to "3.12.0",
            "moshi" to "1.8.0",
            "crashlytics" to "2.9.9",
            "fabric" to "1.+",
            "kotpref" to "2.6.0",
            "timber" to "4.7.1"
        )
    )

    repositories {
        google()
        jcenter()
        maven(url = "https://maven.fabric.io/public")
        maven(url = "https://jitpack.io")
        maven(url = "http://oss.sonatype.org/content/repositories/snapshots")
    }

    val versions: Map<String, String> by extra

    dependencies {
        classpath("com.android.tools.build:gradle:${versions["gradlePlugin"]}")
        classpath(kotlin("gradle-plugin", version = versions["kotlin"]))
        classpath("android.arch.navigation:navigation-safe-args-gradle-plugin:${versions["navigation"]}")
        classpath("io.fabric.tools:gradle:${versions["fabric"]}")
        classpath("com.github.dcendents:android-maven-gradle-plugin:2.1")
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