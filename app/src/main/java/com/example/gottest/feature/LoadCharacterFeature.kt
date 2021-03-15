package com.example.gottest.feature

import android.os.Parcelable
import com.badoo.mvicore.element.Actor
import com.badoo.mvicore.element.NewsPublisher
import com.badoo.mvicore.element.Reducer
import com.badoo.mvicore.element.TimeCapsule
import com.badoo.mvicore.feature.ActorReducerFeature
import com.example.gottest.GOTTestApp.Companion.kodein
import com.example.gottest.data.IceAndFireCharacter
import com.example.gottest.feature.LoadCharacterFeature.*
import com.example.gottest.net.IceAndFireApi
import io.reactivex.Observable
import io.reactivex.Observable.just
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.parcel.Parcelize
import org.kodein.di.generic.instance

class LoadCharacterFeature(
    timeCapsule: TimeCapsule<Parcelable>? = null
) : ActorReducerFeature<Wish, Effect, State, News>(
    initialState = timeCapsule?.get(LoadCharactersFeature::class.java) ?: State(),
    actor = ActorImpl(),
    reducer = ReducerImpl(),
    newsPublisher = NewsPublisherImpl()
) {
    init {
        timeCapsule?.register(LoadCharactersFeature::class.java) {
            state.copy(
                isLoading = false
            )
        }
    }

    @Parcelize
    data class State(
        val isLoading: Boolean = false,
        val iceAndFireCharacter: IceAndFireCharacter? = null,
        val error: Throwable? = null
    ) : Parcelable

    sealed class Wish {
        class LoadCharacter(val id: Int) : Wish()
    }

    sealed class Effect {
        object StartedLoading : Effect()
        data class CharacterLoaded(val character: IceAndFireCharacter) : Effect()
        data class ErrorLoading(val throwable: Throwable) : Effect()
    }

    sealed class News {
        data class ErrorExecutingRequest(val throwable: Throwable) : News()
    }

    class ActorImpl : Actor<State, Wish, Effect> {
        private val service by kodein.instance<IceAndFireApi>()

        override fun invoke(state: State, wish: Wish): Observable<Effect> = when (wish) {
            is Wish.LoadCharacter -> loadCharacter(wish.id)
                .map { Effect.CharacterLoaded(it) as Effect }
                .startWith(just(Effect.StartedLoading))
                .onErrorReturn { Effect.ErrorLoading(it) }
        }

        fun loadCharacter(id: Int = 1): Observable<IceAndFireCharacter> {
            return service.getCharacterById(id = id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
        }
    }

    class ReducerImpl : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State = when (effect) {
            Effect.StartedLoading -> state.copy(
                isLoading = true
            )
            is Effect.CharacterLoaded -> state.copy(
                isLoading = false,
                iceAndFireCharacter = effect.character
            )

            is Effect.ErrorLoading -> state.copy(
                isLoading = false,
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