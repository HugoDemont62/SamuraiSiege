package fr.hugodemont.samuraisiege

import android.graphics.RectF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import fr.hugodemont.samuraisiege.sprite.*
import fr.hugodemont.samuraisiege.transform.FocusTransform
import fr.hugodemont.samuraisiege.utils.SpriteSheet
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

    // Game Tower Defense
    private fun towerDefense() {
        // tableau des Vagues d'ennemis
        var vagueNb = 0 // = vagues[0]
        val room = TiledArea(R.drawable.decor, Decor(Decor.laby))
        // Les lists de sprites
        val list = SpriteList() // Notre liste de sprites
        val listEnnemi = SpriteList() // Liste des ennemis
        val listObjectif = SpriteList() // Liste des objectifs
        val listTower = SpriteList() // Liste des tours
        val distanceMap =
            DistanceMap(room.data, room.sizeX / room.sizeX to room.sizeY / room.sizeY) {
                it == 0
            }
        list.add(listEnnemi)
        list.add(listObjectif)
        list.add(listTower)

        val btnShop: ImageButton = findViewById(R.id.shopId)//Selection du BTN pour shop
        btnShop.setOnClickListener {
            listTower.add(
                TowerSprite(
                    R.drawable.tower,
                    listEnnemi,
                    room.sizeX / 2 to room.sizeY / 2,
                    room
                )
            )
        }

        // Création de la tour et des tirs
        listTower.add(
            TowerSprite(
                R.drawable.tower,
                listEnnemi,
                room.sizeX / 3 to room.sizeY / 2,
                room
            )
        )
        // Création de l'objectif
        listObjectif.add(
            ObjectifSprite(
                R.drawable.objectif,
                listEnnemi,
                room.sizeX / room.sizeX to room.sizeY / room.sizeY,
                room
            )
        )

        //val cross = CrossSprite(R.drawable.cross)
        val center =
            BasicSprite(R.drawable.tower, room.sizeX * room.w / 2f, room.sizeY * room.h / 2f)
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        var job = scope.launch {
            // New coroutine
            generate(
                listEnnemi,
                vagues[vagueNb],
                room,
                distanceMap
            )
        }
        // Configuration de gameView : tout ce qui est dans le apply concerne gameView
        gameView.apply {
            background = room
            sprite = list
            transform =
                FocusTransform(this, room, center, 28) // Modifier la taille de la map sur le visu
            update = {
                list.update()
                listEnnemi.list.removeAll { it is EnnemiSprite && it.ennemiPv <= 0 } // Kills des ennemies
                listObjectif.list.removeAll { it is ObjectifSprite && it.pv <= 0 } // Kills de l'objectif
                if (listObjectif.list.size == 0) {
                    setContentView(R.layout.activity_lose)
                }
                if (job.isCompleted && listEnnemi.list.size == 0 && listObjectif.list.size == 1) {
                    if (vagueNb == vagues.size) {
                        setContentView(R.layout.activity_win) // Rajouter +1 à la boucle de vague
                    } else if (vagueNb >= 0) {
                        println(vagueNb)
                        job = scope.launch {
                            // New coroutine
                            generate(
                                listEnnemi,
                                vagues[vagueNb],
                                room,
                                distanceMap
                            )
                        }
                        vagueNb += 1
                    }
                }
            }
            var offset: Float? = null
            var pointer = -1
            var target: Sprite? = null
            onTouch = { point, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // On vérifie si on a cliqué sur une tour
                        val (x, y) = point
                        target = listTower[x, y]
                        target != null

                        //On selectionne l'ennemi le plus proche
                        val rect = RectF(
                            point[0] - 30,
                            point[1] - 30,
                            point[0] + 30,
                            point[1] + 30
                        )
                        // On met des degats sur l'ennemi
                        listEnnemi.list.filter {
                            it is EnnemiSprite && it.boundingBox.intersect(rect) // On filtre les ennemis
                        }.forEach { // On parcours les ennemis

                            val ennemiSprite = it as? EnnemiSprite // On caste l'ennemi
                            ennemiSprite?.ennemiPv =
                                ennemiSprite?.ennemiPv?.minus(50) ?: 0 //50 pv de degats
                            println(ennemiSprite?.ennemiPv)
                        }
                        if (offset == null) {
                            offset = point[0]
                            pointer = event.getPointerId(0)
                            true
                        } else false
                    }
                    MotionEvent.ACTION_MOVE -> {
                        if (pointer == event.getPointerId(0)) {
                            if (target == null) {
                                offset?.let {
                                    center.x -= -it + point[0]
                                    offset = point[0]
                                    gameView.invalidate() // On demande la mise à jour
                                }
                                offset != null
                            }else {
                                (target as? TowerSprite)?.let {
                                    // On déplace le sprite sélectionné aux nouvelles coordonnées
                                    it.x = point[0]
                                    it.y = point[1]
                                    gameView.invalidate() // On demande la mise à jour
                                    true
                                } ?: false
                            }
                        } else false
                    }
                    MotionEvent.ACTION_UP -> {  // Déselection
                        offset = null
                        pointer = -1
                        target = null
                        true
                    }
                    else -> false
                }
            }
        }
    }

    //Generate with timer (Couroutine)
    private suspend fun generate(
        list: SpriteList,
        vague: Vague,
        room: TiledArea,
        distanceMap: DistanceMap
    ) {
        withContext(Dispatchers.Main) {
            repeat(vague.nbEnnemi) {
                delay(100)
                // On crée plusieurs sprites d'ennemi
                list.add(
                    EnnemiSprite(
                        R.drawable.ennemi,
                        room,
                        distanceMap,
                        vague.speed,
                        vague.ennemiPv
                    )
                )
            }
        }
    }
}