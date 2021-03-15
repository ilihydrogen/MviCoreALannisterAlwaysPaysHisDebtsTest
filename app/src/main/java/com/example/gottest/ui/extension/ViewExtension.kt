package com.example.gottest.ui.extension

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding4.recyclerview.RecyclerViewScrollEvent
import com.jakewharton.rxbinding4.recyclerview.scrollEvents
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

fun View.visibleOrNot(loading: Boolean) {
    this.visibility = if (loading) View.VISIBLE else View.INVISIBLE
}

fun RecyclerView.overScroll(): @NonNull Observable<Boolean>? {

    fun detectOverScroll(event: RecyclerViewScrollEvent): Boolean {
        if (event.dy > 0) {
            val rv = event.view
            val pos: Int =
                (rv.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            val numItems: Int = rv.adapter?.itemCount ?: 0
            return pos >= numItems - 1
        }

        return false
    }

    return this.scrollEvents().map { detectOverScroll(it) }.debounce(1, TimeUnit.SECONDS)
        .filter { it }
}