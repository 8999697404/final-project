package com.luisenricke.helpyourself.service

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText
import com.luisenricke.helpyourself.R
import com.luisenricke.helpyourself.SingletonHolder
import timber.log.Timber

@Suppress("unused")
class Keyboard private constructor(private val context: Context) {

    companion object : SingletonHolder<Keyboard, Context>(::Keyboard)

    private val manager: InputMethodManager = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

    fun hide(view: View) {
        if (!view.requestFocus()) return

        manager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun show(view: View) {
        if (!view.requestFocus()) return

        manager.showSoftInput(view, 0)
    }

    fun touchEvent(event: MotionEvent?, currentFocus: View?) {
        if (currentFocus == null) return
        if (event?.action != MotionEvent.ACTION_DOWN) return

        when (currentFocus) {
            is EditText, is AutoCompleteTextView, is TextInputEditText -> Timber.d(context.getString(R.string.keyboard_service_hide))
            else                                                       -> return
        }

        val borderView = Rect()
        currentFocus.getGlobalVisibleRect(borderView)

        if (!borderView.contains(event.rawX.toInt(), event.rawY.toInt())) {
            hide(currentFocus)
        }
    }
}
