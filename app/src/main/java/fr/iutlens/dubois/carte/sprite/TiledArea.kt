package fr.iutlens.dubois.carte.sprite

import android.graphics.Canvas
import android.graphics.RectF
import fr.iutlens.dubois.carte.utils.SpriteSheet

class TiledArea(private val sprite: SpriteSheet,  val data: TileMap) : Sprite {

    constructor(id: Int, data: TileMap) : this(SpriteSheet[id]!!, data)

    val w  = sprite.spriteWidth
    val h  = sprite.spriteHeight
    private val sizeX  = data.sizeX
    private val sizeY  = data.sizeY

    override fun paint(canvas: Canvas) {
        for (y in 0 until sizeY) {
            for (x in 0 until sizeX) {
                sprite.paint(
                    canvas,
                    data[x, y],
                    (x * w).toFloat(),
                    (y * h).toFloat()
                )
            }
        }
    }

    override val boundingBox = RectF(0f,0f,w*sizeX.toFloat(),h*sizeY.toFloat())
}