package fr.hugodemont.samuraisiege

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class CreditsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credits)

        val btnBack: Button = findViewById(R.id.btnReturnMenu)
        btnBack.setOnClickListener {
            finish()
        }
    }
}