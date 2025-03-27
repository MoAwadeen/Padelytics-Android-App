package grad.project.padelytics.features.tournaments.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import grad.project.padelytics.appComponents.AppToolbar
import grad.project.padelytics.appComponents.BottomAppBar
import grad.project.padelytics.features.tournaments.components.GridItem
import grad.project.padelytics.features.tournaments.viewModel.TournamentsViewModel
import grad.project.padelytics.navigation.Routes

@Composable
fun TournamentsScreen(modifier: Modifier = Modifier, navController: NavHostController, viewModel: TournamentsViewModel = viewModel()) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            AppToolbar(toolbarTitle = "Tournaments") },
        bottomBar = {
            BottomAppBar(navController, currentRoute) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                items(10) {
                    GridItem(tournament="Tournament", prize="Prize", date="Date", onClick = {navController.navigate(Routes.TOURNAMENT_DETAILS)})
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TournamentsScreenPreview() {
    TournamentsScreen(navController = rememberNavController())
}