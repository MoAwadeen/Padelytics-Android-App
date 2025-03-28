package grad.project.padelytics.features.profile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import grad.project.padelytics.R
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import grad.project.padelytics.ui.theme.GreenLight
import grad.project.padelytics.ui.theme.lexendFontFamily
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import grad.project.padelytics.appComponents.MidDarkHeadline
import grad.project.padelytics.appComponents.SimiMidDarkHeadline
import grad.project.padelytics.ui.theme.Blue
import grad.project.padelytics.ui.theme.BlueDark
import grad.project.padelytics.ui.theme.GreenDark
import grad.project.padelytics.ui.theme.WhiteGray


@Composable
fun ProfileHeader(
    backgroundImage: Int = R.drawable.profilebg,
    avatarImage: Painter = painterResource(id = R.drawable.shop),
    avatarSize: Dp = 80.dp,
    iconOnClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(Transparent)
    ) {

        Box(modifier = Modifier.background(Transparent)) {
            Image(
                painter = painterResource(id = backgroundImage),
                contentDescription = "Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
                    .background(Transparent)
            )
        }

        Box(
            modifier = Modifier
                .size(avatarSize)
                .align(Alignment.BottomCenter)
                .offset(y = avatarSize / 2)
                .shadow(8.dp, CircleShape)
                .background(Transparent)
        ) {
            Image(
                painter = avatarImage,
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .border(4.dp, GreenLight, CircleShape)
                    .clip(CircleShape)
                    .background(Transparent)
            )
        }

        Box(modifier = Modifier
            .size(avatarSize)
            .align(Alignment.BottomEnd)
            .offset(y = avatarSize /2)
            .background(Transparent)){
            Image(
                painter = painterResource(id = R.drawable.edit),
                contentDescription = "Edit Icon",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { iconOnClick() },
                colorFilter = ColorFilter.tint(GreenLight),
                contentScale = ContentScale.Fit,
                alignment = Alignment.BottomEnd,
            )
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_8")
@Composable
fun PreviewProfileHeader(modifier: Modifier = Modifier){
    ProfileHeader(iconOnClick = {})
}

@Composable
fun NumberLabelChip(
    number: Int,
    label: String,
    chipColor: Color,
    textColor: Color
) {
    AssistChip(
        modifier = Modifier.wrapContentSize(),
        onClick = {},
        label = { Text(text = "$number$label", color = textColor, fontFamily = lexendFontFamily, fontWeight = FontWeight.Bold, fontSize = 10.sp)},
        enabled = false, // Makes it visually unclickable
        colors = AssistChipDefaults.assistChipColors(
            containerColor = chipColor, // Custom chip background color
            disabledContainerColor = chipColor // Ensure it remains colored when disabled
        ),
        shape = MaterialTheme.shapes.large
    )
}


@Composable
fun NumberChipsRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NumberLabelChip(number = 10, label = "Wins", chipColor = GreenLight, textColor = Color.White)
        NumberLabelChip(number = 5, label = "Losses", chipColor = Color.Red, textColor = Color.White)
        NumberLabelChip(number = 3, label = "Draws", chipColor = Color.Gray, textColor = Color.White)
    }
}

@Preview
@Composable
fun PreviewNumberChipsRow() {
    NumberChipsRow()
}

@Composable
fun InfoRow(
    icon: Painter,
    label: String,
    value: String? = null // Optional value
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        SimiMidDarkHeadline(label,16)

        Spacer(modifier = Modifier.weight(1f))

        value?.let {
            Text(
                text = it,
                color = GreenDark,
                fontSize = 16.sp,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}


@Composable
fun InfoColumn() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        InfoRow(
            icon = painterResource(id = R.drawable.user),
            label = "Username",
            value = "Mohamed"
        )

        InfoRow(
            icon = painterResource(id = R.drawable.coupon),
            label = "Email",
            value = "mohamed@example.com"
        )

        InfoRow(
            icon = painterResource(id = R.drawable.notifications),
            label = "Settings" // No value, optional
        )
    }
}

@Preview
@Composable
fun PreviewInfoColumn() {
    InfoColumn()
}


@Composable
fun LogoutButton(onClick: () -> Unit) {

    Button(
        colors = ButtonDefaults.buttonColors(containerColor = White),
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        border = BorderStroke(5.dp, GreenLight),
        shape = RoundedCornerShape(100.dp),
        elevation = ButtonDefaults.buttonElevation(0.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),

            ) {
            Image(
                painter = painterResource(id = R.drawable.logout), // Add Google logo in drawable
                contentDescription = "Google Sign-In",
                modifier = Modifier
                    .size(28.dp)
            )
            Spacer(modifier = Modifier.width(18.dp))
            Text(
                text = "Log out",
                color = BlueDark,
                fontSize = 20.sp,
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
        }
    }
}

@Preview
@Composable
fun LogoutButtonPreview() {
    LogoutButton(onClick = {})
}

