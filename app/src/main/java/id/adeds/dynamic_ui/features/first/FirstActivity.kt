package id.adeds.dynamic_ui.features.first

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import id.adeds.dynamic_ui.R
import id.adeds.dynamic_ui.util.MyResult
import id.adeds.dynamic_ui.util.print
import org.koin.androidx.viewmodel.ext.android.viewModel

class FirstActivity : AppCompatActivity(R.layout.activity_first) {
    private val viewModel: FirstViewModel by viewModel()

    override fun onStart() {
        super.onStart()
        viewModel.observeData().observe(this){
            when(it){
                is MyResult.Success -> {
                    "${it.data}".print()
                }
                is MyResult.Error.RecoverableError -> {
                    it.exception.message.print()
                }
                is MyResult.Error.NonRecoverableError -> {
                    it.exception.message.print()
                }
                is MyResult.Loading -> {
                    "loading".print()
                }
            }
        }
        viewModel.getReposFromGitHub("user:adeds")
    }

}