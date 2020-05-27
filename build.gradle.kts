buildscript {
    val kotlin_version by extra("1.3.72")
    repositories { Repositories.with(this) }

    dependencies {
        classpath(Lib.androidx.build)
        classpath(Lib.kotlin.gradle)
        classpath(Lib.maven)
        "classpath"("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}

allprojects { repositories { Repositories.with(this) } }

apply(from = "helper.gradle")

tasks.register("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}