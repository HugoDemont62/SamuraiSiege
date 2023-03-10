package fr.hugodemont.samuraisiege.transform

import android.graphics.Matrix
import fr.hugodemont.samuraisiege.GameView
import fr.hugodemont.samuraisiege.sprite.Sprite

class FitTransform(val gameView: GameView, val sprite: Sprite, val fitMode : Matrix.ScaleToFit) :
    CameraTransform {

    private val transform = Matrix()
    private val reverse = Matrix()

    private var point = FloatArray(2)

    override fun getMatrix(): Matrix {
        transform.setRectToRect(sprite.boundingBox,gameView.boundingBox,fitMode)
        transform.invert(reverse)
        return transform
    }

    override fun getPoint(x: Float, y: Float): FloatArray {
        point[0] = x
        point[1] = y
        reverse.mapPoints(point)
        return point
    }

}
