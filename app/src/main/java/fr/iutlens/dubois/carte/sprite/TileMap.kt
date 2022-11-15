package fr.iutlens.dubois.carte.sprite

interface TileMap {
    operator fun get(x : Int , y : Int) : Int
    val sizeX : Int
    val sizeY : Int
}