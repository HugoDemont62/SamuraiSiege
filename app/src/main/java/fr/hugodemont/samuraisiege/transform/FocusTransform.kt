package fr.hugodemont.samuraisiege.transform

import android.graphics.Matrix
import fr.hugodemont.samuraisiege.GameView
import fr.hugodemont.samuraisiege.sprite.Sprite
import fr.hugodemont.samuraisiege.sprite.TiledArea

class FocusTransform(
   val gameView: GameView,
   val tiledArea: TiledArea,
   var sprite: Sprite,
   var minTiles: Int
) :
   CameraTransform {

   private val transform = Matrix()
   private val reverse = Matrix()

   private var point = FloatArray(2)

   override fun getPoint(x: Float, y: Float): FloatArray {
      point[0] = x
      point[1] = y
      reverse.mapPoints(point)
      return point
   }

   override fun getMatrix(): Matrix {
      val tilesY = 1.0f * gameView.boundingBox.height() / tiledArea.h
      val scale = tilesY / minTiles
      val dxMax = (tiledArea.sizeX * tiledArea.w / 2 - gameView.boundingBox.centerX() / scale) * 3f
      transform.setTranslate(
         0f,
         gameView.boundingBox.centerY()
      )
      transform.preScale(scale, scale)

      val dx = (sprite.boundingBox.centerX()).coerceIn(-dxMax, dxMax)
      transform.preTranslate(-dx, -sprite.boundingBox.centerY())
      transform.invert(reverse)

      return transform
   }

}
