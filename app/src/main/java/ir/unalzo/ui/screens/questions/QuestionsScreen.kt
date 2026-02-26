package ir.unalzo.ui.screens.questions

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.unalzo.navigation.navigator.RootNavigator
import ir.unalzo.navigation.routes.RootRoute
import ir.unalzo.ui.components.AppInput
import ir.unalzo.ui.components.AppRadioButton
import ir.unalzo.ui.components.RoundedButton
import ir.unalzo.ui.theme.Pink300
import ir.unalzo.ui.theme.Pink500
import ir.unalzo.ui.theme.Prev
import ir.unalzo.utils.Constants
import ir.unalzo.utils.set
import ir.unalzo.utils.sharedPrefs
import kotlinx.coroutines.launch
import org.koin.compose.koinInject


@Composable
fun QuestionsScreen(
    navigator: RootNavigator = koinInject()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val items = remember {
        listOf(
            QuestionItem.Start,
            QuestionItem.Input(
                title = "اسمت رو بهم بگو",
                placeholder = "به عنوان مثال مریم"
            ),
            QuestionItem.Option(
                title = "چند سالته؟",
                options = listOf(
                    "بین 30 تا 40",
                    "بین 45 تا 60",
                    "بین 60 تا 75",
                    "بالای 75",
                )
            ),
            QuestionItem.Option(
                title = "چند ساله که به آلزایمر مبتلا هستی؟",
                options = listOf(
                    "زیر یک سال",
                    "بین 1 تا 3 سال",
                    "بالای 3 سال",
                    "مبتلا نیستم!",
                )
            ),
        )
    }

    val localKeyboard = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(22.dp)
            .imePadding()
    ) {
        val pagerState = rememberPagerState { items.count() }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier.weight(.4f)
            )

            HorizontalPager(
                state = pagerState,
                pageSpacing = 14.dp,
                reverseLayout = true,
                modifier = Modifier.weight(1f)
            ) { page ->
                Box(Modifier.fillMaxSize()) {
                    QuestionItem(item = items[page])
                }
            }

            val currentItem = items[pagerState.currentPage]

            RoundedButton(
                label = when {
                    currentItem is QuestionItem.Start -> "موافقم"
                    pagerState.currentPage == items.lastIndex -> "بزن بریم"
                    else -> "بعدی"
                },
                containerColor = Pink500,
                onClick = {
                    if (
                        currentItem is QuestionItem.Answerable &&
                        (currentItem.answer.value.isBlank() && currentItem.optional.not())
                    ) {
                        Toast.makeText(
                            context,
                            "لطفا جواب را وارد کنید.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        if (pagerState.canScrollForward) {
                            scope.launch {
                                localKeyboard?.hide()
                                pagerState.animateScrollToPage(
                                    pagerState.currentPage + 1
                                )
                            }
                        } else {
                            context.sharedPrefs[Constants.Prefs.INTRO_PASSED] = true
                            items.filterIsInstance<QuestionItem.Input>()
                                .first()
                                .also {
                                    context.sharedPrefs[Constants.Prefs.NAME] =
                                        it.answer.value
                                }
                            navigator.navigateAndClear(RootRoute.Main)
                        }
                    }
                },
                modifier = Modifier
                    .animateContentSize()
                    .padding(bottom = 64.dp),
                textColor = Color.White
            )
        }
    }

}

@Composable
private fun QuestionItem(
    item: QuestionItem,
) {
    when (item) {
        is QuestionItem.Input -> {
            Column {
                Text(
                    text = item.title,
                    fontSize = 20.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                AppInput(
                    value = item.answer.value,
                    onValueChange = {
                        item.answer.value = it
                    },
                    placeholder = item.placeholder,
                    focusAtStart = true
                )
            }
        }

        is QuestionItem.Option -> {
            Column {
                Text(
                    text = item.title,
                    fontSize = 20.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item.options.forEach {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White)
                                .clickable {
                                    item.answer.value = it
                                }
                                .padding(horizontal = 12.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            AppRadioButton(
                                checked = it == item.answer.value
                            )
                            Text(
                                text = it,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        QuestionItem.Start -> {
            Column {
                Spacer(Modifier)
                Column {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Pink300)
                            .padding(horizontal = 22.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "لطفا برای شروع به چند سوال کوتاه درباره خودت پاسخ بده",
                            fontSize = 25.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            lineHeight = 38.sp
                        )
                    }
                    Spacer(Modifier.height(22.dp))
                    Text(
                        "اگر پاسخ رو نمیدونستی اصلا نگران نباش، مشکلی ایجاد نمیشه",
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        lineHeight = 30.sp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun QuestionsPreview() {
    Prev {
        QuestionsScreen()
    }
}