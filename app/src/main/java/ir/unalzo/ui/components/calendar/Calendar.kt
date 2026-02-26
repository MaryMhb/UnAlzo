package ir.unalzo.ui.components.calendar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.aminography.primecalendar.persian.PersianCalendar
import ir.unalzo.ui.theme.Blue500
import ir.unalzo.ui.theme.Prev
import ir.unalzo.ui.theme.UnAlzoTheme
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

@Composable
fun Calendar(
    initialSelectedDay: Day? = null,
    onDismiss: () -> Unit,
    onConfirm: (Day) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        UnAlzoTheme {

            val today = remember {
                PersianCalendar()
            }
            val currentYear = remember {
                mutableStateOf(1404)
            }
            val selectedDay = remember {
                mutableStateOf(
                    initialSelectedDay ?: getToday()
                )
            }
            val scope = rememberCoroutineScope()

            val weekDays = listOf(
                "ش",
                "ی",
                "د",
                "س",
                "چ",
                "پ",
                "ج",
            )
            val pagerState = rememberPagerState(initialPage = selectedDay.value.month) { 12 }
            val currentMonthName = remember(pagerState.currentPage) {
                PersianCalendar()
                    .apply {
                        set(year, pagerState.currentPage, 1)
                    }
                    .getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()).orEmpty()
            }

            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(12.dp),
//                    clip = true,
//                    spotColor = Color.Red,
//                    ambientColor = Color.Red
                    )
                    .background(Color.White)
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = {
                            if (pagerState.canScrollBackward) {
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                            contentDescription = null
                        )
                    }
                    Text(
                        text = buildAnnotatedString {
                            append(currentMonthName)
                            append(" ")
                            append(currentYear.value.toString())
                        },
                        fontWeight = FontWeight.Medium
                    )
                    IconButton(
                        onClick = {
                            if (pagerState.canScrollForward) {
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                            contentDescription = null
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
                Row(modifier = Modifier.padding(horizontal = 14.dp)) {
                    weekDays.forEach {
                        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            Text(
                                text = it.first().toString(),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .animateContentSize()
                        .padding(horizontal = 14.dp)
                ) { pageMonth ->
                    val monthCalendar = remember {
                        PersianCalendar()
                            .apply {
                                set(currentYear.value, pageMonth, 1)
                            }
                    }
                    val dayCount = getMonthLength(currentYear.value, pageMonth)
                    val previousMonthDayCount = run {
                        if (pageMonth == 0) {
                            null
                        } else {
                            getMonthLength(currentYear.value, pageMonth - 1)
                        }
                    }
                    val dayStartFromWeekIndex = weekDays.indexOf(
                        monthCalendar.getDisplayName(
                            Calendar.DAY_OF_WEEK, Calendar.LONG,
                            Locale.getDefault()
                        ).orEmpty().take(1)
                    ).coerceAtLeast(0)
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(7),
                    ) {
                        items(dayStartFromWeekIndex) {
                            if (previousMonthDayCount != null) {
                                val day = Day(
                                    year = currentYear.value,
                                    month = pageMonth - 1,
                                    day = (previousMonthDayCount - (dayStartFromWeekIndex - (it + 1)))
                                )
                                DayItem(
                                    day = day,
                                    state = DayState.NotForThisMonth
                                )
                            }
                        }
                        items(dayCount) {
                            val day = Day(
                                year = currentYear.value,
                                month = pageMonth,
                                day = it + 1
                            )
                            val state = when {
                                day.isToday() -> DayState.Today
                                day == selectedDay.value -> DayState.Selected
                                day.isClosed() -> DayState.Closed
                                else -> DayState.Normal
                            }
                            DayItem(
                                day = day,
                                state = state,
                                onClick = {
                                    selectedDay.value = day
                                }
                            )
                        }
                        val restDaysOfRow = 7 - (dayCount + dayStartFromWeekIndex) % 7
                        if (restDaysOfRow < 7) {
                            items(7 - (dayCount + dayStartFromWeekIndex) % 7) {
                                val day = Day(
                                    year = currentYear.value,
                                    month = pageMonth + 1,
                                    day = it + 1
                                )
                                DayItem(
                                    day = day,
                                    state = DayState.NotForThisMonth
                                )
                            }
                        }

                    }
                }
                Spacer(Modifier.height(18.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val textButtonColors = ButtonDefaults.textButtonColors(
                        contentColor = Blue500
                    )
                    TextButton(
                        onClick = {
                            selectedDay.value = getToday()
                            scope.launch {
                                pagerState.animateScrollToPage(getToday().month)
                            }
                        },
                        colors = textButtonColors
                    ) {

                        Text("امروز", fontWeight = FontWeight.Medium)
                    }
                    Spacer(Modifier.weight(1f))
                    TextButton(
                        onClick = {
                            onDismiss()
                        },
                        colors = textButtonColors
                    ) {
                        Text("انصراف", fontWeight = FontWeight.Medium)
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onConfirm(
                                selectedDay.value.copy(
                                    month = selectedDay.value.month + 1
                                )
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Blue500
                        ),
                        shape = RoundedCornerShape(5.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        modifier = Modifier.height(38.dp)
                    ) {
                        Text("انتخاب", fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

@Composable
private fun LazyGridItemScope.DayItem(
    day: Day,
    state: DayState,
    onClick: () -> Unit = {}
) {
    val backgroundColor: Color
    var borderColor: Color = Color.Transparent
    var textColor = Color.Black
    when (state) {
        DayState.Selected -> {
            backgroundColor = Color.Transparent
            borderColor = Blue500
            textColor = Blue500
        }

        DayState.NotForThisMonth -> {
            backgroundColor = Color.Transparent
            textColor = Color.Black.copy(.4f)
        }

        DayState.Closed -> {
            backgroundColor = Color(0xffD0D4FF)
            textColor = Blue500
        }

        DayState.Today -> {
            backgroundColor = Blue500
            textColor = Color.White
        }

        DayState.Normal -> {
            backgroundColor = Color(0xffF5F5F5)
        }
    }
    val shape = RoundedCornerShape(4.dp)
    Box(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(shape)
            .background(animateColorAsState(backgroundColor).value)
            .border(
                width = 1.dp,
                shape = shape,
                color = animateColorAsState(borderColor).value
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.day.toString(),
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = animateColorAsState(textColor).value
        )
    }
}

enum class DayState {
    Selected,
    Closed,
    Today,
    Normal,
    NotForThisMonth
}

@Preview
@Composable
private fun CalendarPrev() {
    Prev {
        Calendar(
            onDismiss = {

            },
            onConfirm = {

            }
        )
    }
}