package fr.iutlens.dubois.carte

import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import fr.iutlens.dubois.carte.sprite.*
import fr.iutlens.dubois.carte.transform.FitTransform
import fr.iutlens.dubois.carte.utils.SpriteSheet
import kotlinx.coroutines.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private val gameView by lazy { findViewById<GameView>(R.id.gameView) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Setting du mode fullscreen
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_chargin)

        val handler = Handler(Looper.getMainLooper()) // Handler pour le chargement principal
        handler.postDelayed({
            setContentView(R.layout.activity_main)

            val btnPlay: Button = findViewById(R.id.playBtnId)//Selection du BTN pour play
            val btnCredits: Button = findViewById(R.id.creditsBtnId)//Selection du BTN pour credits
            val btnExit: Button = findViewById(R.id.exitBtnId)//Selection du BTN pour exit

            btnPlay.setOnClickListener {
                setContentView(R.layout.activity_game)
                // Chargement des feuilles de sprites - Peut etre voir un code moins lourd ?
                SpriteSheet.load(R.drawable.decor, 8, 6, this)
                SpriteSheet.load(R.drawable.ennemi, 1, 1, this)
                SpriteSheet.load(R.drawable.tower, 1, 1, this)
                SpriteSheet.load(R.drawable.cball, 1, 1, this)
                SpriteSheet.load(R.drawable.objectif, 1, 1, this)
                towerDefense() //set Game tower Defense
            }
            btnCredits.setOnClickListener {
                setContentView(R.layout.activity_credits)
            }
            btnExit.setOnClickListener {
                finish()
            }
        }, 1000)
    }

    private fun towerDefense() {
        val room = TiledArea(R.drawable.decor, Decor(Decor.laby))
        // Création des différents éléments à afficher dans la vue
        val list = SpriteList() // Notre liste de sprites
        val distanceMap = DistanceMap(room.data, room.sizeX / room.sizeX to room.sizeY / 2) {
            it == 0
        }

        // Création de la tour et des tirs
        list.add(TowerSprite(R.drawable.tower, list, room.sizeX / 2 to room.sizeY / 2, room))
        list.add(TowerSprite(R.drawable.objectif, list, room.sizeX / room.sizeX to room.sizeY / 2, room))

        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            // New coroutine
            generate(list, room, distanceMap)

        }
        // Configuration de gameView : tout ce qui est dans le apply concerne gameView
        gameView.apply {
            background = room
            sprite = list
            transform = FitTransform(this, room, Matrix.ScaleToFit.CENTER) // Modifier la taille de la map sur le visu
            update = {
                list.update()
                list.list.removeAll { it is EnnemiSprite && it.pv < 0 } // Kills des ennemies

            }
        }
    }

    //Generate with timer (Couroutine)
    private suspend fun generate(
        list: SpriteList,
        room: TiledArea,
        distanceMap: DistanceMap
    ) {
        withContext(Dispatchers.Main) {
            repeat(10) {
                delay(100)
                // On crée plusieurs sprites aléatoires
                list.add(EnnemiSprite(R.drawable.ennemi, room, distanceMap))
            }
        }

    }

}