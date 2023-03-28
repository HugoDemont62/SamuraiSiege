package fr.hugodemont.samuraisiege.sprite

import android.graphics.Canvas
import androidx.core.graphics.withTranslation

class TowerSprite(
    sprite: Int,
    val list: SpriteList,
    coordinate: Pair<Int, Int>,
    val tiledArea: TiledArea,
    var move: Boolean
) : BasicSprite(
    sprite,
    (coordinate.first + 0.5f) * tiledArea.w,
    (coordinate.second + 0.5f) * tiledArea.h
) {

    //variables
    var down = 0 //CoolDown
    //val paint100 = android.graphics.Paint().apply {
    //    color = android.graphics.Color.GREEN
    //    style = android.graphics.Paint.Style.FILL
    //}
    //fun paint(canvas: Canvas) =
    //    canvas.withTranslation(x, y) {
    //        spriteSheet.paint(this, ndx, -w2, -h2)
    //        canvas.drawRect(
    //            -w2,
    //            -h2,
    //            -w2 + 2 * w2,
    //            -h2 + 5f,
    //            paint100
    //        )
    //    }
    fun hitEnnemi(ennemi: EnnemiSprite?) {
        ennemi?.let { it.ennemiPv -= 10 } //Faire des degats toutes les 10 frames

        //if ennemie was hit print scare

    }

    //fun stopEnnemi(ennemi: EnnemiSprite?) {
    //    ennemi?.let { it.speedEnnemi = 0f } //Arreter l'ennemi
    //}

    //Barre de vie de la tour
    override fun paint(canvas: Canvas) =
        canvas.withTranslation(x, y) {
            spriteSheet.paint(this, ndx, -w2, -h2)
        }

    override fun update() {
        if (down > 0) down--
        if (down == 0) {
            list.list.filter {
                it != this && it is BasicSprite && distance(it) < 15 * tiledArea.w
            }.minByOrNull { distance(it as BasicSprite) }?.let {
                if (!move) {
                    hitEnnemi(it as? EnnemiSprite)
                    down = 20
                }
            }
        }
    }

    fun square(x: Float) = x * x
    fun distance(target: BasicSprite): Float {
        return Math.sqrt(square(target.x - x).toDouble() + square(target.y - y)).toFloat()
    }
}

