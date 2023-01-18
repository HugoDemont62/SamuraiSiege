package fr.iutlens.dubois.carte

import fr.iutlens.dubois.carte.sprite.TileMap

/**
 * Created by dubois on 27/12/2017.
 */
class Decor(dataSrc: Array<String> = map) : TileMap {

    private val digits = "123456789ABCDEFGHIJKL"

    private val data: List<List<Int>> =  dataSrc.map { line -> line.map { c -> digits.indexOf(c) } }

    override operator fun get(x: Int, y: Int): Int { return data[y][x] }

    override val sizeX = data[0].size
    override val sizeY = data.size

    companion object {
         val room = arrayOf(
            "1222232222225",
            "677778777777A",
            "BCCCCCCCCCCCG",
            "BCCCCCCCCCCCG",
            "BCCCCCCCCCCCG",
            "BCCCCCCCCCCCG",
            "BCCCCCCCCCCCG",
            "BCCCCCCCCCCCG",
            "BCCCCCCCCCCCG",
            "122DE222DE225",
            "677IJ777IJ77A",
            )

          val map = arrayOf(
            "22223222222322222322342242222222422",
            "77778777777877777877897797777777977",
            "CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC",
            "CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC",
            "1222325222232522232BCCG242222122422",
            "677787A777787A77787BCCG797777677977",
            "BCCCCCGCCCCCCGCCCCCBCCGCCCCCCBCCCCC",
            "BCCCCCGCCCCCCGCCCCCBCCGCCCCCCBCCCCC",
            "122DE2222DE2222DE22BCCG22DE2222DE22",
            "677IJ7777IJ7777IJ77BCCG77IJ7777IJ77",
            "HHHHHHHHHHHHHHHHHHH1345HHHHHHHHHHHH",
            "HHHHHHHHHHHHHHHHHHH689AHHHHHHHHHHHH"
        )

    }
}