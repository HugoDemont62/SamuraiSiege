package fr.iutlens.dubois.carte

import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import fr.iutlens.dubois.carte.sprite.*
import fr.iutlens.dubois.carte.transform.FitTransform
import fr.iutlens.dubois.carte.transform.FocusTransform
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
      // Les lists de sprites
      val list = SpriteList() // Notre liste de sprites
      val listEnnemi = SpriteList() // Liste des ennemis
      val distanceMap = DistanceMap(room.data, room.sizeX / room.sizeX to room.sizeY / room.sizeY) {
         it == 0
      }
      list.add(listEnnemi)
      listEnnemi.add(EnnemiSprite(R.drawable.ennemi, room, distanceMap))
      // Création de la tour et des tirs
      list.add(TowerSprite(R.drawable.tower, listEnnemi, room.sizeX / 3 to room.sizeY / 2, room))

      // Création de l'objectif
      list.add(
         ObjectifSprite(
            R.drawable.objectif,
            listEnnemi,
            room.sizeX / room.sizeX to room.sizeY / room.sizeY,
            room
         )
      )
      val center = BasicSprite(R.drawable.tower, room.sizeX * room.w / 2f, room.sizeY * room.h / 2f)

      val scope = CoroutineScope(Job() + Dispatchers.Main)
      scope.launch {
         // New coroutine
         generate(listEnnemi, room, distanceMap)
      }
      // Configuration de gameView : tout ce qui est dans le apply concerne gameView
      gameView.apply {
         background = room
         sprite = list
         transform =
            FocusTransform(this, room, center, 30) // Modifier la taille de la map sur le visu
         update = {
            list.update()
            listEnnemi.list.removeAll { it is EnnemiSprite && it.pv == 0 } // Kills des ennemies
            list.list.removeAll { it is ObjectifSprite && it.pv == 0 } // Kills des tirs
         }
         var offset: Float? = null
         var pointer = -1
         onTouch = { point, event ->
            when (event.action) {
               MotionEvent.ACTION_DOWN -> { // Sélection du sprite aux coordonnées cliquéesoffset?.let {
                  if (offset == null) {
                     offset = point[0]
                     pointer = event.getPointerId(0)
                     true
                  } else false
               }
               MotionEvent.ACTION_MOVE -> { // Déplacement du sprite sélectionné
                  if (pointer == event.getPointerId(0)) {
                     offset?.let {
                        center.x -= -it + point[0]
                        offset = point[0]
                        gameView.invalidate() // On demande la mise à jour
                     }
                     offset != null
                  } else false
               }
               MotionEvent.ACTION_UP -> {  // Déselection
                  offset = null
                  pointer =-1
                  false
               }
               else -> false
            }

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