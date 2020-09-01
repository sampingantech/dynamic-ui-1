package id.adeds.dynamic_ui.data.repo


import androidx.lifecycle.MutableLiveData
import id.adeds.dynamic_ui.data.model.GitHubSearchResponse
import id.adeds.dynamic_ui.util.MyResult
import id.adeds.dynamic_ui.util.isValid
import id.adeds.dynamic_ui.util.print
import io.ktor.client.HttpClient
import io.ktor.client.request.*
import io.ktor.http.HttpMethod
import io.ktor.util.KtorExperimentalAPI

class GitHubRepo( private val client : HttpClient) {

    @KtorExperimentalAPI
    suspend fun getRepositoriesKtor(liveData : MutableLiveData<MyResult<GitHubSearchResponse>>, query : String){
        "Ktor_request entered 0".print()
        liveData.postValue(MyResult.Loading(true))
        "Ktor_request entered".print()

        try{
            val requestUrl = "https://api.github.com/search/repositories?q=$query"
            "Ktor_request URL: $requestUrl".print()

            val response =
                client.request<GitHubSearchResponse>(requestUrl) {
                    method = HttpMethod.Get
                    headers {
                        append("My-Custom-Header", "HeaderValue")
                    }
                }

            "Ktor_request Response: $response".print()
            liveData.postValue(MyResult.Success(response))
        }catch (e : java.lang.Exception){
            "Ktor_request Error: ${e.message?:"Un-traceable"}".print()
            "Ktor_request Error: $e".print()
            if (e.message.isValid()) {
                liveData.postValue(MyResult.Error.RecoverableError(Exception(e.message)))
            }else{
                liveData.postValue(MyResult.Error.NonRecoverableError(Exception("Un-traceable")))
            }
        }

    }



}