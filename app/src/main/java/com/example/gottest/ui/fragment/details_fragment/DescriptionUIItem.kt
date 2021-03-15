package com.example.gottest.ui.fragment.details_fragment

import android.view.View
import com.example.gottest.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.character_description_list_item.view.*

class DescriptionUIItem(
    val info: Pair<Int, String>,
    private val onClick: ((position: Int) -> Unit)? = null,
    private val textUpdated: PublishSubject<Pair<Int, String>>? = null
) : Item() {

    private var disposableObserver: Disposable? = null

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val view: View = viewHolder.containerView
        val itemValue = view.itemValue
        val itemKey = view.itemKey

        itemKey.setText(info.first)
        if (info.second != ".") itemValue.text = info.second

        view.setOnClickListener { onClick?.invoke(position) }
        disposableObserver = textUpdated?.subscribe {
            if (it.first == info.first) {
                var s = "${itemValue.text}, ${it.second}"
                if (s.startsWith(", ")) s = s.replaceFirst(", ", "")
                itemValue.text = s
            }
        }
    }

    override fun getLayout(): Int {
        return R.layout.character_description_list_item
    }

    override fun onViewDetachedFromWindow(viewHolder: GroupieViewHolder) {
        if (disposableObserver != null) {
            disposableObserver!!.dispose()
        }
    }

}