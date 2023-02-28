package fr.iutlens.dubois.carte


data class Vague(val nbEnnemi: Int, val speed: Float, val ennemiPv: Int)

val vagues = arrayOf(
   //vague sur 100 exponentielle
   Vague(100, 0.2f, 1),
   Vague(200, 0.02f, 1),
   Vague(300, 0.06f, 1),
   Vague(400, 0.1f, 1),
   Vague(500, 0.2f, 1),
   Vague(600, 0.2f, 1),
   Vague(700, 0.2f, 1),
   Vague(800, 0.2f, 1),
   Vague(900, 0.2f, 1),
)