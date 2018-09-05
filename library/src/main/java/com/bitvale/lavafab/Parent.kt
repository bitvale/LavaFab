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
import android.view.MotionEvent
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

    private val childSet = ArrayList<Child>()
    private var parentRadiusOffset = 0f
    private var isAnimated = false

    private var onClickRadiusOffset = 0f
        set(value) {
            field = value
            container?.invalidate()
        }

    val drawer = Drawer()

    var container: View? = null
    var isExpanded = false

    fun initChild(icons: SparseArray<Bitmap?>, left: Boolean, top: Boolean, right: Boolean, bottom: Boolean,
                  leftTop: Boolean, leftBottom: Boolean, rightTop: Boolean, rightBottom: Boolean) {
        val childRadius = radius / Child.CHILD_RADIUS_RATIO
        if (left) childSet.add(Child(this, PointF(center.x, center.y), childRadius).apply {
            type = Child.LEFT
            icon = icons.get(Child.LEFT)
        })
        if (leftTop) childSet.add(Child(this, PointF(center.x, center.y), childRadius).apply {
            type = Child.LEFT_TOP
            icon = icons.get(Child.LEFT_TOP)
        })
        if (top) childSet.add(Child(this, PointF(center.x, center.y), childRadius).apply {
            type = Child.TOP
            icon = icons.get(Child.TOP)
        })
        if (rightTop) childSet.add(Child(this, PointF(center.x, center.y), childRadius).apply {
            type = Child.RIGHT_TOP
            icon = icons.get(Child.RIGHT_TOP)
        })
        if (right) childSet.add(Child(this, PointF(center.x, center.y), childRadius).apply {
            type = Child.RIGHT
            icon = icons.get(Child.RIGHT)
        })
        if (rightBottom) childSet.add(Child(this, PointF(center.x, center.y), childRadius).apply {
            type = Child.RIGHT_BOTTOM
            icon = icons.get(Child.RIGHT_BOTTOM)
        })
        if (bottom) childSet.add(Child(this, PointF(center.x, center.y), childRadius).apply {
            type = Child.BOTTOM
            icon = icons.get(Child.BOTTOM)
        })
        if (leftBottom) childSet.add(Child(this, PointF(center.x, center.y), childRadius).apply {
            type = Child.LEFT_BOTTOM
            icon = icons.get(Child.LEFT_BOTTOM)
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
        currentRadius = radius - parentRadiusOffset + onClickRadiusOffset
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
        if (checkOnClickXY(x, y)) {
            listener?.onClick()
        } else {
            childSet.forEach {
                it.handleOnClick(x, y)
            }
        }
    }

    fun proceedOnTouch(event: MotionEvent): Boolean {
        val inParent = checkOnClickXY(event.x, event.y)
        val inChild = if (!inParent) checkChildOnClickXY(event.x, event.y) else false
        if (inParent) onClickRadiusOffset = 2f
        when(event.action) {
            MotionEvent.ACTION_UP -> {
                if (inParent || inChild) {
                    onClickRadiusOffset = 0f
                    handleOnClick(event.x, event.y)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (!inParent) onClickRadiusOffset = 0f
            }
        }
        return inParent || inChild
    }

    private fun checkOnClickXY(x: Float, y: Float): Boolean {
        val clickRect = RectF(center.x - radius, center.y - radius, center.x + radius, center.y + radius)
        return clickRect.contains(x, y)
    }

    private fun checkChildOnClickXY (x: Float, y: Float) = childSet.any{ it.checkOnClickXY(x, y) }

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