package fr.iutlens.dubois.carte.sprite

import android.graphics.Canvas
import androidx.core.graphics.withTranslation
import fr.iutlens.dubois.carte.DistanceMap

class CBallSprite(
    sprite: Int,
    val list: SpriteList,
    val tiledArea: TiledArea,
    var distanceMap: DistanceMap,
    var coordinate: Pair<Int, Int>
) : BasicSprite(
    sprite,
    (coordinate.first + 0.5f) * tiledArea.w,
    (coordinate.second + 0.5f) * tiledArea.h
) {

    var speed = 0.1f
    private var alpha = 0f
    private var nextCoordinate = distanceMap.nextMove(coordinate)
    override fun paint(canvas: Canvas) =
        canvas.withTranslation(x, y) {
            spriteSheet.paint(this, ndx, -w2, -h2)
        }

    fun hitEnnemi(ennemi: EnnemiSprite?) {
        ennemi?.let { it.ennemiPv -= 1 } //Faire des degats toutes les 10 frames
    }


    override fun update() {
        //On regarde si il y a un ennemi dans la zone de tir
        list.list.filter {
            it.boundingBox.intersect(boundingBox)
        }.forEach {//First ?
            hitEnnemi(it as? EnnemiSprite)
        }
    }
}