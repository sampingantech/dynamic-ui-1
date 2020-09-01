package id.adeds.dynamic_ui.di

import id.adeds.dynamic_ui.data.repo.GitHubRepo
import id.adeds.dynamic_ui.features.first.FirstViewModel
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object Injection {
    val baseModule = module {
        single { okHttpKtor }
        single { GitHubRepo(get()) }
        viewModel { FirstViewModel(get()) }
    }

    private val okHttpKtor = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

}