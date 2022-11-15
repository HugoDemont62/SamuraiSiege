package fr.iutlens.dubois.carte.sprite

import android.graphics.Canvas
import android.graphics.RectF

class SpriteList : Sprite {

    val list = ArrayList<Sprite>()

    fun add(sprite : Sprite){
        list.add(sprite)
    }

    var target : Sprite? = null

    fun setTarget(x : Float, y : Float): Sprite? {
        target = list.firstOrNull() { it.boundingBox.contains(x,y) }
        return target
    }

    override fun paint(canvas: Canvas) {
        list.forEach { it.paint(canvas) }
    }

    override val boundingBox: RectF
        get() = list.map{it.boundingBox}.reduce { result, box -> result.apply { union(box)} }
}