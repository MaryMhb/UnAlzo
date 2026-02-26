package ir.unalzo.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import ir.unalzo.ui.components.calendar.getCurrentTime
import ir.unalzo.ui.theme.Blue100
import ir.unalzo.ui.theme.Blue500
import ir.unalzo.ui.theme.Prev
import ir.unalzo.ui.theme.UnAlzoTheme
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

data class Time(
    val hour: Int,
    val minutes: Int
) {
    override fun toString(): String {
        return "%02d:%02d".format(this.hour, this.minutes)
    }

    fun toTimeMills(): Long {
        return (hour.hours.inWholeMilliseconds) + (minutes.minutes.inWholeMilliseconds)
    }
}

@Composable
fun TimePicker(
    initialTime: Time? = null,
    onDismiss: () -> Unit,
    onConfirm: (Time) -> Unit
) {
    val time = remember {
        mutableStateOf(
            initialTime ?: getCurrentTime()
        )
    }
    Dialog(
        onDismissRequest = onDismiss
    ) {
        UnAlzoTheme {
            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Blue100)
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TimeColumn(
                        value = time.value.minutes,
                        max = 59,
                        onValueChange = {
                            time.value = time.value.copy(
                                minutes = it
                            )
                        },
                    )
                    Text(":", color = Blue500, fontSize = 50.sp)
                    TimeColumn(
                        value = time.value.hour,
                        max = 23,
                        onValueChange = {
                            time.value = time.value.copy(
                                hour = it
                            )
                        },
                    )
                }
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(22.dp)
                ) {
                    TimePickerAction(
                        label = "تایید",
                        onClick = {
                            onConfirm(time.value)
                        }
                    )
                    TimePickerAction(label = "انصراف", onClick = onDismiss)
                }
            }
        }
    }
}

@Composable
fun RowScope.TimePickerAction(
    label: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Blue500
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.weight(1f),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(text = label, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun TimeColumn(
    value: Int,
    max: Int,
    onValueChange: (Int) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ArrowButton(
            icon = Icons.Rounded.KeyboardArrowUp,
            onClick = {
                if (value < max) {
                    onValueChange(value + 1)
                } else {
                    onValueChange(0)
                }
            }
        )
        val shape = RoundedCornerShape(8.dp)
        Box(
            Modifier
                .size(80.dp, 80.dp)
                .clip(shape)
                .background(Color.White)
                .border(
                    width = 2.dp,
                    shape = shape,
                    color = Blue500
                ),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(value, transitionSpec = {
                if (targetState > initialState) {
                    slideInVertically { height -> height } + fadeIn() togetherWith
                            slideOutVertically { height -> -height } + fadeOut()
                } else {
                    slideInVertically { height -> -height } + fadeIn() togetherWith
                            slideOutVertically { height -> height } + fadeOut()
                }.using(
                    SizeTransform(clip = false)
                )
            }) {
                Text(
                    text = String.format("%02d", it),
                    fontWeight = FontWeight.Bold,
                    color = Blue500,
                    fontSize = 50.sp,
                    lineHeight = 1.sp,
                    modifier = Modifier.offset(y = 4.dp)
                )
            }
        }
        ArrowButton(
            icon = Icons.Rounded.KeyboardArrowDown,
            onClick = {
                if (value > 0) {
                    onValueChange(value - 1)
                } else {
                    onValueChange(max)
                }
            }
        )
    }
}

@Composable
private fun ArrowButton(
    icon: ImageVector,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(6.dp)
    Box(
        Modifier
            .clip(shape)
            .border(
                width = 1.5.dp,
                shape = shape,
                color = Blue500
            )
            .clickable {
                onClick()
            }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Blue500,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview
@Composable
private fun TimerPickerPreview() {
    Prev() {
        TimePicker(
            onDismiss = {

            },
            onConfirm = {

            }
        )
    }
}