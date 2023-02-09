package fr.iutlens.dubois.carte.sprite

import android.graphics.Canvas
import androidx.core.graphics.withTranslation

class TowerSprite(
    sprite: Int,
    val list: SpriteList,
    coordinate: Pair<Int, Int>,
    tiledArea: TiledArea
) : BasicSprite(
    sprite,
    (coordinate.first + 0.5f) * tiledArea.w,
    (coordinate.second + 0.5f) * tiledArea.h
) {

    //variables
    var pv = 100
    var down = 0 //CoolDown
    private val paint100 = android.graphics.Paint().apply {//Mettre une barre de vie verte
        color = android.graphics.Color.RED
        style = android.graphics.Paint.Style.FILL
    }

    fun hitEnnemi(ennemi: EnnemiSprite?) {
        ennemi?.let { it.pv -= 1 } //Faire des degats toutes les 10 frames
        }
    fun stopEnnemi(ennemi: EnnemiSprite?) {
        ennemi?.let { it.speed = 0f } //Arreter l'ennemi
    }
    //Barre de vie de la tour
    override fun paint(canvas: Canvas) =
        canvas.withTranslation(x, y) {
            spriteSheet.paint(this, ndx, -w2, -h2)
            if (pv < 50) { // si PV inf a 50
                canvas.drawRect(
                    -w2,
                    -h2,
                    -w2 + 2 * w2,
                    -h2 + 5f,
                    paint100
                )
            }
        }
    override fun update() {
        if (down > 0) down--
        if (down == 0){
            list.list.filter {
                it.boundingBox.intersect(boundingBox)
            }.forEach {//First ?
                hitEnnemi(it as? EnnemiSprite)
                down = 100
            }
        }
        list.list.filter {
            it.boundingBox.intersect(boundingBox)
        }.forEach {//First ?
            stopEnnemi(it as? EnnemiSprite)
        }
    }
}

