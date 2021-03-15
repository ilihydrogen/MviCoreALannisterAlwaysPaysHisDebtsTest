package com.example.gottest.ui.fragment.main_fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.gottest.GOTTestApp.Companion.kodein
import com.example.gottest.R
import com.example.gottest.data.IceAndFireCharacter
import com.example.gottest.data.IceAndFireCharactersResponse
import com.example.gottest.ui.common.ObservableSourceFragment
import com.example.gottest.ui.event.UIEvent
import com.example.gottest.ui.extension.overScroll
import com.example.gottest.ui.extension.toast
import com.example.gottest.ui.extension.visibleOrNot
import com.example.gottest.ui.fragment.details_fragment.CHARACTER_ID_ARG
import com.example.gottest.ui.viewmodel.ViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_main.*
import org.kodein.di.generic.instance
import org.kodein.di.newInstance

class MainFragment : ObservableSourceFragment<UIEvent>(), Consumer<ViewModel> {
    private var page: Int = 0

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    private val bindings by kodein.newInstance {
        MainFragmentBindings(this@MainFragment, instance())
    }

    override fun getLayout() = R.layout.fragment_main

    private fun characterClicked(characterUIItem: IceAndFireCharacter) {
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

        val bundle = Bundle()
        bundle.putInt(CHARACTER_ID_ARG, characterUIItem.id)
        findNavController().navigate(R.id.nav_action_view_details, bundle, options)
    }

    override fun accept(model: ViewModel?) {
        loading?.visibleOrNot(model?.isLoading ?: true)
        model?.error?.let { context?.toast(it) }
        model?.characters?.let { setList(characters = it, append = model.append) }
    }

    private fun setList(characters: IceAndFireCharactersResponse, append: Boolean = false) {

        if (characters.currentPage > page) {
            adapter.apply {
                if (!append) clear()
                characters
                    .filter { it.name.isNotEmpty() }
                    .map { item -> add(CharacterUIItem(item, ::characterClicked)); }
            }

            if (adapter.itemCount < 20) {
                onNext(UIEvent.RecyclerViewTrigger(page = characters.currentPage + 1))
            }

            page = characters.currentPage
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindings.setup(this)
        initViews()
        onNext(UIEvent.UIReadyToLoad())
    }

    private fun initViews() {
        charactersRv.adapter = adapter
        charactersRv.overScroll()
            ?.subscribe { onNext(UIEvent.RecyclerViewTrigger(page = page + 1)) }
    }

}
