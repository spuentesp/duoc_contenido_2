package com.example.ejemploguias.ejemplos.navegacion

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @Test
    fun `MainViewModel emite NavigateTo al navegar`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val viewModel = MainViewModel()
            val eventos = mutableListOf<NavigationEvent>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.navigationEvents.collect { eventos.add(it) }
            }

            viewModel.navigateTo(Screen.Profile)
            advanceUntilIdle()

            assertEquals(
                listOf(NavigationEvent.NavigateTo(route = Screen.Profile)),
                eventos
            )
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun `MainViewModel emite PopBackStack al volver`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val viewModel = MainViewModel()
            val eventos = mutableListOf<NavigationEvent>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.navigationEvents.collect { eventos.add(it) }
            }

            viewModel.navigateBack()
            advanceUntilIdle()

            assertEquals(listOf(NavigationEvent.PopBackStack), eventos)
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun `MainViewModelMejorado emite el mismo flujo de eventos`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val viewModel = MainViewModelMejorado()
            val eventos = mutableListOf<NavigationEvent>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                viewModel.navigationEvents.collect { eventos.add(it) }
            }

            viewModel.navigateTo(Screen.Settings)
            viewModel.navigateUp()
            advanceUntilIdle()

            assertEquals(
                listOf(
                    NavigationEvent.NavigateTo(route = Screen.Settings),
                    NavigationEvent.NavigateUp
                ),
                eventos
            )
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun `Detail buildRoute reemplaza el argumento en la ruta`() {
        assertEquals("detail_page/42", Screen.Detail(itemId = "42").buildRoute())
    }

    @Test
    fun `las rutas de la guía son home_page profile_page settings_page`() {
        assertEquals("home_page", Screen.Home.route)
        assertEquals("profile_page", Screen.Profile.route)
        assertEquals("settings_page", Screen.Settings.route)
    }
}
