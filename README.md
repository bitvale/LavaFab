# LavaFab

<img src="/art/preview.gif" alt="sample" title="sample" width="320" height="600" align="right" vspace="52" />

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-19%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=19)

This is an Android project allowing to animate a Floating Action Button menu with "lava" animation.

USAGE
-----

Just add LavaFab view in your layout XML and LavaFab library in your project via Gradle:

```gradle
dependencies {
  implementation 'com.bitvale:lavafab:1.0.1'
}
```

XML
-----

```xml
<com.bitvale.lavafab.LavaFab
    android:id="@+id/lava_fab"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:lavaBackgroundColor="@color/color_pink"
    app:lavaChild="left|top"
    app:lavaParentSize="@dimen/fab_size"
    app:lavaParentIcon="@drawable/ic_parent"
    app:lavaLeftIcon="@drawable/ic_left"
    app:lavaTopIcon="@drawable/ic_right" 
    app:lavaDrawShadow="true" />
```

You must use the following properties in your XML to change your LavaFab.


##### Properties:

* `app:lavaBackgroundColor`         (color)     -> default  ?attr/colorAccent
* `app:lavaParentSize`              (dimension) -> default  56dp
* `app:lavaDrawShadow`              (boolean)   -> default  false
* `app:lavaChild`                   (flag)      -> default  left|top
* `app:lavaParentIcon`              (drawable)  -> default  none
* `app:lavaLeftIcon`                (drawable)  -> default  none
* `app:lavaLeftTopIcon`             (drawable)  -> default  none
* `app:lavaTopIcon`                 (drawable)  -> default  none
* `app:lavaRightTopIcon`            (drawable)  -> default  none
* `app:lavaRightIcon`               (drawable)  -> default  none
* `app:lavaRightBottomIcon`         (drawable)  -> default  none
* `app:lavaBottomIcon`              (drawable)  -> default  none
* `app:lavaLeftBottomIcon`          (drawable)  -> default  none


Kotlin
-----

```kotlin
with(lava_fab) {
    setLavaBackgroundResColor(R.color.fab_color)
    setParentOnClickListener { lava_fab_center.trigger() }
    setChildOnClickListener(Child.TOP) { lava_fab_center.collapse() }
    setChildOnClickListener(Child.LEFT) { // some action }
    enableShadow()
    setParentIcon(R.drawable.ic_parent)
    setChildIcon(Child.TOP, R.drawable.ic_child_top)
    setChildIcon(Child.LEFT, R.drawable.ic_child_left)
}
```

LICENCE
-----

LavaFab by [Alexander Kolpakov](https://play.google.com/store/apps/dev?id=7044571013168957413) is licensed under an [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).