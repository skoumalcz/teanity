buildscript {
    repositories { Repositories.with(this) }

    dependencies {
        classpath(Lib.androidx.build)
        classpath(Lib.kotlin.gradle)
    }
}

allprojects { repositories { Repositories.with(this) } }

apply(from = "helper.gradle")

tasks.register("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}