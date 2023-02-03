package fr.iutlens.dubois.carte.sprite
import fr.iutlens.dubois.carte.DistanceMap

class TowerSprite () : BasicSprite(){
    var speed = 0.1f // speed of bullet
    var alpha = 0f // alpha of bullet
    var nextCoordinate = DistanceMap.nextMove(coordinate) // next coordinate of bullet
    val paint = android.graphics.Paint().apply {
        color = android.graphics.Color.BLUE
        style = android.graphics.Paint.Style.FILL
    }
    // Infleger des degats à EnnemieSprite
    fun hitEnnemi(ennemi: EnnemiSprite) {
        ennemi.pv -= 10
    }
    // Tirer une balle
    fun shoot() {
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