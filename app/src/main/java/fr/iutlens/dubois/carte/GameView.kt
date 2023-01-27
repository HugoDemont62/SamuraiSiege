package fr.iutlens.dubois.carte

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color.argb
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import fr.iutlens.dubois.carte.sprite.BasicSprite
import fr.iutlens.dubois.carte.sprite.Sprite
import fr.iutlens.dubois.carte.sprite.TiledArea
import fr.iutlens.dubois.carte.transform.CameraTransform
import fr.iutlens.dubois.carte.transform.FocusTransform
import fr.iutlens.dubois.carte.utils.RefreshHandler
import fr.iutlens.dubois.carte.utils.SpriteSheet

class GameView : View, View.OnTouchListener {

    //private var timer: RefreshHandler? = null

    // les 3 constructeurs standards, obligatoires pour une vue, appellent tous init() (plus bas)
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    var update: (() -> Unit)? = null
    private var timer: RefreshHandler? = null
    var background: Sprite? = null // La grille pour le fond
    var sprite: Sprite? = null // Le sprite à afficher sur la grille

    val boundingBox : RectF by lazy { RectF(0f,0f,width.toFloat(),height.toFloat())}

    var transform : CameraTransform? = null
    var onTouch : ((FloatArray,MotionEvent) -> Boolean)? = null


    init {
        if (isInEditMode) { // utile uniquement pour afficher la vue correctement dans l'éditeur
            // Normalement, c'est l'activité qui configure la vue
            // Chargement des feuilles de sprites
            SpriteSheet.load(R.drawable.decor, 5, 4, this.context)
            SpriteSheet.load(R.drawable.car, 3, 1, this.context)
            // Création des différents éléments à afficher dans la vue
            val tileView = TiledArea(R.drawable.decor, Decor())
            background = tileView
            val car = BasicSprite(R.drawable.car, 3F * tileView.w, 8F * tileView.h)
            sprite = car
            transform = FocusTransform(this, tileView, car, 12)
//            transform = FitTransform(this,tileView,Matrix.ScaleToFit.CENTER)
        }


        setOnTouchListener(this)


         //Gestion du rafraichissement de la vue. Le code (juste en dessous)
         //sera appelé toutes les 30 ms
        timer = RefreshHandler{
            if (this.isShown) { // Si la vue est visible
                timer?.scheduleRefresh(30) // programme le prochain rafraichissement
                update?.invoke()
                invalidate() // demande à rafraichir la vue
            }
        }

        timer?.scheduleRefresh(30)





    }

    override fun onTouch(view: View?, event: MotionEvent): Boolean {
        val point = transform?.getPoint(event.x, event.y) ?: return false
        return onTouch?.invoke(point, event) ?: false
    }


    private val backgroundColor = argb(255, 0, 0, 0)

    /**
     * Méthode appelée (automatiquement) pour afficher la vue
     * C'est là que l'on dessine le décor et les sprites
     * @param canvas
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // On met une couleur de fond
        canvas.drawColor(backgroundColor)
        canvas.save()
        transform?.getMatrix().let { canvas.concat(it) }
        background?.paint(canvas) // Dessin du fond
        sprite?.paint(canvas) // Dessin du ou des sprites, si il y en a
        canvas.restore()
    }
}