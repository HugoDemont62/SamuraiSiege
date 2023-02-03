package fr.iutlens.dubois.carte.sprite

import android.graphics.Canvas
import androidx.core.graphics.withTranslation
import fr.iutlens.dubois.carte.DistanceMap



fun spawnPoint(tileMap: TileMap): Pair<Int, Int> {
    val x = tileMap.sizeX - 1
    val y = (0 until tileMap.sizeY).random()
    return x to y

}


class EnnemiSprite(
    sprite: Int,
    val tiledArea: TiledArea,
    var distanceMap: DistanceMap,
    var coordinate: Pair<Int, Int>
) : BasicSprite(
    sprite,
    coordinate.first * tiledArea.w.toFloat(),
    coordinate.second * tiledArea.h.toFloat()
) {
    constructor(sprite: Int, tiledArea: TiledArea, distanceMap: DistanceMap) : this(
        sprite,
        tiledArea,
        distanceMap,
        spawnPoint(tiledArea.data)
    )

    var pv = 100
    var speed = 0.1f
    var alpha = 0f
    var nextCoordinate = distanceMap.nextMove(coordinate)
    val paint = android.graphics.Paint().apply {
        color = android.graphics.Color.GREEN
        style = android.graphics.Paint.Style.FILL
    }
    //Barre de vie
    override fun paint(canvas: Canvas) =
        canvas.withTranslation(x, y) {
            spriteSheet.paint(this, ndx, -w2, -h2)
            canvas.drawRect(-w2, -h2, -w2 + 2 * w2 * pv / 100, -h2 + 5f, paint)
        }

    override fun update() {
        alpha = (alpha + speed).coerceAtMost(1f)
        x = (coordinate.first * (1f - alpha) + alpha * nextCoordinate.first + 0.5f) * tiledArea.w
        y = (coordinate.second * (1f - alpha) + alpha * nextCoordinate.second + 0.5f) * tiledArea.h
        if (alpha == 1f) {
            coordinate = nextCoordinate
            alpha = 0f
            nextCoordinate = distanceMap.nextMove(coordinate)
            if (nextCoordinate == coordinate) {
                coordinate = spawnPoint(tiledArea.data)
                nextCoordinate = distanceMap.nextMove(coordinate)
            }
        }
    }
}