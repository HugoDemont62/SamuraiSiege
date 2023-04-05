package fr.hugodemont.samuraisiege

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

@Suppress("DEPRECATION")
class CreditsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_credits)

        val btnBack: Button = findViewById(R.id.btnReturnMenu)
        btnBack.setOnClickListener {
            finish()
        }
    }
}