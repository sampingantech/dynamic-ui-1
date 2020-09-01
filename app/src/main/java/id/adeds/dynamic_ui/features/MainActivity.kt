package id.adeds.dynamic_ui.features

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import id.adeds.dynamic_ui.R
import id.adeds.dynamic_ui.features.first.FirstActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onStart() {
        super.onStart()
        firstButton.setOnClickListener { startActivity(Intent(this, FirstActivity::class.java)) }
    }
}