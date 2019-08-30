import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.maven

object Repositories {

    fun with(handler: RepositoryHandler) = with(handler) {
        google()
        jcenter()
        maven(url = "https://maven.fabric.io/public")
        maven(url = "https://jitpack.io")
    }

}