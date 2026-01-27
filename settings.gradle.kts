pluginManagement {
    repositories {
        maven {
            url = uri("https://nexus.dd.dhsn.de/repository/maven-public/")
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven {
            url = uri("https://nexus.dd.dhsn.de/repository/maven-public/")
        }
    }
}

rootProject.name = "Herdenmanager"
include(":app")
