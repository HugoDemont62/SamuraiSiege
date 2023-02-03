package fr.iutlens.dubois.carte.sprite


import android.graphics.Canvas
import android.graphics.RectF
import androidx.core.graphics.withTranslation
import fr.iutlens.dubois.carte.utils.SpriteSheet

open class BasicSprite(
    val spriteSheet: SpriteSheet,
    var x: Float, var y: Float,
    var ndx : Int = 0) : Sprite {
    constructor(id: Int,  x: Float, y: Float, ndx : Int=0) : this(SpriteSheet[id]!!, x, y,ndx)

    // taille du sprite en pixels, divisée par deux (pour le centrage)
    val w2 = spriteSheet.spriteWidth / 2f
    val h2 = spriteSheet.spriteHeight / 2f

    override fun paint(canvas: Canvas) =
        canvas.withTranslation(x,y) { spriteSheet.paint(this, ndx, -w2, -h2) }

    //rectangle occuppé par le sprite
    override val boundingBox get() = RectF(x - w2, y - h2, x + w2, y + h2)
    override fun update() {}
}