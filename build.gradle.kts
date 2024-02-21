// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    id("com.google.devtools.ksp") version "${Versions.kotlin_version}-1.0.16" apply false
    id("org.jetbrains.kotlin.android") version Versions.kotlin_version apply false
}

buildscript{
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath ("com.android.tools.build:gradle:8.2.2")
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.nav_version}")
        classpath ("org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin_version}")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt_version}")
    }

}

allprojects {
    repositories {
        //mavenCentral()
    }
}

tasks {
    register("clean", Delete::class) {
        delete(rootProject.buildDir)
    }
}