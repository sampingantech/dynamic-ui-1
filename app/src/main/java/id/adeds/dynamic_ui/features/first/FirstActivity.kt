package id.adeds.dynamic_ui.features.first

import androidx.appcompat.app.AppCompatActivity
import id.adeds.dynamic_ui.R
import id.adeds.dynamic_ui.util.MyResult
import kotlinx.android.synthetic.main.activity_first.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FirstActivity : AppCompatActivity(R.layout.activity_first) {
    private val viewModel: FirstViewModel by viewModel()

    override fun onStart() {
        super.onStart()
        viewModel.observeData().observe(this){
            when(it){
                is MyResult.Success -> {
                    resutlText.text = "${it.data}"
                }
                is MyResult.Error.RecoverableError -> {
                    resutlText.text = it.exception.message
                }
                is MyResult.Error.NonRecoverableError -> {
                    resutlText.text = it.exception.message
                }
                is MyResult.Loading -> {
                    resutlText.text = "TUNGGU ..."
                }
            }
        }
        viewModel.getReposFromGitHub()
    }

}