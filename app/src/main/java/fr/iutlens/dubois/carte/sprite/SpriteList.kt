package fr.iutlens.dubois.carte.sprite

import android.graphics.Canvas

class SpriteList : Sprite {

    val list = ArrayList<Sprite>()

    fun add(sprite: Sprite) = list.add(sprite)

    operator fun get(x: Float, y: Float): Sprite? =
        list.firstOrNull() { it.boundingBox.contains(x, y) }

    override fun paint(canvas: Canvas) = list.forEach { it.paint(canvas) }

    override val boundingBox
        get() = list.map { it.boundingBox }.reduce { result, box -> result.apply { union(box) } }

    override fun update() {
        list.forEach { it.update() }
    }
}