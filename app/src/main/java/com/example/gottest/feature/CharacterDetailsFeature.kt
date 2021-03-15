package com.example.gottest.feature

import android.os.Parcelable
import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.element.TimeCapsule
import com.badoo.mvicore.feature.ActorReducerFeature
import com.example.gottest.GOTTestApp.Companion.kodein
import com.example.gottest.data.BaseAPIResponse
import com.example.gottest.feature.CharacterDetailsFeature.*
import com.example.gottest.net.IceAndFireApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.parcel.Parcelize
import org.kodein.di.generic.instance

class CharacterDetailsFeature(
    timeCapsule: TimeCapsule<Parcelable>? = null
) : ActorReducerFeature<Wish, Effect, State, News>(
    initialState = timeCapsule?.get(LoadCharactersFeature::class.java) ?: State(),
    actor = ActorImpl(),
    reducer = ReducerImpl(),
    newsPublisher = NewsPublisherImpl()
) {
    @Parcelize
    data class State(
        val result: Pair<Int, String>? = null,
        val error: Throwable? = null
    ) : Parcelable

    sealed class Wish {
        class LoadDetails(val id: Int, val url: String) : Wish()
    }

    sealed class Effect {
        data class DetailsLoaded(val result: Pair<Int, String>) : Effect()
        data class ErrorLoading(val throwable: Throwable) : Effect()
    }

    sealed class News {
        data class ErrorExecutingRequest(val throwable: Throwable) : News()
    }

    class ActorImpl : Actor<State, Wish, Effect> {
        private val service by kodein.instance<IceAndFireApi>()

        override fun invoke(state: State, wish: Wish): Observable<Effect> = when (wish) {
            is Wish.LoadDetails -> loadDetails(url = wish.url)
                .map { Effect.DetailsLoaded(wish.id to it.name) as Effect }
                .onErrorReturn { Effect.ErrorLoading(it) }
        }

        private fun loadDetails(url: String): Observable<BaseAPIResponse> {
            return service.getDetailsByUrl(url = url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
        }
    }

    class ReducerImpl : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State = when (effect) {
            is Effect.DetailsLoaded -> state.copy(
                result = effect.result
            )

            is Effect.ErrorLoading -> state.copy(
                error = effect.throwable
            )
        }
    }

    class NewsPublisherImpl : NewsPublisher<Wish, Effect, State, News> {
        override fun invoke(wish: Wish, effect: Effect, state: State): News? = when (effect) {
            is Effect.ErrorLoading -> News.ErrorExecutingRequest(effect.throwable)
            else -> null
        }
    }
}