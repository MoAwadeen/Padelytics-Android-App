package grad.project.padelytics.features.profile.components

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import grad.project.padelytics.R
import grad.project.padelytics.appComponents.SimiMidDarkHeadline
import grad.project.padelytics.data.NotificationPreferencesManager
import grad.project.padelytics.ui.theme.Blue
import grad.project.padelytics.ui.theme.BlueDark
import grad.project.padelytics.ui.theme.GreenDark
import grad.project.padelytics.ui.theme.GreenLight
import grad.project.padelytics.ui.theme.WhiteGray
import grad.project.padelytics.ui.theme.lexendFontFamily
import kotlin.math.roundToInt

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
    value: String? = null,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
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

@Composable
fun LogoutConfirmationDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirmLogout: () -> Unit
) {
    if (!showDialog) return

    val context = LocalContext.current

    AlertDialog(
        containerColor = Blue,
        titleContentColor = BlueDark,
        textContentColor = GreenLight,
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Log out",
                style = TextStyle(
                    fontSize = 22.sp,
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = WhiteGray
                )
            )
        },
        text = {
            Text(
                text = "Are you sure you want to log out from your account?",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = BlueDark
                )
            )
        },
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenLight,
                    contentColor = BlueDark
                ),
                onClick = {
                    onDismiss()
                    onConfirmLogout()
                    Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
                }
            ) {
                Text(
                    text = "YES",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = BlueDark
                    )
                )
            }
        },
        dismissButton = {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenDark,
                    contentColor = GreenLight
                ),
                onClick = onDismiss
            ) {
                Text(
                    text = "NO",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = GreenLight
                    )
                )
            }
        }
    )
}

@Composable
fun NotificationRow(context: Context = LocalContext.current) {
    val state by NotificationPreferencesManager.notificationState.collectAsState()

    LaunchedEffect(Unit) {
        NotificationPreferencesManager.loadState(context)
    }

    val iconPainter = if (state) painterResource(id = R.drawable.notifications)
    else painterResource(id = R.drawable.mute)

    var showPermissionDialog by remember { mutableStateOf(false) }

    if (showPermissionDialog) {
        AlertDialog(
            containerColor = Blue,
            titleContentColor = BlueDark,
            textContentColor = GreenLight,
            onDismissRequest = { showPermissionDialog = false },
            confirmButton = {
                TextButton(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GreenLight,
                        contentColor = BlueDark
                    ),
                    onClick = {
                    showPermissionDialog = false
                    openAppNotificationSettings(context)
                }
                ) {
                    Text(
                        text = "Open Settings",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = lexendFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = BlueDark
                        )
                    )
                }
            },
            dismissButton = {
                TextButton(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GreenDark,
                        contentColor = GreenLight),
                    onClick = {
                    showPermissionDialog = false
                }
                ) {
                    Text(
                        text = "Cancel",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = lexendFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = GreenLight
                        )
                    )
                }
            },
            title = {Text(
                    text = "Allow Notifications",
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = WhiteGray
                    )
                )},
            text = {Text(
                text = "To enable notifications, please allow the permission from app settings.",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = lexendFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = BlueDark
                )
            )}
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = iconPainter,
            contentDescription = "Notification Icon",
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))
        SimiMidDarkHeadline(text = "Notifications", size = 16)
        Spacer(modifier = Modifier.weight(1f))

        SwipeToggleSwitch(
            selectedValue = if (state) "On" else "Off",
            onValueChange = { newValue ->
                val willEnable = newValue == "On"

                if (willEnable) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                        ContextCompat.checkSelfPermission(
                            context, Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        val activity = context as? Activity

                        val shouldShowRationale = activity?.let {
                            ActivityCompat.shouldShowRequestPermissionRationale(it, Manifest.permission.POST_NOTIFICATIONS)
                        } ?: false

                        if (shouldShowRationale) {
                            // Re-ask for permission
                            ActivityCompat.requestPermissions(
                                activity!!,
                                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                                999
                            )
                        } else {
                            // ❗ User checked "Don't ask again" or denied initially: show dialog
                            showPermissionDialog = true
                        }

                        return@SwipeToggleSwitch
                    }
                }

                NotificationPreferencesManager.setNotificationState(context, willEnable)
            }
        )
    }
}

@Composable
fun SwipeToggleSwitch(selectedValue: String, onValueChange: (String) -> Unit) {
    val toggleWidth = 50.dp
    val toggleHeight = 24.dp
    val thumbWidth = toggleWidth / 2

    val density = LocalDensity.current
    val maxOffsetPx = with(density) { thumbWidth.toPx() }
    var offsetX by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(selectedValue) {
        offsetX = if (selectedValue == "On") maxOffsetPx else 0f
    }

    val backgroundColor = if (selectedValue == "Off") {
        LightGray
    } else {
        GreenLight
    }

    val animatedOffset by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = tween(250),
        label = "SwipeToggleOffset"
    )

    Box(
        modifier = Modifier
            .width(toggleWidth)
            .height(toggleHeight)
            .clip(RoundedCornerShape(30.dp))
            .background(backgroundColor)
            .border(3.dp, backgroundColor, RoundedCornerShape(30.dp))
            .pointerInput(Unit) {
                detectTapGestures { tapOffset ->
                    val newValue = if (tapOffset.x < size.width / 2f) "Off" else "On"
                    offsetX = if (newValue == "Off") 0f else maxOffsetPx
                    onValueChange(newValue)
                }
            }
    ) {
        Box(
            modifier = Modifier
                .offset { IntOffset(animatedOffset.roundToInt(), 0) }
                .width(thumbWidth)
                .fillMaxHeight()
                .clip(CircleShape)
                .background(White)
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        offsetX = (offsetX + delta).coerceIn(0f, maxOffsetPx)
                    },
                    onDragStopped = {
                        val newValue = if (offsetX < maxOffsetPx / 2) "Off" else "On"
                        offsetX = if (newValue == "Off") 0f else maxOffsetPx
                        onValueChange(newValue)
                    }
                )
        )
    }
}

fun openAppNotificationSettings(context: Context) {
    val intent = Intent().apply {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}