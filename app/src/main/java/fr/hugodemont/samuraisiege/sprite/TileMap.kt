package fr.hugodemont.samuraisiege.sprite

interface TileMap {
    operator fun get(x: Int, y: Int) : Int
    val sizeX : Int
    val sizeY : Int
}