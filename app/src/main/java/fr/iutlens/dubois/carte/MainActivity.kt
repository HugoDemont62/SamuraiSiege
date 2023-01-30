package fr.iutlens.dubois.carte

import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import fr.iutlens.dubois.carte.sprite.*
import fr.iutlens.dubois.carte.transform.FitTransform
import fr.iutlens.dubois.carte.transform.FocusTransform
import fr.iutlens.dubois.carte.utils.SpriteSheet
import kotlin.math.abs


class MainActivity : AppCompatActivity() {
    private val gameView by lazy { findViewById<GameView>(R.id.gameView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_chargin)

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            setContentView(R.layout.activity_main)

            val btnPlay: Button = findViewById(R.id.playBtnId)//Selection du BTN pour play
            val btnCredits: Button = findViewById(R.id.creditsBtnId)//Selection du BTN pour credits
            val btnExit: Button = findViewById(R.id.exitBtnId)//Selection du BTN pour exit

            btnPlay.setOnClickListener {

                setContentView(R.layout.activity_game)

                // Chargement des feuilles de sprites
                SpriteSheet.load(R.drawable.decor, 10, 8, this)
                SpriteSheet.load(R.drawable.car, 1, 1, this)
                configDrag()
                val btnBack: Button = findViewById(R.id.Back)
                btnBack.setOnClickListener {
                    setContentView(R.layout.activity_main)
                }
            }
            btnCredits.setOnClickListener {
                setContentView(R.layout.activity_credits)
            }
            btnExit.setOnClickListener {
                finish()
            }
        }, 1000)


    }

    private fun configDrag() {
        //var target: Sprite? = null
        val room = TiledArea(R.drawable.decor, Decor(Decor.room))

        val list = SpriteList()
        repeat(7) {
            list.add(
                BasicSprite(
                    R.drawable.car,
                    (room.data.sizeX * Math.random() * room.w).toFloat(),
                    (room.data.sizeY * Math.random() * room.h).toFloat()
                )
            )
        }
        //GameView
        gameView.apply {
            background = room
            sprite = list
            transform = FitTransform(this, room, Matrix.ScaleToFit.CENTER)
            update = {
                list.list.forEach { sprite ->
                    (sprite as? EnnemiSprite)?.update()
                }
            }
        }
    }


}