package com.zoho.todo.di



import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.zoho.todo.appdatabase.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import javax.inject.Qualifier
import javax.inject.Singleton


private const val TIME_OUT = 60_000

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Singleton
    @Provides
    fun provideJson(): Json {
        return Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        }
    }

    @Singleton
    @Provides
    fun provideHttpClient(json: Json) = HttpClient(Android) {
        expectSuccess = true
        install(ContentNegotiation) {
            json(json)
        }
        engine {
            connectTimeout = TIME_OUT
            socketTimeout = TIME_OUT
        }


        install(ResponseObserver) {
            onResponse { response ->
                Log.d("HTTP status", "${response.status.value}")
                Log.d("HTTP status", "${response.request}")
                Log.d("HTTP status", "${response}")
                Log.d("HTTP response", response.bodyAsText())
                Log.d("Header", "${response.headers}")
            }
        }

        install(Logging) {
            level = LogLevel.ALL
            filter {
                it.url.host.contains("")
            }
        }

        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
    }


    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return AppDatabase.getInstance(appContext)
    }

}


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val niaDispatcher: ToDoTaskDispatchers)

enum class ToDoTaskDispatchers {
    IO
}