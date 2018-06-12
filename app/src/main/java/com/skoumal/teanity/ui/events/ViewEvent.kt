package com.skoumal.teanity.ui.events

/**
 * Class for passing events from ViewModels to Activities/Fragments
 * Variable [handled] used so each event is handled only once
 * (see https://medium.com/google-developers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150)
 * Use [ViewEventObserver] for observing these events
 */
abstract class ViewEvent {

    var handled = false
}