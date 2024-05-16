pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()


    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
rootProject.name = "Hvordu"
include(":app")
include(":repo")
include(":api")
include(":core:datastore")
include(":core:datastore-proto")
include(":core:common")
include(":core:supabase")
