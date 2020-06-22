buildscript {
    repositories { Repositories.with(this) }

    dependencies {
        classpath(Lib.androidx.build)
        classpath(Lib.kotlin.gradle)
        classpath(Lib.maven)
        classpath(Lib.teanity)
    }
}

allprojects { repositories { Repositories.with(this) } }

apply(from = "helper.gradle")

tasks.register("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}