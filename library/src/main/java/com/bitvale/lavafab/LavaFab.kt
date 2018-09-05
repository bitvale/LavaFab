package com.bitvale.lavafab

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.SparseArray
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.bitvale.lavafab.utils.BitmapUtil
import com.bitvale.lavafab.utils.getFloatDimen
import com.bitvale.lavafab.utils.getThemeAccentColor
import com.bitvale.lavafab.utils.getVectorDrawable


/**
 * Created by Alexander Kolpakov on 24.08.2018
 */
class LavaFab : View {

    lateinit var parent: Parent

    @ColorInt
    private var lavaBackgroundColor = 0
    @DrawableRes
    private var parentIcon: Bitmap? = null
    private var drawShadow: Boolean = false

    private var lavaSize = 0f
    private var containerWidth = 0
    private var containerHeight = 0
    private var childFlag = Child.CHILD_NONE

    private val childIcons = SparseArray<Bitmap?>()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet? = null) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

        attrs?.let {
            retrieveAttributes(attrs, defStyleAttr)
        } ?: run {
            lavaBackgroundColor = context.getThemeAccentColor()
            lavaSize = context.getFloatDimen(R.dimen.default_fab_size)
        }

        initParent(lavaSize)
        setupOnClickHandler()
    }

    private fun retrieveAttributes(attrs: AttributeSet, defStyleAttr: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LavaFab, defStyleAttr, R.style.LavaFab)

        lavaBackgroundColor = typedArray.getColor(R.styleable.LavaFab_lavaBackgroundColor, 0)
        lavaSize = typedArray.getDimension(R.styleable.LavaFab_lavaParentSize, 0f)
        childFlag = typedArray.getInteger(R.styleable.LavaFab_lavaChild, (Child.CHILD_LEFT or Child.CHILD_TOP))
        drawShadow = typedArray.getBoolean(R.styleable.LavaFab_lavaDrawShadow, false)

        var drawableResId = typedArray.getResourceId(R.styleable.LavaFab_lavaParentIcon, 0)
        var drawable = context.getVectorDrawable(drawableResId)
        parentIcon = BitmapUtil.getBitmapFromDrawable(drawable)

        drawableResId = typedArray.getResourceId(R.styleable.LavaFab_lavaLeftIcon, 0)
        drawable = context.getVectorDrawable(drawableResId)
        var childIcon = BitmapUtil.getBitmapFromDrawable(drawable)
        childIcons.put(Child.CHILD_LEFT, childIcon)

        drawableResId = typedArray.getResourceId(R.styleable.LavaFab_lavaLeftTopIcon, 0)
        drawable = context.getVectorDrawable(drawableResId)
        childIcon = BitmapUtil.getBitmapFromDrawable(drawable)
        childIcons.put(Child.CHILD_LEFT_TOP, childIcon)

        drawableResId = typedArray.getResourceId(R.styleable.LavaFab_lavaTopIcon, 0)
        drawable = context.getVectorDrawable(drawableResId)
        childIcon = BitmapUtil.getBitmapFromDrawable(drawable)
        childIcons.put(Child.CHILD_TOP, childIcon)

        drawableResId = typedArray.getResourceId(R.styleable.LavaFab_lavaRightTopIcon, 0)
        drawable = context.getVectorDrawable(drawableResId)
        childIcon = BitmapUtil.getBitmapFromDrawable(drawable)
        childIcons.put(Child.CHILD_RIGHT_TOP, childIcon)

        drawableResId = typedArray.getResourceId(R.styleable.LavaFab_lavaRightIcon, 0)
        drawable = context.getVectorDrawable(drawableResId)
        childIcon = BitmapUtil.getBitmapFromDrawable(drawable)
        childIcons.put(Child.CHILD_RIGHT, childIcon)

        drawableResId = typedArray.getResourceId(R.styleable.LavaFab_lavaRightBottomIcon, 0)
        drawable = context.getVectorDrawable(drawableResId)
        childIcon = BitmapUtil.getBitmapFromDrawable(drawable)
        childIcons.put(Child.CHILD_RIGHT_BOTTOM, childIcon)

        drawableResId = typedArray.getResourceId(R.styleable.LavaFab_lavaBottomIcon, 0)
        drawable = context.getVectorDrawable(drawableResId)
        childIcon = BitmapUtil.getBitmapFromDrawable(drawable)
        childIcons.put(Child.CHILD_BOTTOM, childIcon)

        drawableResId = typedArray.getResourceId(R.styleable.LavaFab_lavaLeftBottomIcon, 0)
        drawable = context.getVectorDrawable(drawableResId)
        childIcon = BitmapUtil.getBitmapFromDrawable(drawable)
        childIcons.put(Child.CHILD_LEFT_BOTTOM, childIcon)

        typedArray.recycle()
    }

    private fun initParent(lavaSize: Float) {
        val center = PointF(0f, 0f)
        val radius = lavaSize / 2f
        val padding = radius / 4f

        val left = containsChild(childFlag, Child.CHILD_LEFT)
        val right = containsChild(childFlag, Child.CHILD_RIGHT)
        val top = containsChild(childFlag, Child.CHILD_TOP)
        val bottom = containsChild(childFlag, Child.CHILD_BOTTOM)
        val leftTop = containsChild(childFlag, Child.CHILD_LEFT_TOP)
        val rightTop = containsChild(childFlag, Child.CHILD_RIGHT_TOP)
        val rightBottom = containsChild(childFlag, Child.CHILD_RIGHT_BOTTOM)
        val leftBottom = containsChild(childFlag, Child.CHILD_LEFT_BOTTOM)
        val none = childFlag == Child.CHILD_NONE

        // top & left
        if (top && left && !right && !bottom) {
            containerWidth = (lavaSize * VIEW_SMALL_SIZE_RATIO).toInt()
            containerHeight = containerWidth
            center.x = containerWidth - radius - padding
            center.y = containerWidth - radius - padding
        }

        // top & right
        if (top && !left && right && !bottom) {
            containerWidth = (lavaSize * VIEW_SMALL_SIZE_RATIO).toInt()
            containerHeight = containerWidth
            center.x = radius + padding
            center.y = containerWidth - radius - padding
        }

        // bottom & left
        if (!top && left && !right && bottom) {
            containerWidth = (lavaSize * VIEW_SMALL_SIZE_RATIO).toInt()
            containerHeight = containerWidth
            center.x = containerWidth - radius - padding
            center.y = radius + padding
        }

        // bottom & right
        if (!top && !left && right && bottom) {
            containerWidth = (lavaSize * VIEW_SMALL_SIZE_RATIO).toInt()
            containerHeight = containerWidth
            center.x = radius + padding
            center.y = radius + padding
        }

        // left & right
        if (!top && left && right && !bottom) {
            containerWidth = (lavaSize * VIEW_LARGE_SIZE_RATIO).toInt()
            containerHeight = (lavaSize + padding * 2).toInt()
            center.x = containerWidth / 2f
            center.y = containerHeight / 2f
        }

        // top & bottom
        if (top && !left && !right && bottom) {
            containerWidth = (lavaSize + padding * 2).toInt()
            containerHeight = (lavaSize * VIEW_LARGE_SIZE_RATIO).toInt()
            center.x = containerWidth / 2f
            center.y = containerHeight / 2f
        }

        // top & left & right
        if (top && left && right && !bottom) {
            containerWidth = (lavaSize * VIEW_LARGE_SIZE_RATIO).toInt()
            containerHeight = (lavaSize * VIEW_SMALL_SIZE_RATIO).toInt()
            center.x = containerWidth / 2f
            center.y = containerHeight - radius - padding
        }

        // bottom & left & right
        if (!top && left && right && bottom) {
            containerWidth = (lavaSize * VIEW_LARGE_SIZE_RATIO).toInt()
            containerHeight = (lavaSize * VIEW_SMALL_SIZE_RATIO).toInt()
            center.x = containerWidth / 2f
            center.y = radius + padding
        }

        // left & top & bottom
        if (top && left && !right && bottom) {
            containerWidth = (lavaSize * VIEW_SMALL_SIZE_RATIO).toInt()
            containerHeight = (lavaSize * VIEW_LARGE_SIZE_RATIO).toInt()
            center.x = containerWidth - radius - padding
            center.y = containerHeight / 2f
        }

        // right & top & bottom
        if (top && !left && right && bottom) {
            containerWidth = (lavaSize * VIEW_SMALL_SIZE_RATIO).toInt()
            containerHeight = (lavaSize * VIEW_LARGE_SIZE_RATIO).toInt()
            center.x = radius + padding
            center.y = containerHeight / 2f
        }

        // all
        if (left && right && top && bottom) {
            containerWidth = (lavaSize * VIEW_LARGE_SIZE_RATIO).toInt()
            containerHeight = containerWidth
            center.x = containerWidth / 2f
            center.y = containerWidth / 2f
        }

        // none
        if (none) {
            containerWidth = lavaSize.toInt()
            containerHeight = containerWidth
            center.x = containerWidth / 2f
            center.y = containerWidth / 2f
        }

        parent = Parent(center, radius)
        parent.initChild(childIcons, left, top, right, bottom, leftTop, leftBottom, rightTop, rightBottom)
        parent.container = this
        parent.icon = parentIcon
        parent.drawer.setPaintColor(lavaBackgroundColor)
        if (drawShadow) enableShadow()
    }

    private fun containsChild(flagSet: Int, flag: Int): Boolean {
        return flagSet or flag == flagSet
    }

    override fun onDraw(canvas: Canvas?) {
        parent.draw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(containerWidth, containerHeight)
    }

    private fun setupOnClickHandler() {
        setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) parent.handleOnClick(event.x, event.y)
            true
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        // do nothing
    }

    /**
     * Register a callback to be invoked when the parent fab is clicked.
     *
     * @param listener The callback that will run
     */
    fun setParentOnClickListener(listener: LavaView.LavaOnClickListener) {
        parent.setOnClickListener(listener)
    }

    /**
     * Register a callback to be invoked when the parent fab is clicked.
     *
     * @param listener The callback that will run
     */
    fun setParentOnClickListener(listener: () -> Unit) {
        parent.setOnClickListener(listener)
    }

    /**
     * Register a callback to be invoked when the child fab is clicked.
     *
     * @param listener The callback that will run
     * @param childType The child fab type. Should be one of the following:
     *
     * * [Child.CHILD_TOP],
     * * [Child.CHILD_RIGHT],
     * * [Child.CHILD_BOTTOM],
     * * [Child.CHILD_LEFT],
     * * [Child.CHILD_LEFT_TOP],
     * * [Child.CHILD_RIGHT_TOP],
     * * [Child.CHILD_RIGHT_BOTTOM],
     * * [Child.CHILD_LEFT_BOTTOM],
     */
    fun setChildOnClickListener(@Child.Type childType: Int, listener: LavaView.LavaOnClickListener) {
        parent.setChildOnClickListener(childType, listener)
    }

    /**
     * Register a callback to be invoked when the child fab is clicked.
     *
     * @param listener The callback that will run
     * @param childType The child fab type. Should be one of the following:
     *
     * * [Child.CHILD_TOP],
     * * [Child.CHILD_RIGHT],
     * * [Child.CHILD_BOTTOM],
     * * [Child.CHILD_LEFT],
     * * [Child.CHILD_LEFT_TOP],
     * * [Child.CHILD_RIGHT_TOP],
     * * [Child.CHILD_RIGHT_BOTTOM],
     * * [Child.CHILD_LEFT_BOTTOM],
     */
    fun setChildOnClickListener(@Child.Type childType: Int, listener: () -> Unit) {
        parent.setChildOnClickListener(childType, listener)
    }

    /**
     * Expand view (show child views with animation)
     */
    fun expand() = parent.expand()

    /**
     * Collapse view (hide child views with animation)
     */
    fun collapse() = parent.collapse()

    /**
     * Trigger the view state:
     *
     * if current state is collapsed then call [expand]
     *
     * if current state is expanded then call [collapse]
     */
    fun trigger() = parent.trigger()

    /**
     * @return Whether the view is currently expanded.
     */
    fun isExpanded() = parent.isExpanded

    /**
     * Sets the background color.
     *
     * @param colorId the color resId of the background
     */
    fun setLavaBackgroundResColor(@ColorRes colorId: Int) {
        val color = ContextCompat.getColor(context, colorId)
        lavaBackgroundColor = color
        parent.drawer.setPaintColor(color)
        invalidate()
    }

    /**
     * Sets the background color.
     *
     * @param color the color of the background
     */
    fun setLavaBackgroundColor(@ColorInt color: Int) {
        lavaBackgroundColor = color
        parent.drawer.setPaintColor(color)
        invalidate()
    }

    /**
     * Set the icon to display on the parent fab.
     *
     * @param icon The drawable of the icon
     */
    fun setParentIcon(icon: Drawable?) {
        parent.icon = BitmapUtil.getBitmapFromDrawable(icon)
        invalidate()
    }

    /**
     * Set the icon to display on the parent fab.
     *
     * @param icon The drawable resource id of the icon
     */
    fun setParentIcon(@DrawableRes iconId: Int) {
        val drawable = VectorDrawableCompat.create(resources, iconId, null)
        setParentIcon(drawable)
    }

    /**
     * Set the icon to display on the parent fab.
     *
     * @param icon The drawable of the icon
     * @param childType The child fab type. Should be one of the following:
     *
     * * [Child.CHILD_NONE]
     * * [Child.CHILD_TOP],
     * * [Child.CHILD_RIGHT],
     * * [Child.CHILD_BOTTOM],
     * * [Child.CHILD_LEFT],
     * * [Child.CHILD_LEFT_TOP],
     * * [Child.CHILD_RIGHT_TOP],
     * * [Child.CHILD_RIGHT_BOTTOM],
     * * [Child.CHILD_LEFT_BOTTOM],
     * * [Child.CHILD_ALL]
     */
    fun setChildIcon(@Child.Type childType: Int, icon: Drawable?) {
        val ic = BitmapUtil.getBitmapFromDrawable(icon)
        parent.setChildIcon(childType, ic)
        invalidate()
    }

    /**
     * Set the icon to display on the parent fab.
     *
     * @param icon The drawable resource id of the icon
     * @param childType The child fab type. Should be one of the following:
     *
     * * [Child.CHILD_NONE]
     * * [Child.CHILD_TOP],
     * * [Child.CHILD_RIGHT],
     * * [Child.CHILD_BOTTOM],
     * * [Child.CHILD_LEFT],
     * * [Child.CHILD_LEFT_TOP],
     * * [Child.CHILD_RIGHT_TOP],
     * * [Child.CHILD_RIGHT_BOTTOM],
     * * [Child.CHILD_LEFT_BOTTOM],
     * * [Child.CHILD_ALL]
     */
    fun setChildIcon(@Child.Type childType: Int, @DrawableRes iconId: Int) {
        val drawable = VectorDrawableCompat.create(resources, iconId, null)
        setChildIcon(childType, drawable)
    }

    /**
     * Enable shadow layer.
     */
    fun enableShadow() {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        parent.enableShadow()
    }

    companion object {
        const val VIEW_SMALL_SIZE_RATIO = 2.9f
        const val VIEW_LARGE_SIZE_RATIO = 3.9f
    }
}