package com.example.wordsapp.util

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.annotation.MenuRes
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle

private var menuCreated: Menu? = null

//FORMA INTERESANTE NUEVA DE AGREGAR UN MENU
fun Fragment.addMenuProvider(
    @MenuRes menuRes: Int,
    callback: (item: MenuItem) -> Boolean
){

    val menuProvider = object : MenuProvider{

        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(menuRes, menu)
            menuCreated = menu
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean = callback(menuItem)

    }

    (requireActivity() as MenuHost).addMenuProvider(
        menuProvider,
        viewLifecycleOwner,
        Lifecycle.State.STARTED
    )

}

fun Fragment.getIconMenu(): Menu? = menuCreated
