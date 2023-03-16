package fr.hugodemont.samuraisiege.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.content.res.AppCompatResources

fun getStringResourceByName(context: Context, aString: String?): String {
    val packageName = context.packageName
    val resId = context.resources.getIdentifier(aString, "string", packageName)
    return context.getString(resId)
}

fun loadImage(context: Context, id: Int): Bitmap? {

//		Drawable blankDrawable = context.getResources().getDrawable(id);
//		Bitmap b =((BitmapDrawable)blankDrawable).getBitmap();
    return BitmapFactory.decodeResource(context.resources, id)
}

fun loadImages(context: Context, id1: Int, id2: Int): Bitmap? {
    val blankDrawable = AppCompatResources.getDrawable(context, id1)
    val b = (blankDrawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
    val c = Canvas(b)
    c.drawBitmap(loadImage(context, id2) ?: return null, 0f, 0f, null)
    return b
}

fun createCroppedBitmap(
    src: Bitmap,
    left: Int, top: Int,
    width: Int, height: Int
): Bitmap = if (Build.VERSION.SDK_INT > 22) {
    Bitmap.createBitmap(src, left, top, width, height);
    //bug: returns incorrect region for some version,  so must do it manually
} else {
    val offset = 0
    val pixels = IntArray(width * height)
    src.getPixels(pixels, offset, width, left, top, width, height)
    Bitmap.createBitmap(pixels, width, height, src.config)
}