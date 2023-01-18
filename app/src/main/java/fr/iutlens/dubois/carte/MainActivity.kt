package fr.iutlens.dubois.carte

import android.graphics.Matrix
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import fr.iutlens.dubois.carte.sprite.BasicSprite
import fr.iutlens.dubois.carte.sprite.Sprite
import fr.iutlens.dubois.carte.sprite.SpriteList
import fr.iutlens.dubois.carte.sprite.TiledArea
import fr.iutlens.dubois.carte.transform.FitTransform
import fr.iutlens.dubois.carte.transform.FocusTransform
import fr.iutlens.dubois.carte.utils.SpriteSheet
import kotlin.math.abs



class MainActivity : AppCompatActivity() {


    private val gameView by lazy { findViewById<GameView>(R.id.gameView) }

    private var soundPool = SoundPool.Builder()
        .setMaxStreams(10)
        .setAudioAttributes(AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_GAME).build()
        ).build()

    private val bip by lazy { soundPool.load(this,R.raw.message,1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Chargement des feuilles de sprites
        SpriteSheet.load(R.drawable.decor, 5, 4, this)
        SpriteSheet.load(R.drawable.car, 3, 1, this)

        // Par défaut on démarre sur la configuration map
        configMap()

        // Chargement du son
        Log.d("MainActivity", "Chargement du son $bip")

        // On définit les actions des boutons
        findViewById<Button>(R.id.buttonMap).setOnClickListener { configMap() }
        findViewById<Button>(R.id.buttonDrag).setOnClickListener { configDrag() }
    }


    private fun configDrag() {
        var target: Sprite? = null;
        val room = TiledArea(R.drawable.decor, Decor(Decor.room))

        // Création des différents éléments à afficher dans la vue
        val list = SpriteList() // Notre liste de sprites
        repeat(7){ // On crée plusieurs sprites aléatoires
            list.add(BasicSprite(R.drawable.car,
                (room.data.sizeX*Math.random()*room.w).toFloat(),
                (room.data.sizeY*Math.random()*room.h).toFloat(),
                (0..2).random()))
        }

        // Configuration de gameView : tout ce qui est dans le apply concerne gameView
        gameView.apply {
            background = room
            sprite = list
            transform = FitTransform(this, room, Matrix.ScaleToFit.CENTER)
            onTouch = {
                point, event ->
                when(event.action) {
                    MotionEvent.ACTION_DOWN -> { // Sélection du sprite aux coordonnées cliquées
                        val (x,y) = point
                        target = list[x,y]
                        if (target != null) soundPool.play(bip,1f,1f, 1,0,1f)
                        target != null
                    }
                    MotionEvent.ACTION_MOVE -> { // Déplacement du sprite sélectionné
                        (target as? BasicSprite)?.let {
                            // On déplace le sprite sélectionné aux nouvelles coordonnées
                            it.x = point[0]
                            it.y = point[1]
                            gameView.invalidate() // On demande la mise à jour
                            true
                        } ?: false
                    }
                    MotionEvent.ACTION_UP -> {  // Déselection
                        target = null
                        true
                    }
                    else -> false
                }

            }
            invalidate() // On demande à rafraîchir la vue
        }
    }



    private fun configMap() {
         val map = TiledArea(R.drawable.decor, Decor(Decor.map))
         val hero = BasicSprite(R.drawable.car, 8.5F*map.w, 4.5F*map.h)

        // Configuration de gameView : tout ce qui est dans le apply concerne gameView
        gameView.apply {
            background = map // Equivalent à gameView.background = map
            sprite = hero
            transform = FocusTransform(this, map, hero,12)
            onTouch = { point, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        soundPool.play(bip,1f,1f, 1,0,1f)
                        // Calcul de la direction du héro d'après le clic :
                        var dx = point[0] - hero.x // calcule le vecteur entre le sprite et la zone touchée
                        var dy = point[1] - hero.y
                        //       Log.d("move", "$dx/$dy")
                        if (abs(dx) > abs(dy)) { // calcule la direction principale du déplacement
                            dx = if (dx > 0) map.w.toFloat() else -map.w.toFloat() // on se déplace de plus ou moins une case
                            dy = 0f
                        } else {
                            dx = 0f
                            dy = if (dy > 0) map.h.toFloat() else -map.h.toFloat()
                        }

                        // On applique le déplacement calculé
                        hero.x += dx
                        hero.y += dy
                        gameView.invalidate()
                        true
                    } else false
            }
            invalidate() // On demande à rafraîchir la vue
        }
    }

}