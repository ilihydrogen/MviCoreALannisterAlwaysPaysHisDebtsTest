package com.example.gottest.ui.fragment.details_fragment

import android.os.Bundle
import android.view.View
import com.example.gottest.GOTTestApp.Companion.kodein
import com.example.gottest.R
import com.example.gottest.data.IceAndFireCharacter
import com.example.gottest.ui.common.ObservableSourceFragment
import com.example.gottest.ui.event.UIEvent
import com.example.gottest.ui.extension.getSeparated
import com.example.gottest.ui.extension.toast
import com.example.gottest.ui.extension.visibleOrNot
import com.example.gottest.ui.viewmodel.ViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_character_details.*
import kotlinx.android.synthetic.main.fragment_main.loading
import org.kodein.di.generic.instance
import org.kodein.di.newInstance

const val CHARACTER_ID_ARG = "charId"

class CharacterDetailsFragment : ObservableSourceFragment<UIEvent>(), Consumer<ViewModel> {
    private var characterId: Int = 0

    private val bindings by kodein.newInstance {
        DetaisFragmentBindings(this@CharacterDetailsFragment, instance(), instance())
    }

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    private val detailsUpdaterSubject = PublishSubject.create<Pair<Int, String>>()

    override fun getLayout() = R.layout.fragment_character_details

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            characterId = it.getInt(CHARACTER_ID_ARG)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindings.setup(this)
        initViews()
        onNext(UIEvent.UIReadyToLoad(value = characterId))
    }

    private fun initViews() {
        detailsRv.adapter = adapter
    }

    override fun accept(model: ViewModel?) {
        loading?.visibleOrNot(model?.isLoading ?: true)
        model?.error?.let { context?.toast(it) }
        model?.character?.let { applyModel(it) }
        model?.characterDetails.let { detailsUpdaterSubject.onNext(it ?: 0 to "") }
    }

    private fun applyModel(it: IceAndFireCharacter) {

        if (adapter.itemCount > 0) return

        fun createDescriptionUIItem(name: Int, value: String): DescriptionUIItem {
            if (value.startsWith("https:")) {
                onNext(UIEvent.DetailsRequired(resId = name, url = value))
                return DescriptionUIItem(info = name to ".", textUpdated = detailsUpdaterSubject)
            }

            return DescriptionUIItem(info = name to value, textUpdated = detailsUpdaterSubject)
        }

        fun createDescriptionUIItem(name: Int, value: List<String>): DescriptionUIItem {
            var willBeLoaded = false

            value.map {
                if (it.startsWith("https:")) {
                    willBeLoaded = true
                    onNext(UIEvent.DetailsRequired(resId = name, url = it))
                }
            }

            return DescriptionUIItem(
                info = name to if (willBeLoaded) "." else value.getSeparated(),
                textUpdated = detailsUpdaterSubject
            )
        }

        characterName.text = it.name

        adapter.apply {
            addAll(listOf(
                createDescriptionUIItem(R.string.gender, it.gender),
                createDescriptionUIItem(R.string.culture, it.culture),
                createDescriptionUIItem(R.string.born, it.born),
                createDescriptionUIItem(R.string.died, it.died),
                createDescriptionUIItem(R.string.titles, it.titles),
                createDescriptionUIItem(R.string.aliases, it.aliases),
                createDescriptionUIItem(R.string.spouse, it.spouse),
                createDescriptionUIItem(R.string.allegiances, it.allegiances),
                createDescriptionUIItem(R.string.books, it.books),
                createDescriptionUIItem(R.string.povBooks, it.povBooks),
                createDescriptionUIItem(R.string.tvSeries, it.tvSeries),
                createDescriptionUIItem(R.string.playedBy, it.playedBy)
            ).filter { it.info.second.trim().isNotEmpty() }
            )
        }
    }

}