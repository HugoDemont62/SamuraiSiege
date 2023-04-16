package fr.hugodemont.samuraisiege


data class Vague(val nbEnnemi: Int, val speed: Float, val ennemiPv: Int)

val vagues = arrayOf(
    //vague sur 100 exponentielle
    Vague(100, 0.02f,100),
    Vague(200, 0.02f,100),
    Vague(300, 0.06f,100),
    Vague(400, 0.1f, 100),
    Vague(500, 0.2f, 100),
    Vague(550, 0.25f, 100),
    Vague(600, 0.3f, 100),
    Vague(650, 0.3f, 100),
    Vague(400, 0.6f, 100),
    Vague(550, 0.6f, 100),
)