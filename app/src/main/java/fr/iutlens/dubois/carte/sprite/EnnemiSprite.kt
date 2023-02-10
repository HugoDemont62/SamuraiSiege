package fr.iutlens.dubois.carte.sprite

import android.graphics.Canvas
import androidx.core.graphics.withTranslation
import fr.iutlens.dubois.carte.DistanceMap


private fun spawnPoint(tileMap: TileMap): Pair<Int, Int> {
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
   private var alpha = 0f
   private var nextCoordinate = distanceMap.nextMove(coordinate)
   private val paint100 = android.graphics.Paint().apply {
      color = android.graphics.Color.GREEN
      style = android.graphics.Paint.Style.FILL
   }
   private val paint50 = android.graphics.Paint().apply {
      color = android.graphics.Color.RED
      style = android.graphics.Paint.Style.FILL
   }

   //Barre de vie
   override fun paint(canvas: Canvas) =
      canvas.withTranslation(x, y) {
         spriteSheet.paint(this, ndx, -w2, -h2)
         canvas.drawRect(
            -w2,
            -h2,
            -w2 + 2 * w2,
            -h2 + 5f,
            if (pv > 50) paint100 else paint50
         )
      }

   //Update with placement
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