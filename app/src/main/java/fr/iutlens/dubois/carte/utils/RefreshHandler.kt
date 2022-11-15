package fr.iutlens.dubois.carte.utils

import android.os.Handler
import android.os.Looper
import android.os.Message
import java.lang.ref.WeakReference

/**
 * Created by dubois on 27/12/2017.
 */
typealias TimerAction = ()-> Unit

class RefreshHandler(action: TimerAction) : Handler(Looper.getMainLooper()) {
    private val weak: WeakReference<TimerAction> = WeakReference<TimerAction>(action)
    override fun handleMessage(msg: Message) {
        weak.get()?.invoke()
    }

    fun scheduleRefresh(delayMillis: Long) {
        this.removeMessages(0)
        sendMessageDelayed(obtainMessage(0), delayMillis)
    }

    fun isRunning(): Boolean {
        return this.hasMessages(0)
    }

}