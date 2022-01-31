package fr.iutlens.dubois.carte.utils

import android.content.Context
import android.graphics.*
import android.os.Build
import java.util.HashMap

class SpriteSheet(val n: Int, val m: Int) {
    private var bitmap: Bitmap? = null
    private val sprite: Array<Bitmap?> = arrayOfNulls(n * m)
    var w = 0
    var h = 0
/*    private val dst: RectF = RectF()
    private val src: Rect = Rect()
*/
    companion object {
        private var map: MutableMap<Int, SpriteSheet> =  HashMap<Int, SpriteSheet>()
        private var paint = Paint().apply { isAntiAlias = true }

        fun register(id: Int, n: Int, m: Int, context: Context) {
            map[id] = SpriteSheet(n, m).apply { load(context,id) }
        }

        fun createCroppedBitmap(
            src: Bitmap,
            left: Int, top: Int,
            width: Int, height: Int
        ): Bitmap {
            return if (Build.VERSION.SDK_INT > 22) {
                Bitmap.createBitmap(src, left, top, width, height);
                //bug: returns incorrect region for some version,  so must do it manually
            } else {
                val offset = 0
                val pixels = IntArray(width * height)
                src.getPixels(pixels, offset, width, left, top, width, height)
                Bitmap.createBitmap(pixels, width, height, src.config)
            }
        }

        operator fun get(id: Int): SpriteSheet? {
            return map[id]
        }

    }

    constructor(context: Context, id: Int, n: Int, m: Int) : this(n, m) {
        load(context, id)
    }

    private fun load(context: Context, id: Int) {
        bitmap = Utils.loadImage(context, id)?.apply {
            w = width / n
            h = height / m
        }
    }

    fun paint(canvas: Canvas, ndx: Int, x: Float, y: Float) {
        /*	int i = ndx%n;
		int j = ndx/n;
		src.set(i*w, j*h, (i+1)*w-1, (j+1)*h-1);
		dst.set(x,y,x+w,y+h);
		canvas.drawBitmap(bitmap, src, dst, paint); */
        canvas.drawBitmap(getBitmap(ndx)!!, x, y, paint)
    }

    private fun getBitmap(ndx: Int): Bitmap? {
        if (sprite[ndx] == null) {
            val i = ndx % n
            val j = ndx / n
            sprite[ndx] = createCroppedBitmap(bitmap!!, i * w, j * h, w, h)
        }
        return sprite[ndx]
    }
}