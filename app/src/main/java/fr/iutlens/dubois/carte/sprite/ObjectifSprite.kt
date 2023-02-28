package fr.iutlens.dubois.carte.sprite

import android.graphics.Canvas
import androidx.core.graphics.withTranslation

class ObjectifSprite(
   sprite: Int,
   val list: SpriteList,
   coordinate: Pair<Int, Int>,
   tiledArea: TiledArea
) : BasicSprite(
   sprite,
   (coordinate.first + 0.5f) * tiledArea.w,
   (coordinate.second + 0.5f) * tiledArea.h
) {

   //variables
   var pv = 2000 //PV de l'objectif
   private val paint100 = android.graphics.Paint().apply {//Mettre une barre de vie verte
      color = android.graphics.Color.YELLOW
      style = android.graphics.Paint.Style.FILL
   }

   fun stopEnnemi(ennemi: EnnemiSprite?) {
      ennemi?.let { it.speedEnnemi = 0f } //Arreter l'ennemi
      //pv en moins sur l'objectif
      if (pv > 0) {
         pv -= 1
         println(pv)
      }
   }

   //Barre de vie de la tour
   override fun paint(canvas: Canvas) = canvas.withTranslation(x, y) {
      spriteSheet.paint(this, ndx, -w2, -h2)
      if (pv > pv / 2) { // si PV inf a 50
         canvas.drawRect(
            -w2,
            -h2,
            -w2 + 2 * w2,
            -h2 + 5f,
            paint100
         )
      }
   }

   override fun update() {
      list.list.filter {
         it.boundingBox.intersect(boundingBox)
      }.forEach {//First ?
         stopEnnemi(it as? EnnemiSprite)// Stop les ennemies a son contact
      }
   }
}

