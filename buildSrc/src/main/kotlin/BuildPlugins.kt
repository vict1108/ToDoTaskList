object BuildPlugins {
    const val android  =  "com.android.tools.build:gradle:${Versions.gradle_version}"
    const val kotlin  = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin_version}"
    const val firebaseCrashlytics = "com.google.firebase:firebase-crashlytics-gradle:${Versions.firebaseCrashlyticsVersion}"
    const val googleServices = "com.google.gms:google-services:${Versions.googleService}"
}

object Deps{

    const val coroutineCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutine}"
    const val coroutineAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutine}"

    const val retrofit = "com.squareup.retrofit2:retrofit:2.9.0"
    const val okhttp  = "com.squareup.okhttp3:okhttp:4.9.3"
    const val interceptor = "com.squareup.okhttp3:logging-interceptor:4.9.3"
    const val converter = "com.squareup.retrofit2:converter-gson:2.9.0"


    const val roomRuntime = "androidx.room:room-runtime:${Versions.room_version}"
    const val roomCompiler =  "androidx.room:room-compiler:${Versions.room_version}"
    const val roomLib = "androidx.room:room-ktx:${Versions.room_version}"
    val room_paging by lazy {
        "androidx.room:room-paging:${Versions.room_version}"
    }

    val koltlin_date_time by lazy {
        "org.jetbrains.kotlinx:kotlinx-datetime:0.4.0"
    }

    const val compose_ui = "androidx.compose.ui:ui:${Versions.compose_version}"
    const val compose_material = "androidx.compose.material:material:${Versions.compose_version}"
    const val compose_material_icon = "androidx.compose.material:material-icons-extended:${Versions.compose_material_icon}"
    const val compose_ui_tooling = "androidx.compose.ui:ui-tooling:${Versions.compose_version}"
    const val compose_ui_preview = "androidx.compose.ui:ui-tooling-preview:${Versions.compose_version}"
    const val compose_runtime = "androidx.compose.runtime:runtime:${Versions.compose_version}"
    const val compose_compiler = "androidx.compose.compiler:compiler:${Versions.compiler_version}"
    const val compose_foundation = "androidx.compose.foundation:foundation:${Versions.compose_version}"
    const val compose_lifecycle =  "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.lifecycle_version}"

    const val compose_constraintLayout  = "androidx.constraintlayout:constraintlayout-compose:1.0.1"
    const val compose_coil = "io.coil-kt:coil-compose:${Versions.coil_compose}"
    const val splashScreen =  "androidx.core:core-splashscreen:1.0.0"
    const val  kotlin_date_time  = "org.jetbrains.kotlinx:kotlinx-datetime:${Versions.kotlin_date_time}"
    val compose_hilt by lazy {
        "androidx.hilt:hilt-navigation-compose:${Versions.compose_hilt_version}"
    }
    const val nav_fragment  = ("androidx.navigation:navigation-fragment-ktx:${Versions.nav_version}")
    const val nav_ui  = "androidx.navigation:navigation-ui-ktx:${Versions.nav_version}"
    const val nav_compose =  "androidx.navigation:navigation-compose:${Versions.nav_version}"
    const val fragment  =  "androidx.fragment:fragment-ktx:${Versions.fragment_version}"

    const val lifecycle_viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle_version}"
    const val lifecycle_runtime =  "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle_version}"
    const val lifecycle_savedstate = "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.lifecycle_version}"

    val accompanist_pager by lazy {
        "com.google.accompanist:accompanist-pager:${Versions.accompanist}"
    }

    val accompanist_pager_indicator by lazy {
        "com.google.accompanist:accompanist-pager-indicators:${Versions.accompanist}"
    }
    val accompanist_flowLayout by lazy {
        "com.google.accompanist:accompanist-flowlayout:${Versions.accompanist}"
    }

     val  kotlinSerialization by lazy {
         "org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0"
     }

    val arrow_core by lazy {
        "io.arrow-kt:arrow-core:${Versions.arrow_version}"
    }

    val compose_paging by lazy {
        "androidx.paging:paging-compose:${Versions.paging_version}"
    }

    val dpAdjustment by lazy {
        "com.intuit.sdp:sdp-android:${Versions.dpAdjustment}"
    }

    val spAdjustment by lazy {
        "com.intuit.ssp:ssp-android:${Versions.dpAdjustment}"
    }

    val googleMap by lazy {
        "com.google.android.gms:play-services-maps:18.2.0"
    }
    val serviceLocation by lazy {
        "com.google.android.gms:play-services-location:21.1.0"
    }

    val servicePlaces by lazy {
        "com.google.android.libraries.places:places:3.3.0"
    }
    val places by lazy {
        "com.google.android.libraries.places:places:3.3.0"
    }

    val mapUtils by lazy {
        "com.google.maps.android:android-maps-utils:3.4.0"
    }

    val stripe by lazy {
        "com.stripe:stripe-android:20.36.0"
    }

    val dataStore by lazy {
        "androidx.datastore:datastore:1.0.0"

    }

    val kotlin_std_lib by lazy {
        "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin_version}"
    }

    val composeMaterial3 by lazy {
        "androidx.compose.material3:material3:${Versions.composeMaterial3}"
    }

    val composeMaterial3WindowSize by lazy {
        "androidx.compose.material3:material3-window-size-class:${Versions.composeMaterial3}"
    }

}