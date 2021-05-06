package com.kiwi.task.utils

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.TranslateAnimation
import android.widget.RelativeLayout
import android.widget.TextView
import com.kiwi.task.R

class MessageBox @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0) : RelativeLayout(context, attributeSet, defStyleAttr) {

    lateinit var titleLabel: TextView
    lateinit var textLabel: TextView



    companion object{


        fun showSucces(parent: ViewParent, title: String, text: String): MessageBox?{
            return showSucces(parent as ViewGroup, title, text)
        }

        fun showSuccess(parent: ViewGroup, title: String, text: String) : MessageBox? {
            return show(true, parent, title, text, 3000L)
        }

        fun showError(parent: ViewParent, title: String, text: String): MessageBox?{
            return showError(parent as ViewGroup, title, text)
        }

        fun showError(parent: ViewGroup, title: String, text: String) : MessageBox? {
            return show(false, parent, title, text, 3000L)
        }


        private fun show(
            success: Boolean,
            parent: ViewGroup,
            title: String?,
            text: String?,
            milliseconds: Long
        ): MessageBox? {
            val box = LayoutInflater.from(parent.context).inflate(R.layout.message_box, parent, false) as MessageBox
            box.titleLabel = box.findViewById(R.id.message_box_title)
            box.textLabel = box.findViewById(R.id.message_box_text)
            box.titleLabel.text = title
            box.textLabel.text = text
            box.setBackgroundResource(if (success) R.color.msgboxSuccessBg else R.color.msgboxErrorBg)
            parent.addView(box)
            val animation = TranslateAnimation(0F, 0F, (-300).toFloat(), 0F)
            animation.duration = 400
            animation.fillAfter = true
            box.startAnimation(animation)
            parent.postDelayed({
                val animation = TranslateAnimation(0F, 0F, 0F, (-300).toFloat())
                animation.duration = 500
                animation.fillAfter = true
                box.startAnimation(animation)
                animation.setAnimationListener(object : AnimationListener {
                    override fun onAnimationStart(animation: Animation) {}
                    override fun onAnimationEnd(animation: Animation) {
                        box.visibility = View.GONE
                        parent.removeView(box)
                    }

                    override fun onAnimationRepeat(animation: Animation) {}
                })
            }, milliseconds)
            return box
        }
    }


}