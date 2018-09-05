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
            setChildOnClickListener(Child.CHILD_TOP) { showToast() }
            setChildOnClickListener(Child.CHILD_RIGHT_TOP) { showToast() }
            setChildOnClickListener(Child.CHILD_RIGHT) { showToast() }
            setChildOnClickListener(Child.CHILD_RIGHT_BOTTOM) { showToast() }
            setChildOnClickListener(Child.CHILD_BOTTOM) { showToast() }
            setChildOnClickListener(Child.CHILD_LEFT_BOTTOM) { showToast() }
            setChildOnClickListener(Child.CHILD_LEFT) { showToast() }
            setChildOnClickListener(Child.CHILD_LEFT_TOP) { showToast() }
        }

        with(lava_fab_top_left) {
            setParentOnClickListener { lava_fab_top_left.trigger() }
            setChildOnClickListener(Child.CHILD_RIGHT) { showToast() }
            setChildOnClickListener(Child.CHILD_LEFT) { showToast() }
        }

        with(lava_fab_top_right) {
            setParentOnClickListener { lava_fab_top_right.trigger() }
            setChildOnClickListener(Child.CHILD_BOTTOM) { showToast() }
            setChildOnClickListener(Child.CHILD_LEFT) { showToast() }
        }

        with(lava_fab_bottom_left) {
            setParentOnClickListener { lava_fab_bottom_left.trigger() }
            setChildOnClickListener(Child.CHILD_TOP) { showToast() }
            setChildOnClickListener(Child.CHILD_RIGHT) { showToast() }
            setChildOnClickListener(Child.CHILD_LEFT) { showToast() }
        }

        with(lava_fab_bottom_right) {
            setParentOnClickListener { lava_fab_bottom_right.trigger() }
            setChildOnClickListener(Child.CHILD_TOP) { showToast() }
            setChildOnClickListener(Child.CHILD_LEFT) { showToast() }
            setChildOnClickListener(Child.CHILD_LEFT_TOP) { showToast() }
        }

        with(lava_fab_left_center) {
            setParentOnClickListener { lava_fab_left_center.trigger() }
            setChildOnClickListener(Child.CHILD_TOP) { showToast() }
            setChildOnClickListener(Child.CHILD_BOTTOM) { showToast() }
        }
    }

    private fun showToast() = Toast.makeText(this, "Child clicked", Toast.LENGTH_SHORT).show()
}
