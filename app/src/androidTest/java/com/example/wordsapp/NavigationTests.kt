package com.example.wordsapp

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Assert.assertEquals
import org.junit.Test


class NavigationTests {

    @Test
    fun navigate_to_words_nav_component(){

        //OBTENEMOS EL NAVCONTROLLER
        val navController = TestNavHostController( ApplicationProvider.getApplicationContext() )

        //SE INICIALIZA EL FRAGMENT A TESTEAR, ES EQUIVALENTE  A ActivityScenarioRule
        val letterListScenario : FragmentScenario<LetterListFragment> = launchFragmentInContainer<LetterListFragment>( themeResId = R.style.Theme_Words )

        //ESTAMOS ENTRANDO EN EL FRAGMENTO
        letterListScenario.onFragment { thisFragment ->

            //DECIMOS QUE GRAFO DE NAVEGACIÓN DEBE USAR NUESTRO CONTROLER
            navController.setGraph( R.navigation.nav_graph )

            //DEFINIMOS EL CONTROLADOR QUE VA A USAR EL FRAGMENT
            Navigation.setViewNavController( thisFragment.requireView() , navController )

        }

        onView(
            withId(
                R.id.recycler_view
            )
        ).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(2, click() )
        )

        assertEquals( navController.currentDestination?.id, R.id.wordListFragment)

    }
}