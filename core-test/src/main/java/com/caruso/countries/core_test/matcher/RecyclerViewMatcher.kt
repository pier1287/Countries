package com.caruso.countries.core_test.matcher

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

fun hasItemAtPosition(position: Int, matcher: Matcher<View>): Matcher<View> {
    return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {

        override fun describeTo(description: Description?) {
            description?.appendText("has item at position $position : ")
            matcher.describeTo(description)
        }

        override fun matchesSafely(item: RecyclerView?): Boolean {
            val viewHolder = item?.findViewHolderForAdapterPosition(position)
            return matcher.matches(viewHolder?.itemView)
        }
    }
}
