package fr.iutlens.dubois.carte

import android.graphics.Matrix
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
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
                SpriteSheet.load(R.drawable.decor, 5, 4, this)
                SpriteSheet.load(R.drawable.car, 3, 1, this)
                configDrag()
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
        var target: Sprite? = null
        val room = TiledArea(R.drawable.decor, Decor(Decor.room))

        val list = SpriteList()
        repeat(7) {
            list.add(
                BasicSprite(
                    R.drawable.car,
                    (room.data.sizeX * Math.random() * room.w).toFloat(),
                    (room.data.sizeY * Math.random() * room.h).toFloat(),
                    (0..2).random()
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


            //onTouch = { point, event ->
            //    when (event.action) {
            //        MotionEvent.ACTION_DOWN -> { // Sélection du sprite aux coordonnées cliquées
            //            val (x, y) = point
            //            target = list[x, y]
            //            if (target != null) soundPool.play(bip, 1f, 1f, 1, 0, 1f)
            //            target != null
            //        }
            //        MotionEvent.ACTION_MOVE -> { // Déplacement du sprite sélectionné
            //            (target as? BasicSprite)?.let {
            //                // On déplace le sprite sélectionné aux nouvelles coordonnées
            //                it.x = point[0]
            //                it.y = point[1]
            //                true
            //            } ?: false
            //        }
            //        MotionEvent.ACTION_UP -> {  // Déselection
            //            target = null
            //            true
            //        }
            //        else -> false
            //    }
            //
            //}
        }
    }



}