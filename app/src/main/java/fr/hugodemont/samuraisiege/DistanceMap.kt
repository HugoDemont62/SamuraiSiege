package fr.hugodemont.samuraisiege


import fr.hugodemont.samuraisiege.sprite.TileMap

typealias Coordinate = Pair<Int, Int>

val direction = arrayOf(
    Coordinate(1, 0),
    Coordinate(0, 1),
    Coordinate(-1, 0),
    Coordinate(0, -1)
)

operator fun Coordinate.plus(other: Coordinate): Coordinate {
    return Coordinate(this.first + other.first, this.second + other.second)
}

operator fun TileMap.contains(coordinate: Coordinate): Boolean {
    return (coordinate.first in 0 until this.sizeX) &&
            (coordinate.second in 0 until this.sizeY)
}

class DistanceMap(var tileMap: TileMap, var target: Coordinate, var passable: (Int) -> Boolean) {
    val map = HashMap<Coordinate, Int>()

    fun update() {
        map.clear()
        val queue = ArrayDeque<Coordinate>(listOf(target))
        map[target] = 0
        while (!queue.isEmpty()) {
            val coordinate = queue.removeFirst()
            val dist = map[coordinate]!! + 1
            direction.map { coordinate + it }
                .filter {
                    (it in tileMap) && (it !in map) && passable(
                        tileMap.get(
                            it.first,
                            it.second
                        )
                    )
                }
                .forEach {
                    map[it] = dist
                    queue.addLast(it)
                }
        }
    }

    fun nextMove(coordinate: Coordinate): Coordinate {
        val distance = map[coordinate]?.minus(1) ?: -1
        //Log.d("nextMove","$coordinate $distance ")
        return direction.map { coordinate + it }.filter { map[it] == distance }.randomOrNull()
            ?: coordinate
    }

    init {
        update()
    }
}
