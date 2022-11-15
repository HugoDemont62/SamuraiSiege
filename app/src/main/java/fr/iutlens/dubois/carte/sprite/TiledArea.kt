package fr.iutlens.dubois.carte.sprite

import android.graphics.Canvas
import android.graphics.RectF
import fr.iutlens.dubois.carte.utils.SpriteSheet

class TiledArea(private val sprite: SpriteSheet,  val data: TileMap) : Sprite {

    constructor(id: Int, data: TileMap) : this(SpriteSheet[id]!!, data)

    val w  = sprite.w
    val h  = sprite.h
    private val sizeX : Int get() = data.sizeX
    private val sizeY : Int get() = data.sizeY

    override fun paint(canvas: Canvas) {
        for (x in 0 until sizeX) {
            for (y in 0 until sizeY) {
                sprite.paint(
                    canvas,
                    data[x, y],
                    (y * w).toFloat(),
                    (x * h).toFloat()
                )
            }
        }
    }

    override val boundingBox = RectF(0f,0f,w*sizeX.toFloat(),h*sizeY.toFloat())
}