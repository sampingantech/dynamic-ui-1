package id.adeds.dynamic_ui.features.first

import androidx.lifecycle.MutableLiveData
import id.adeds.dynamic_ui.base.BaseViewModel
import id.adeds.dynamic_ui.data.model.Scheme
import id.adeds.dynamic_ui.data.repo.MyRepo
import id.adeds.dynamic_ui.util.MyResult
import io.ktor.util.*

class FirstViewModel(private val gitHubRepo: MyRepo) : BaseViewModel() {

    private val githubRepoLiveData = MutableLiveData<MyResult<Scheme>>()

    fun observeData(): MutableLiveData<MyResult<Scheme>> {
        return githubRepoLiveData
    }

    @KtorExperimentalAPI
    fun getReposFromGitHub() = launch {
        githubRepoLiveData.postValue(MyResult.Loading(true))
        githubRepoLiveData.postValue(gitHubRepo.getRepositoriesKtor())
    }
}