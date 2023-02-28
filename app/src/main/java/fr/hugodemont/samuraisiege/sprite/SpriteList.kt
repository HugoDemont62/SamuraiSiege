package fr.hugodemont.samuraisiege.sprite

import android.graphics.Canvas

class SpriteList : Sprite {

    val list = ArrayList<Sprite>()
//Faut creer des lists différentes pour chaque NameSprite
    fun add(sprite: Sprite) = list.add(sprite)

    fun hitTouch(ennemiSprite: EnnemiSprite) = ennemiSprite.ennemiPv
    operator fun get(x: Float, y: Float): Sprite? =
        list.firstOrNull() { it.boundingBox.contains(x, y) }

    override fun paint(canvas: Canvas) = list.forEach { it.paint(canvas) }

    override val boundingBox
        get() = list.map { it.boundingBox }.reduce { result, box -> result.apply { union(box) } }

    override fun update() {
        list.forEach { it.update() }
    }
}