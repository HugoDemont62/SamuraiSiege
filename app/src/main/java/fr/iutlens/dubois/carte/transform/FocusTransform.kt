package fr.iutlens.dubois.carte.transform

import android.graphics.Matrix
import fr.iutlens.dubois.carte.GameView
import fr.iutlens.dubois.carte.sprite.Sprite
import fr.iutlens.dubois.carte.sprite.TiledArea

class FocusTransform(val gameView: GameView, val tiledArea: TiledArea, var sprite: Sprite, var minTiles: Int) :
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
        val tilesX = 1.0f * gameView.boundingBox.width() / tiledArea.w
        val tilesY = 1.0f * gameView.boundingBox.height() / tiledArea.h
        val sizeTiles = tilesX.coerceAtMost(tilesY)

        val scale = sizeTiles / minTiles

        // La suite de transfomations est à interpréter "à l'envers"

        // On termine par un centrage de l'origine (la voiture donc) dans la fenêtre
        transform.setTranslate(gameView.boundingBox.centerX(), gameView.boundingBox.centerY())

        // On tourne le tout dans le sens inverse à l'angle de la voiture par rapport à la pise
        // Du coup, la voiture sera toujours orientée pareil à l'écran, c'est le décor qui bougera
        //        canvas.rotate(-car.direction);

        // On mets à l'échelle calculée au dessus
        transform.preScale(scale, scale)

        // On centre sur la position actuelle de la voiture (qui se retrouve en 0,0 )
        transform.preTranslate(-sprite.boundingBox.centerX(), -sprite.boundingBox.centerY())
        transform.invert(reverse)

        return  transform
    }

}
