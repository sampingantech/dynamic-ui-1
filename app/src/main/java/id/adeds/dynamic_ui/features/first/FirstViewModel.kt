package id.adeds.dynamic_ui.features.first

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.adeds.dynamic_ui.base.BaseViewModel
import id.adeds.dynamic_ui.data.model.GitHubSearchResponse
import id.adeds.dynamic_ui.data.model.Scheme
import id.adeds.dynamic_ui.data.repo.ApiCallback
import id.adeds.dynamic_ui.data.repo.GitHubRepo
import id.adeds.dynamic_ui.data.repo.MyRepo
import id.adeds.dynamic_ui.util.MyRequestState
import id.adeds.dynamic_ui.util.MyResult
import io.ktor.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

class FirstViewModel(private val gitHubRepo: GitHubRepo)  : ViewModel() {

    val scope : CoroutineScope = CoroutineScope(Dispatchers.IO )

    private val githubRepoLiveData = MutableLiveData<MyResult<GitHubSearchResponse>>()

    fun observeData() : MutableLiveData<MyResult<GitHubSearchResponse>>{
        return githubRepoLiveData
    }

//    fun getReposFromGitHub(data : String){
//        gitHubRepo.getRepositories(liveData = githubRepoLiveData, query = data)
//    }


    @KtorExperimentalAPI
    fun getReposFromGitHub(data : String){
        scope.launch {
            gitHubRepo.getRepositoriesKtor(liveData = githubRepoLiveData,
                query = data)
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.coroutineContext.cancelChildren()
    }
}