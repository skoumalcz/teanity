package com.skoumal.teanity.viewevents

/**
 * Class for passing events from ViewModels to Activities/Fragments
 * Variable [isConsumed] used so each event is consumed only once
 * (see https://medium.com/google-developers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150)
 * Use [ViewEventObserver] for observing these events
 */
abstract class ViewEvent {

    var isConsumed = false
        private set

    fun consume() {
        isConsumed = true
    }
}