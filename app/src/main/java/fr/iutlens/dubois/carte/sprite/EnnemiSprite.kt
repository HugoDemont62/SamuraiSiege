package fr.iutlens.dubois.carte.sprite


class EnnemiSprite (id: Int, x: Float, y: Float, ndx: Int = 0) : BasicSprite(id, x, y, ndx){
    fun update() {
        x+=1
    }


}
