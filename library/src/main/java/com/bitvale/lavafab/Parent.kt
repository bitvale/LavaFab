package com.bitvale.lavafab

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.FloatEvaluator
import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.RectF
import android.util.SparseArray
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator

/**
 * Created by Alexander Kolpakov on 27.08.2018
 */
class Parent(override val center: PointF, override val radius: Float) : LavaView {

    override var currentRadius: Float = radius
    override val evaluator = FloatEvaluator()
    override var icon: Bitmap? = null
    override var listener: LavaView.LavaOnClickListener? = null

    val drawer = Drawer()
    private val childSet = ArrayList<Child>()
    private var parentRadiusOffset = 0f
    var container: View? = null
    var isExpanded = false
    private var isAnimated = false

    fun initChild(icons: SparseArray<Bitmap?>, left: Boolean, top: Boolean, right: Boolean, bottom: Boolean,
                  leftTop: Boolean, leftBottom: Boolean, rightTop: Boolean, rightBottom: Boolean) {
        val childRadius = radius / Child.CHILD_RADIUS_RATIO
        if (left) childSet.add(Child(this, PointF(center.x, center.y), childRadius).apply {
            type = Child.CHILD_LEFT
            icon = icons.get(Child.CHILD_LEFT)
        })
        if (leftTop) childSet.add(Child(this, PointF(center.x, center.y), childRadius).apply {
            type = Child.CHILD_LEFT_TOP
            icon = icons.get(Child.CHILD_LEFT_TOP)
        })
        if (top) childSet.add(Child(this, PointF(center.x, center.y), childRadius).apply {
            type = Child.CHILD_TOP
            icon = icons.get(Child.CHILD_TOP)
        })
        if (rightTop) childSet.add(Child(this, PointF(center.x, center.y), childRadius).apply {
            type = Child.CHILD_RIGHT_TOP
            icon = icons.get(Child.CHILD_RIGHT_TOP)
        })
        if (right) childSet.add(Child(this, PointF(center.x, center.y), childRadius).apply {
            type = Child.CHILD_RIGHT
            icon = icons.get(Child.CHILD_RIGHT)
        })
        if (rightBottom) childSet.add(Child(this, PointF(center.x, center.y), childRadius).apply {
            type = Child.CHILD_RIGHT_BOTTOM
            icon = icons.get(Child.CHILD_RIGHT_BOTTOM)
        })
        if (bottom) childSet.add(Child(this, PointF(center.x, center.y), childRadius).apply {
            type = Child.CHILD_BOTTOM
            icon = icons.get(Child.CHILD_BOTTOM)
        })
        if (leftBottom) childSet.add(Child(this, PointF(center.x, center.y), childRadius).apply {
            type = Child.CHILD_LEFT_BOTTOM
            icon = icons.get(Child.CHILD_LEFT_BOTTOM)
        })
    }

    override fun calculatePoints(animatedFraction: Float) {
        parentRadiusOffset = evaluator.evaluate(animatedFraction, 0f, radius / 8f)
    }

    override fun draw(canvas: Canvas?) {
        if (isAnimated || isExpanded) {
            childSet.forEach {
                it.draw(canvas)
            }
        }

        drawer.rewindParentCircle()
        currentRadius = radius - parentRadiusOffset
        drawer.addCircle(center.x, center.y, currentRadius, true)
        drawer.drawParentCircle(canvas)
        drawer.drawIcon(canvas, icon, center.x, center.y, currentRadius)
    }

    fun expand() {
        if (!isAnimated) {
            isExpanded = true
            startAnimation(0f, 1f)
        }
    }

    fun collapse() {
        if (!isAnimated) {
            isExpanded = false
            startAnimation(1f, 0f)
        }
    }

    fun trigger() {
        if (isExpanded) collapse()
        else expand()
    }

    private fun startAnimation(startValue: Float, endValue: Float) {
        isAnimated = true
        childSet.forEachIndexed { index, child ->
            val delay = ANIMATION_START_DELAY * index
            startChildAnimation(startValue, endValue, delay, child)
        }

        evaluateRadius(startValue, endValue)
    }

    private fun startChildAnimation(startValue: Float, endValue: Float, delay: Long, child: Child) {
        ObjectAnimator.ofFloat(startValue, endValue).apply {
            duration = ANIMATION_DURATION
            startDelay = delay
            interpolator = AnticipateOvershootInterpolator(3f)
            addUpdateListener {
                child.calculatePoints(it.animatedValue as Float)
                container?.invalidate()
            }
            start()
        }
    }

    private fun evaluateRadius(startValue: Float, endValue: Float) {
        ObjectAnimator.ofFloat(startValue, endValue).apply {
            duration = ANIMATION_DURATION + (childSet.size - 1) * ANIMATION_START_DELAY
            interpolator = AnticipateOvershootInterpolator(4f)
            addUpdateListener {
                calculatePoints(it.animatedValue as Float)
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    isAnimated = false
                }
            })
            start()
        }
    }

    override fun handleOnClick(x: Float, y: Float) {
        val clickRect = RectF(center.x - radius, center.y - radius, center.x + radius, center.y + radius)
        if (clickRect.contains(x, y)) {
            listener?.onClick()
        } else {
            childSet.forEach { it.handleOnClick(x, y) }
        }
    }

    override fun setOnClickListener(listener: LavaView.LavaOnClickListener?) {
        this.listener = listener
    }

    override fun setOnClickListener(listener: () -> Unit) {
        setOnClickListener(object : LavaView.LavaOnClickListener {
            override fun onClick() {
                listener()
            }
        })
    }

    fun setChildOnClickListener(childType: Int, listener: LavaView.LavaOnClickListener?) {
        childSet.forEach {
            if (it.type == childType) it.listener = listener
        }
    }

    fun setChildOnClickListener(childType: Int, listener: () -> Unit) {
        setChildOnClickListener(childType, object : LavaView.LavaOnClickListener {
            override fun onClick() {
                listener()
            }
        })
    }

    fun setChildIcon(childType: Int, icon: Bitmap?) {
        childSet.forEach {
            if (it.type == childType) {
                it.icon = icon
                if (isExpanded) container?.invalidate()
            }
        }
    }

    fun enableShadow() = drawer.enableShadow()

    companion object {
        const val ANIMATION_DURATION = 1200L
        const val ANIMATION_START_DELAY = 400L
    }
}