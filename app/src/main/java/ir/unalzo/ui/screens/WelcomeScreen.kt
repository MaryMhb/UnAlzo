package ir.unalzo.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.unalzo.R
import ir.unalzo.navigation.navigator.RootNavigator
import ir.unalzo.navigation.routes.RootRoute
import ir.unalzo.ui.components.RoundedButton
import ir.unalzo.ui.theme.Blue500
import ir.unalzo.ui.theme.Prev
import ir.unalzo.ui.theme.poppins
import ir.unalzo.utils.Constants
import ir.unalzo.utils.get
import ir.unalzo.utils.sharedPrefs
import org.koin.compose.koinInject

@Composable
fun WelcomeScreen(
    navigator: RootNavigator = koinInject()
) {
    val context = LocalContext.current
    val introPassed = remember {
        context.sharedPrefs.get<Boolean>(Constants.Prefs.INTRO_PASSED)
    }
    if (introPassed.not()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(Modifier)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.app_logo),
                    contentDescription = null,
                    modifier = Modifier.size(330.dp),
                    contentScale = ContentScale.FillBounds
                )
                Column(modifier = Modifier.offset(y = -52.dp)) {
                    Text(
                        text = stringResource(R.string.app_name),
                        fontSize = 45.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = 1.sp,
                        fontFamily = poppins
                    )
                    Text(
                        text = "یار همیشه بیدار ذهن",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            RoundedButton(
                label = "شروع",
                onClick = {
                    navigator.navigate(RootRoute.Introduction)
                },
                containerColor = Blue500,
                modifier = Modifier.padding(bottom = 90.dp),
                textColor = Color.White
            )
        }
    } else {
        LaunchedEffect(Unit) {
            navigator.navigateAndClear(RootRoute.Main)
        }
    }

}

@Preview
@Composable
private fun WelcomePreview() {
    Prev {
        WelcomeScreen()
    }
}