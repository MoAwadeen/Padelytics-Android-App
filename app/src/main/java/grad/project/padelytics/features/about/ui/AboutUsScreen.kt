package grad.project.padelytics.features.about.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import grad.project.padelytics.R
import grad.project.padelytics.features.about.components.AboutUsAppToolbar
import grad.project.padelytics.features.about.components.Description
import grad.project.padelytics.features.about.components.MemberInfoGrid
import grad.project.padelytics.features.about.components.Titles
import grad.project.padelytics.features.about.components.TitlesHeaders
import grad.project.padelytics.features.about.data.TeamInfo
import grad.project.padelytics.features.about.viewModel.AboutViewModel

@Composable
fun AboutUsScreen(modifier: Modifier = Modifier, navController: NavHostController, viewModel: AboutViewModel = viewModel()) {
    val mentor = listOf(
        TeamInfo(
            memberName = "Dr. Samar Elbedwehy",
            memberTitle = "Lecturer at KSU",
            memberPhoto = null,
            memberLinkedIn = ""
        )
    )

    val mobileAppTeam = listOf(
        TeamInfo(
            memberName = "Merna Hesham",
            memberTitle = "Team Leader\nAndroid developer",
            memberPhoto = R.drawable.merna,
            memberLinkedIn = "https://www.linkedin.com/in/merna-hesham-8a94b92b5?utm_source=share&utm_campaign=share_via&utm_content=profile&utm_medium=ios_app"
        ),
        TeamInfo(
            memberName = "Mohamed Awadeen",
            memberTitle = "Android developer\nUi/Ux designer",
            memberPhoto = R.drawable.mohamed,
            memberLinkedIn = "https://www.linkedin.com/in/mohamed-awadeen?utm_source=share&utm_campaign=share_via&utm_content=profile&utm_medium=ios_app"
        ),
        TeamInfo(
            memberName = "Youssef Talaat",
            memberTitle = "iOS developer",
            memberPhoto = null,
            memberLinkedIn = ""
        )
    )

    val aiTeam = listOf(
        TeamInfo(
            memberName = "Mostafa Gamal",
            memberTitle = "Ai Engineer",
            memberPhoto = null,
            memberLinkedIn = ""
        ),
        TeamInfo(
            memberName = "Omar Gomaa",
            memberTitle = "Ai Engineer",
            memberPhoto = null,
            memberLinkedIn = ""
        ),
        TeamInfo(
            memberName = "Dalia Ghazi",
            memberTitle = "Ai Engineer",
            memberPhoto = null,
            memberLinkedIn = ""
        )
    )

    val webDevelopmentTeam = listOf(
        TeamInfo(
            memberName = "Mona Ahmed",
            memberTitle = "Front end developer",
            memberPhoto = null,
            memberLinkedIn = ""
        ),
        TeamInfo(
            memberName = "Aya Mostafa",
            memberTitle = "Back end developer",
            memberPhoto = null,
            memberLinkedIn = ""
        )
    )

    val documentationTeam = listOf(
        TeamInfo(
            memberName = "Rahma Emad",
            memberTitle = "",
            memberPhoto = R.drawable.rahma,
            memberLinkedIn = ""
        ),
        TeamInfo(
            memberName = "Rewan El-Hady",
            memberTitle = "",
            memberPhoto = null,
            memberLinkedIn = ""
        )
    )

    BackHandler {
        navController.popBackStack()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AboutUsAppToolbar()
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ){
            item{
                Spacer(modifier = Modifier.height(16.dp))

                Titles(title = "About us")

                Spacer(modifier = Modifier.height(10.dp))

                Description(description = "We are a team of Artificial Intelligence students developing Padelytics as our graduation project, a mobile and web platform for padel performance analysis. This project reflects our passion for sports technology, innovation, and real-world AI applications.")

                Spacer(modifier = Modifier.height(16.dp))

                Titles(title = "Mission")

                Spacer(modifier = Modifier.height(10.dp))

                Description(description = "Our mission is to empower padel players and coaches with intelligent tools that analyze performance through video and image processing. By using AI, computer vision and machine learning, we aim to deliver real-time, data-driven insights that support training, strategy, and player development.")

                Spacer(modifier = Modifier.height(16.dp))

                Titles(title = "Vision")

                Spacer(modifier = Modifier.height(10.dp))

                Description(description = "Our vision is to become a leading platform in sports technology, making performance analysis accessible, smart, and engaging for the global padel community. We strive to push the boundaries of AI in sports and inspire innovation in athletic improvement.")

                Spacer(modifier = Modifier.height(16.dp))

                Titles(title = "Meet our team")

                Spacer(modifier = Modifier.height(16.dp))

                TitlesHeaders(title = "Our Mentor")

                Spacer(modifier = Modifier.height(16.dp))

                MemberInfoGrid(teamInfo = mentor)

                Spacer(modifier = Modifier.height(16.dp))

                TitlesHeaders(title = "Mobile App Team")

                Spacer(modifier = Modifier.height(16.dp))

                MemberInfoGrid(teamInfo = mobileAppTeam)

                Spacer(modifier = Modifier.height(16.dp))

                TitlesHeaders(title = "AI Team")

                Spacer(modifier = Modifier.height(16.dp))

                MemberInfoGrid(teamInfo = aiTeam)

                Spacer(modifier = Modifier.height(16.dp))

                TitlesHeaders(title = "Web Development Team")

                Spacer(modifier = Modifier.height(16.dp))

                MemberInfoGrid(teamInfo = webDevelopmentTeam)

                Spacer(modifier = Modifier.height(16.dp))

                TitlesHeaders(title = "Documentation Team")

                Spacer(modifier = Modifier.height(16.dp))

                MemberInfoGrid(teamInfo = documentationTeam)

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 3000)
@Composable
fun AboutUsScreenPreview() {
    AboutUsScreen(navController = rememberNavController())
}