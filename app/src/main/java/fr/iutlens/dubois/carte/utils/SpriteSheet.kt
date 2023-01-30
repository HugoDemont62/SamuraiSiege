package fr.iutlens.dubois.carte.utils

import android.content.Context
import android.graphics.*
import java.util.HashMap

class SpriteSheet(val bitmap: Bitmap, val sizeX: Int, val sizeY: Int) {

    val spriteWidth = bitmap.width / sizeX
    val spriteHeight = bitmap.height / sizeY

    private val sprite: Array<Bitmap?> = Array(sizeX * sizeY) {
        val i = it % sizeX
        val j = it / sizeX
        createCroppedBitmap(bitmap, i * spriteWidth, j * spriteHeight, spriteWidth, spriteHeight)
    }

    operator fun get(ndx: Int) = sprite[ndx]!!

    fun paint(canvas: Canvas, ndx: Int, x: Float, y: Float) {
        canvas.drawBitmap(get(ndx), x, y, paint)
    }

    companion object {
        private val map: MutableMap<Int, SpriteSheet> = HashMap<Int, SpriteSheet>()
        private val paint = Paint().apply { isAntiAlias = true }

        fun load(id: Int, sizeX: Int, sizeY: Int, context: Context) {
            loadImage(context, id)?.let {
                map[id] = SpriteSheet(it, sizeX, sizeY)
            } ?: throw NoSuchElementException("Image resource not fount (id=$id)")
        }

        operator fun get(id: Int): SpriteSheet? {
            return map[id]
        }
    }

}