package com.endeavorsheep.comiclibrary

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.endeavorsheep.comiclibrary.ui.theme.ComicLibraryTheme
import com.endeavorsheep.comiclibrary.view.CharactersBottomNav
import com.endeavorsheep.comiclibrary.view.CharactersDetailScreen
import com.endeavorsheep.comiclibrary.view.CollectionScreen
import com.endeavorsheep.comiclibrary.view.LibraryScreen
import com.endeavorsheep.comiclibrary.viewmodel.LibraryApiViewModel
import dagger.hilt.android.AndroidEntryPoint

sealed class Destination(val route: String) {
    object Library : Destination("library")
    object Collection : Destination("collection")
    object CharacterDetail : Destination("character/{characterId}") {
        fun createRoute(characterId: Int?) = "character/$characterId"
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val lvm by viewModels<LibraryApiViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComicLibraryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Project
                    val navController = rememberNavController()
                    CharactersScaffold(navController = navController, lvm)
                }
            }
        }
    }
}

@Composable
fun CharactersScaffold(navController: NavHostController, lvm: LibraryApiViewModel) {
    val scaffoldState = rememberScaffoldState()
    val ctx = LocalContext.current
    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = { CharactersBottomNav(navController = navController) }
    ) { paddingValues ->
        NavHost(navController = navController, startDestination = Destination.Library.route) {
            composable(Destination.Library.route) {
                LibraryScreen(navController, lvm, paddingValues)
            }
            composable(Destination.Collection.route) {
                CollectionScreen()
            }
            composable(Destination.CharacterDetail.route) { navBackStackEntry ->
                val id = navBackStackEntry.arguments?.getString("characterId")?.toIntOrNull()
                if (id == null)
                    Toast.makeText(ctx, "Character id is required", Toast.LENGTH_SHORT).show()
                else {
                    lvm.retrieveSingleCharacter(id)
                    CharactersDetailScreen()
                }
            }
        }
    }
}
