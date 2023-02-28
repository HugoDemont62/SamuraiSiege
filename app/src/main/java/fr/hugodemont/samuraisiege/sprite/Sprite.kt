package fr.hugodemont.samuraisiege.sprite

import android.graphics.Canvas
import android.graphics.RectF

interface Sprite {
    fun paint(canvas: Canvas)
    val boundingBox: RectF
    fun update()

}