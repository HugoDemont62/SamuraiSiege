package fr.iutlens.dubois.carte

import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import fr.iutlens.dubois.carte.sprite.BasicSprite
import fr.iutlens.dubois.carte.sprite.SpriteList
import fr.iutlens.dubois.carte.sprite.TiledArea
import fr.iutlens.dubois.carte.transform.FitTransform
import fr.iutlens.dubois.carte.transform.FocusTransform
import fr.iutlens.dubois.carte.utils.SpriteSheet
import kotlin.math.abs



class MainActivity : AppCompatActivity() {

    private val map by lazy { TiledArea(R.drawable.decor, Decor(Decor.map)) }
    private val room by lazy { TiledArea(R.drawable.decor, Decor(Decor.room)) }
    private val hero by lazy { BasicSprite(R.drawable.car, 8.5F*map.w, 4.5F*map.h) }
    private val gameView by lazy { findViewById<GameView>(R.id.gameView) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Chargement des feuilles de sprites
        SpriteSheet.register(R.drawable.decor, 5, 4, this)
        SpriteSheet.register(R.drawable.car, 3, 1, this)

        // Par défaut on démarre sur la configuration map
        configMap()

        // On définit les actions des boutons
        findViewById<Button>(R.id.buttonMap).setOnClickListener { configMap() }
        findViewById<Button>(R.id.buttonDrag).setOnClickListener { configDrag() }
        
    }


    private fun configDrag() {
        // Création des différents éléments à afficher dans la vue
        val list = SpriteList() // Notre liste de sprites
        repeat(7){ // On crée plusieurs sprites aléatoires
            list.add(BasicSprite(R.drawable.car,
                (room.data.sizeX*Math.random()*map.w).toFloat(),
                (room.data.sizeY*Math.random()*map.h).toFloat(),
                (0..2).random()))
        }

        // Configuration de gameView
        gameView.apply {
            background = room
            sprite = list
            transform = FitTransform(this, room, Matrix.ScaleToFit.CENTER)
        }
        gameView.onTouch = this::onTouchDrag
        gameView.invalidate() // On demande à rafraîchir la vue

    }


    private fun onTouchDrag(
        point: FloatArray,
        event: MotionEvent,
    ) : Boolean {
        val list = gameView.sprite as? SpriteList ?: return false // On récupère la liste (quitte si erreur)
        return when(event.action) {
            MotionEvent.ACTION_DOWN -> list.setTarget(point[0], point[1]) != null // Sélection du sprite aux coordonnées cliquées
            MotionEvent.ACTION_MOVE -> { // Déplacement du sprite sélectionné
                (list.target as? BasicSprite)?.let {
                    // On déplace le sprite sélectionné aux nouvelles coordonnées
                    it.x = point[0]
                    it.y = point[1]
                    gameView.invalidate() // On demande la mise à jour
                    true
                } ?: false
            }
            MotionEvent.ACTION_UP -> {  // On déselectionne
                list.target = null
                true
            }
            else -> false
        }
    }

    private fun configMap() {
        // Configuration de gameView
        gameView.apply {
            background = map
            sprite = hero
            transform = FocusTransform(this, map, hero,12)
        }
        gameView.onTouch = this::onTouchMap
        gameView.invalidate()
    }

    private fun onTouchMap(
        point: FloatArray,
        event: MotionEvent
    ) = if (event.action == MotionEvent.ACTION_DOWN) {

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