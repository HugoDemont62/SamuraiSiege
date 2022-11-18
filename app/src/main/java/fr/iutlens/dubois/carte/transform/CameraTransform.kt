package fr.iutlens.dubois.carte.transform

import android.graphics.Matrix

interface CameraTransform {
    fun getPoint(x: Float, y: Float): FloatArray
    fun getMatrix(): Matrix
}