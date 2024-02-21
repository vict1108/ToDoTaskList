import Versions.hilt_version
import Versions.hilt_work_version
import Versions.paging_version
import Versions.work_version
import org.gradle.configurationcache.extensions.capitalized
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlinx-serialization")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("com.google.protobuf") version "0.9.1"
}


protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.0"

        generateProtoTasks {
            all().forEach { task ->
                task.builtins {
                    create("java") {
                        option("lite")
                    }
                }
            }
        }
    }
}

android {
    namespace = "com.zoho.todo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.zoho.todo"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compiler_version
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    androidComponents {
        onVariants(selector().all()) { variant ->
            afterEvaluate {
                val capName = variant.name.capitalized()
                tasks.getByName<KotlinCompile>("ksp${capName}Kotlin") {
                    setSource(tasks.getByName("generate${capName}Proto").outputs)
                }
            }
        }
    }
}

dependencies {

    implementation(Deps.kotlin_std_lib)
    implementation("androidx.appcompat:appcompat:${Versions.appCompoatVersion}")
    implementation("androidx.constraintlayout:constraintlayout:${Versions.constraintLayoutVersion}")
    implementation("com.google.android.material:material:${Versions.materialVersion}")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2024.01.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.6.1")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation(Deps.composeMaterial3)
    implementation(Deps.composeMaterial3WindowSize)
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation(Deps.compose_lifecycle)
    implementation(Deps.compose_hilt)
    implementation("androidx.lifecycle:lifecycle-runtime-compose:${Versions.lifecycle_version}")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //room
    implementation(Deps.roomLib)
    implementation(Deps.roomRuntime)
    ksp(Deps.roomCompiler)
    implementation(Deps.room_paging)

    //compose paging
    implementation("androidx.paging:paging-runtime-ktx:$paging_version")
    implementation(Deps.compose_paging)

    // fragment
    implementation(Deps.fragment)
    // Navigation
    implementation(Deps.nav_fragment)
    implementation(Deps.nav_ui)
    implementation(Deps.nav_compose)
    // Coroutine
    implementation(Deps.coroutineCore)
    implementation(Deps.coroutineAndroid)
    // Ktor
    implementation("io.ktor:ktor-client-android:${Versions.ktor_version}")
    implementation("io.ktor:ktor-serialization-kotlinx-json:${Versions.ktor_version}")
    implementation("io.ktor:ktor-client-logging:${Versions.ktor_version}")
    implementation("io.ktor:ktor-client-okhttp:${Versions.ktor_version}")
    implementation("io.ktor:ktor-client-content-negotiation:${Versions.ktor_version}")
    implementation("io.ktor:ktor-client-auth:${Versions.ktor_version}")
    implementation("io.ktor:ktor-client-core:${Versions.ktor_version}")
    implementation("io.ktor:ktor-client-cio:${Versions.ktor_version}")
    // viewmodel
    implementation(Deps.lifecycle_viewmodel)
    implementation(Deps.lifecycle_savedstate)
    implementation(Deps.lifecycle_runtime)
    // Hilt
    implementation("com.google.dagger:hilt-android:${Versions.hilt_version}")
    ksp("com.google.dagger:hilt-compiler:${Versions.hilt_version}")

    // arrow
    implementation(Deps.arrow_core)
    implementation(Deps.compose_foundation)
    implementation(Deps.dataStore)
    implementation("androidx.datastore:datastore-core:1.0.0")
    implementation("com.google.protobuf:protobuf-javalite:3.21.5")
    implementation(Deps.compose_coil)
    //Workmanager
    implementation("androidx.work:work-runtime-ktx:$work_version")
    implementation ("androidx.work:work-multiprocess:$work_version")
    ksp("androidx.hilt:hilt-compiler:1.1.0")
    implementation ("androidx.hilt:hilt-work:$hilt_work_version")

}