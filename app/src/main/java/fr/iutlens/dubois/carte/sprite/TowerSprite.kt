package fr.iutlens.dubois.carte.sprite

import android.graphics.Canvas
import androidx.core.graphics.withTranslation


class TowerSprite(
    sprite: Int,
    val list: SpriteList,
    val coordinate: Pair<Int, Int>,
    val tiledArea: TiledArea
) : BasicSprite(
    sprite,
    (coordinate.first + 0.5f) * tiledArea.w,
    (coordinate.second + 0.5f) * tiledArea.h
) {
    override fun paint(canvas: Canvas) =
        canvas.withTranslation(x, y) {
            spriteSheet.paint(this, ndx, -w2, -h2)
        }

    //variables
    var pv = 100

    fun hitEnnemi(ennemi: EnnemiSprite?) {
        ennemi?.let { it.pv -= 10 }
    }
    fun hit(){
        pv-=1
    }


    override fun update() {
        list.list.filter { it.boundingBox.intersect(boundingBox) }.forEach { hitEnnemi(it as? EnnemiSprite)  }
        list.list.filter { it.boundingBox.intersect(boundingBox) }.forEach { _ -> hit()  }
    }
}

