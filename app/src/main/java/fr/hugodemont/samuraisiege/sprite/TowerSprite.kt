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
    var down = 10 //CoolDown
    fun hitEnnemi(ennemi: EnnemiSprite?) {
        ennemi?.let { it.ennemiPv -= 10 } //Faire des degats toutes les 10 frames

    }

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
    //fun shoot() {
    //    val ballSprite = CBallSprite(
    //        sprite = R.drawable.cball,
    //        list = list,
    //        tiledArea = tiledArea,
    //        distanceMap = distanceMap,
    //        coordinate = coordinate
    //    )
    //    ballSprite.speed = 0.1f
    //    list.add(ballSprite)
    //}

    fun square(x: Float) = x * x
    fun distance(target: BasicSprite): Float {
        return Math.sqrt(square(target.x - x).toDouble() + square(target.y - y)).toFloat()
    }
}

