package com.skoumal.teanity.view

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment

typealias NavHostId = Int
typealias FragmentId = Int

object NavHostRetriever {

    private val registry = mutableMapOf<NavHostId, FragmentId>()

    fun findNavController(activity: AppCompatActivity) = registry.getOrPut(activity.hashCode()) {
        activity.supportFragmentManager.fragments.asSequence()
            .filterIsInstance<NavHostFragment>()
            .first()
            .id
    }.let {
        activity.findNavController(it)
    }

    fun findNavController(fragment: Fragment) = fragment.requireView().findNavController()

}
