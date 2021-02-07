package id.adeds.dynamic_ui.features.first

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import id.adeds.dynamic_ui.base.BaseViewModel
import id.adeds.dynamic_ui.data.model.Form
import id.adeds.dynamic_ui.data.repo.MyRepo
import id.adeds.dynamic_ui.util.*
import io.ktor.util.*

class FirstViewModel(private val gitHubRepo: MyRepo) : BaseViewModel() {

    private val githubRepoLiveData = MutableLiveData<MyResult<Form>>()

    fun observeData(): MutableLiveData<MyResult<Form>> {
        return githubRepoLiveData
    }

    @KtorExperimentalAPI
    fun getReposFromGitHub() = launch {
        githubRepoLiveData.postValue(MyResult.Loading(true))
        githubRepoLiveData.postValue(gitHubRepo.getRepositoriesKtor())
    }

    fun convertUriToBase64(context: Context, uri: Uri): String? {
        val imageUtils = ImageUtils(context)
        var bitmap: Bitmap? = null
        val selectedPath = FileUtils.getRealPath(context, uri) ?: return null
        val fileType = context.contentResolver.getType(uri)
        if (!fileType.isNullOrBlank() && fileType.startsWith("image/")) {
            try {
                bitmap = imageUtils.compressImage(uri.toString(), Constant.IMAGE_HEIGHT, Constant.IMAGE_WIDTH)
            } catch (e: Exception) {
                Log.e("convertToBase64","${e.message}")
            }
        }
        return if (bitmap != null) {
            EncodeBased64Binary.bitmapToBase64(bitmap)
        } else EncodeBased64Binary.getBase64FromPath(selectedPath)
    }

    fun postSubmission(id: Int?, submissionId: Int?, submission: MutableMap<String, Any?>) {
        Log.e("post", """
            
            id           : $id,
            submissionId : $submissionId
            submission   : $submission  
        """.trimIndent())
    }
}