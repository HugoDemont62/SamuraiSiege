package fr.hugodemont.samuraisiege.sprite

import android.graphics.Canvas
import android.graphics.RectF
import fr.hugodemont.samuraisiege.utils.SpriteSheet

class TiledArea(private val sprite: SpriteSheet, val data: TileMap) : Sprite {

    constructor(id: Int, data: TileMap) : this(SpriteSheet[id]!!, data)

    val w  = sprite.spriteWidth
    val h  = sprite.spriteHeight
    val sizeX  = data.sizeX
    val sizeY  = data.sizeY

    override fun paint(canvas: Canvas) {
        for (y in 0 until sizeY) {
            for (x in 0 until sizeX) {
                sprite.paint(
                    canvas,
                    data.get(x, y),
                    (x * w).toFloat(),
                    (y * h).toFloat()
                )
            }
        }
    }

    override val boundingBox = RectF(0f,0f,w*sizeX.toFloat(),h*sizeY.toFloat())
    override fun update() {
    }
}