package uk.co.josh9595.vroomswidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate

class MainActivity : ComponentActivity() {

    private val formulaFamily = FontFamily(
        Font(R.font.formula_regular, weight = FontWeight.Normal),
        Font(R.font.formula_bold, weight = FontWeight.Bold)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val widgetManager = AppWidgetManager.getInstance(this)
        val widgetProviders = widgetManager.getInstalledProvidersForPackage(packageName, null)

        setContent {
            val colors = if (isSystemInDarkTheme()) {
                darkColors()
            } else {
                lightColors()
            }

            MaterialTheme(colors) {
                Column (horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Vrooms Widget",
                        fontFamily = formulaFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 64.sp,
                        modifier = Modifier.fillMaxWidth().padding(16.dp, 32.dp, 16.dp, 0.dp),
                        style = TextStyle(
                            color = MaterialTheme.colors.onBackground
                        )
                    )
                    Text(
                        text = "To keep up to date with when every race of the ${LocalDate.now().year} season is happening, tap below to add the Vrooms widget to your home screen.",
                        fontFamily = formulaFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp,
                        modifier = Modifier.fillMaxWidth().padding(16.dp, 32.dp),
                        style = TextStyle(
                            color = MaterialTheme.colors.onBackground,
                            lineHeight = 26.sp
                        )
                    )
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = MaterialTheme.colors.onBackground,
                        modifier = Modifier.size(50.dp).padding(bottom = 16.dp)
                    )
                    WidgetInfoCard(providerInfo = widgetProviders[0])
                }

            }
        }
    }

    @Composable
    private fun WidgetInfoCard(providerInfo: AppWidgetProviderInfo) {
        val context = LocalContext.current
        val label = providerInfo.loadLabel(context.packageManager)
        val preview = painterResource(id = providerInfo.previewImage)

        Image(
            painter = preview,
            contentDescription = label,
            modifier = Modifier.clickable { providerInfo.pin(context) }
        )
    }

    private fun AppWidgetProviderInfo.pin(context: Context) {
        val successCallback = PendingIntent.getBroadcast(
            context,
            0,
            Intent(context, AppWidgetPinnedReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        AppWidgetManager.getInstance(context).requestPinAppWidget(provider, null, successCallback)
    }
}
