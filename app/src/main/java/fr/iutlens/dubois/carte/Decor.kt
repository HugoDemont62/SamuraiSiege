package fr.iutlens.dubois.carte


import fr.iutlens.dubois.carte.sprite.TileMap

/**
 * Created by dubois on 27/12/2017.
 */
class Decor(dataSrc: Array<String>) : TileMap {

    private val digits = "0123456789ABCDEFGHIJKL"

    private val data: List<List<Int>> = dataSrc.map { line -> line.map { c -> digits.indexOf(c) } }

    override operator fun get(x: Int, y: Int): Int {
        return data[y][x]
    }

    override val sizeX = data[0].size
    override val sizeY = data.size


    companion object {
        val laby = arrayOf(
            "11111111111111111111111111111110",
            "10000000000000000000011111112210",
            "10000000000000000000000000012210",
            "10000000000000000000011111012210",
            "10000000000000000000000001012210",
            "10000000000000000000000001012210",
            "10000000000000000000000001012210",
            "10000000000000000000000001011110",
            "10000000000000000000000001000000",
            "10000000000000000000000001011110",
            "10000000000000000000000001012210",
            "10000000000000000000000001012210",
            "10000000000000000000000001012210",
            "10000000000000000000011111012210",
            "10000000000000000000000000012210",
            "10000000000000000000011111112210",
            "11111111111111111111111111111110",


            )
    }
}