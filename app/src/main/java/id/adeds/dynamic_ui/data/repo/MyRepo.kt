package id.adeds.dynamic_ui.data.repo

import id.adeds.dynamic_ui.data.model.Scheme
import id.adeds.dynamic_ui.util.MyResult
import id.adeds.dynamic_ui.util.isValid
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*

class MyRepo(private val client: HttpClient) {

    @KtorExperimentalAPI
    suspend fun getRepositoriesKtor(): MyResult<Scheme> {
        try {
            val requestUrl = "https://private-788f7-adedyas1.apiary-mock.com/json-scheme-2"

            val response =
                client.request<Scheme>(requestUrl) {
                    method = HttpMethod.Get
                    headers {
                        append("My-Custom-Header", "HeaderValue")
                    }
                }
            return MyResult.Success(response)
        } catch (e: java.lang.Exception) {
            return if (e.message.isValid()) {
                MyResult.Error.RecoverableError(Exception(e.message))
            } else {
                MyResult.Error.NonRecoverableError(Exception("Un-traceable"))
            }
        }

    }


}