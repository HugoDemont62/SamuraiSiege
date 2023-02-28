package fr.hugodemont.samuraisiege.transform

import android.graphics.Matrix

interface CameraTransform {
    fun getPoint(x: Float, y: Float): FloatArray
    fun getMatrix(): Matrix
}