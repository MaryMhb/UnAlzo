package ir.unalzo.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.unalzo.R
import ir.unalzo.navigation.navigator.RootNavigator
import ir.unalzo.navigation.routes.RootRoute
import ir.unalzo.ui.theme.Blue300
import ir.unalzo.ui.theme.Blue500
import ir.unalzo.ui.theme.Orange300
import ir.unalzo.ui.theme.Orange500
import ir.unalzo.ui.theme.Pink300
import ir.unalzo.ui.theme.Pink500
import ir.unalzo.ui.theme.Prev
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

private data class IntroItem(
    val title: String,
    val description: String,
    val cardColors: List<Color>,
    val image: Int
)

@Composable
fun IntroScreen(
    navigator: RootNavigator = koinInject()
) {
    val items = listOf(
        IntroItem(
            title = "سبک زندگیت رو تغییر بده!",
            description = "آنالزو کمک میکنه تا زندگی برای شما آسان\u200Cتر بشه",
            image = R.drawable.img_intro_1,
            cardColors = listOf(
                Pink300, Pink500, Pink300
            )
        ),
        IntroItem(
            title = "کلید پیشرفت، استمراره!",
            description = " برای بهبود عملکرد مغز، هر روز با آنالزو بازی\u200Cهای جذاب انجام بده!",
            image = R.drawable.img_intro_2,
            cardColors = listOf(
                Blue300, Blue500, Blue300
            )
        ),
        IntroItem(
            title = "دستیار شخصی، هرلحظه در کنار شما",
            description = "یادآور 24 ساعته برای هرچیزی که ممکنه از قلم بیفته",
            image = R.drawable.img_intro_3,
            cardColors = listOf(
                Orange500, Orange300, Orange500
            )
        ),

        )
    val pagerState = rememberPagerState {
        items.count()
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .systemBarsPadding()
        .padding(22.dp)) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            pageSpacing = 12.dp,
            reverseLayout = true
        ) {
            val item = items[it]
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 48.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Brush.horizontalGradient(item.cardColors))
                        .padding(horizontal = 22.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.title,
                        fontSize = 25.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        lineHeight = 38.sp
                    )
                }
                Text(
                    text = item.description,
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 38.sp
                )
                Image(
                    painter = painterResource(item.image),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.FillHeight
                )
            }
        }
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        IconButton(
            onClick = {
                if(pagerState.canScrollForward){
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage+1)
                    }
                }else{
                    navigator.navigate(RootRoute.Questions)
                }
            },
            modifier = Modifier.size(50.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_next),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                tint = Blue500
            )
        }
    }
}

@Preview
@Composable
private fun IntroPreview() {
    Prev {
        IntroScreen()
    }
}