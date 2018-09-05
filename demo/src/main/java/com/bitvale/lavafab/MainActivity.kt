package com.bitvale.lavafab

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by Alexander Kolpakov on 27.08.2018
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViews()
    }

    private fun setupViews() {
        lava_fab_bottom_right.setLavaBackgroundResColor(R.color.color_purple)

        with(lava_fab_center) {
            setParentOnClickListener { lava_fab_center.trigger() }
            setChildOnClickListener(Child.TOP) { showToast() }
            setChildOnClickListener(Child.RIGHT_TOP) { showToast() }
            setChildOnClickListener(Child.RIGHT) { showToast() }
            setChildOnClickListener(Child.RIGHT_BOTTOM) { showToast() }
            setChildOnClickListener(Child.BOTTOM) { showToast() }
            setChildOnClickListener(Child.LEFT_BOTTOM) { showToast() }
            setChildOnClickListener(Child.LEFT) { showToast() }
            setChildOnClickListener(Child.LEFT_TOP) { showToast() }
        }

        with(lava_fab_top_left) {
            setParentOnClickListener { lava_fab_top_left.trigger() }
            setChildOnClickListener(Child.RIGHT) { showToast() }
            setChildOnClickListener(Child.LEFT) { showToast() }
        }

        with(lava_fab_top_right) {
            setParentOnClickListener { lava_fab_top_right.trigger() }
            setChildOnClickListener(Child.BOTTOM) { showToast() }
            setChildOnClickListener(Child.LEFT) { showToast() }
        }

        with(lava_fab_bottom_left) {
            setParentOnClickListener { lava_fab_bottom_left.trigger() }
            setChildOnClickListener(Child.TOP) { showToast() }
            setChildOnClickListener(Child.RIGHT) { showToast() }
            setChildOnClickListener(Child.LEFT) { showToast() }
        }

        with(lava_fab_bottom_right) {
            setParentOnClickListener { lava_fab_bottom_right.trigger() }
            setChildOnClickListener(Child.TOP) { showToast() }
            setChildOnClickListener(Child.LEFT) { showToast() }
            setChildOnClickListener(Child.LEFT_TOP) { showToast() }
        }

        with(lava_fab_left_center) {
            setParentOnClickListener { lava_fab_left_center.trigger() }
            setChildOnClickListener(Child.TOP) { showToast() }
            setChildOnClickListener(Child.BOTTOM) { showToast() }
        }
    }

    private fun showToast() = Toast.makeText(this, "Child clicked", Toast.LENGTH_SHORT).show()
}
