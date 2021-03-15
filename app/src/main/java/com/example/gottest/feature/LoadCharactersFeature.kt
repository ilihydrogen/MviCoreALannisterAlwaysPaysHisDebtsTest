package com.example.gottest.feature

import android.os.Parcelable
import com.badoo.mvicore.element.*
import com.badoo.mvicore.feature.ActorReducerFeature
import com.example.gottest.GOTTestApp.Companion.kodein
import com.example.gottest.data.IceAndFireCharacter
import com.example.gottest.data.IceAndFireCharactersResponse
import com.example.gottest.feature.LoadCharactersFeature.*
import com.example.gottest.net.IceAndFireApi
import io.reactivex.Observable
import io.reactivex.Observable.just
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.parcel.Parcelize
import org.kodein.di.generic.instance

class LoadCharactersFeature(
    timeCapsule: TimeCapsule<Parcelable>? = null
) : ActorReducerFeature<Wish, Effect, State, News>(
    initialState = timeCapsule?.get(LoadCharactersFeature::class.java) ?: State(),
    bootstrapper = BootStrapperImpl(),
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
        val iceAndFireCharacters: IceAndFireCharactersResponse? = null,
        val error: Throwable? = null,
        val append: Boolean = false
    ) : Parcelable

    sealed class Wish {
        object LoadCharacters : Wish()
        class LoadMoreCharacters(val page: Int) : Wish()
    }

    sealed class Effect {
        object StartedLoading : Effect()
        data class CharactersLoaded(val characters: IceAndFireCharactersResponse) : Effect()
        data class MoreCharactersLoaded(val characters: IceAndFireCharactersResponse) : Effect()
        data class ErrorLoading(val throwable: Throwable) : Effect()
    }

    sealed class News {
        data class ErrorExecutingRequest(val throwable: Throwable) : News()
    }

    class BootStrapperImpl : Bootstrapper<Wish> {
        override fun invoke(): Observable<Wish> = just(Wish.LoadCharacters)
    }

    class ActorImpl : Actor<State, Wish, Effect> {
        private val service by kodein.instance<IceAndFireApi>()

        override fun invoke(state: State, wish: Wish): Observable<Effect> = when (wish) {
            is Wish.LoadCharacters -> loadCharacters()
                .map { Effect.CharactersLoaded(IceAndFireCharactersResponse().apply { addAll(it) }) as Effect }
                .startWith(just(Effect.StartedLoading))
                .onErrorReturn { Effect.ErrorLoading(it) }

            is Wish.LoadMoreCharacters -> loadCharacters(page = wish.page)
                .map { Effect.MoreCharactersLoaded(IceAndFireCharactersResponse().apply { addAll(it) }) as Effect }
                .onErrorReturn { Effect.ErrorLoading(it) }
        }

        fun loadCharacters(page: Int = 1): Observable<List<IceAndFireCharacter>> {
            return service.getCharacters(page = page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
        }
    }

    class ReducerImpl : Reducer<State, Effect> {
        override fun invoke(state: State, effect: Effect): State = when (effect) {
            Effect.StartedLoading -> state.copy(
                isLoading = true
            )
            is Effect.CharactersLoaded -> state.copy(
                isLoading = false,
                iceAndFireCharacters = effect.characters,
                append = false
            )
            is Effect.MoreCharactersLoaded -> state.copy(
                isLoading = false,
                iceAndFireCharacters = effect.characters,
                append = true
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