package grad.project.padelytics.features.tournaments.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import grad.project.padelytics.appComponents.BottomAppBar
import grad.project.padelytics.features.tournaments.components.TournamentAppToolbar
import grad.project.padelytics.features.tournaments.components.TournamentDetails

@Composable
fun TournamentDetailsScreen(modifier: Modifier = Modifier, navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            TournamentAppToolbar(navController, tournamentName = "Tournament Name") },
        bottomBar = {
            BottomAppBar(navController, currentRoute) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ){
            item {
                TournamentDetails(navController)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TournamentDetailsScreenPreview() {
    TournamentDetailsScreen(navController = rememberNavController())
}