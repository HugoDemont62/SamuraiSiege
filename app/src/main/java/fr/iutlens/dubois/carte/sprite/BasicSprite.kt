package fr.iutlens.dubois.carte.sprite


import android.graphics.Canvas
import android.graphics.RectF
import androidx.core.graphics.withTranslation
import fr.iutlens.dubois.carte.utils.SpriteSheet


/**
 *
 *
 * Created by dubois on 27/12/2017.
 */
class BasicSprite(private val spriteSheet: SpriteSheet,
                  var x: Float, var y: Float,
                  private var ndx : Int = 0) : Sprite {

    constructor(id: Int,  x: Float, y: Float, ndx : Int=0) : this(SpriteSheet[id]!!, x, y,ndx)

    // taille du sprite en pixels, divisée par deux (pour le centrage)
    private val w2 = spriteSheet.spriteWidth / 2f
    private val h2 = spriteSheet.spriteHeight / 2f

    override fun paint(canvas: Canvas) =
        canvas.withTranslation(x,y) { spriteSheet.paint(this, ndx, -w2, -h2) }

//rectangle occuppé par le sprite
    override val boundingBox get() = RectF(x - w2, y - h2, x + w2, y + h2)

}