package grad.project.padelytics.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import grad.project.padelytics.R


val lexendFontFamily = FontFamily(
    Font(R.font.lexend, FontWeight.Normal),
    Font(R.font.lexend_bold, FontWeight.Bold),
    Font(R.font.lexend_medium, FontWeight.Medium),
    Font(R.font.lexend_light, FontWeight.Light),
    Font(R.font.lexend_semibold, FontWeight.SemiBold),
    Font(R.font.lexend_extra_bold, FontWeight.ExtraBold),
    Font(R.font.lexend_thin, FontWeight.Thin),
    Font(R.font.lexend_black, FontWeight.Black)
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = lexendFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)
