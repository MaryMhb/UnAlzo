package ir.unalzo.ui.screens.newTask

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aminography.primecalendar.persian.PersianCalendar
import ir.unalzo.R
import ir.unalzo.base.collectViewModelEffects
import ir.unalzo.base.collectViewModelState
import ir.unalzo.createNotificationChannel
import ir.unalzo.database.entities.TaskEntity
import ir.unalzo.navigation.navigator.RootNavigator
import ir.unalzo.ui.components.Time
import ir.unalzo.ui.components.TimePicker
import ir.unalzo.ui.components.calendar.Calendar
import ir.unalzo.ui.components.calendar.Day
import ir.unalzo.ui.theme.Blue500
import ir.unalzo.ui.theme.Prev
import ir.unalzo.ui.theme.Purple500
import ir.unalzo.ui.theme.screenPadding
import ir.unalzo.utils.canScheduleExactAlarms
import ir.unalzo.utils.requestExactAlarmPermission
import org.koin.compose.koinInject
import java.util.Calendar

@Composable
fun NewTaskScreen(
    viewModel: NewTaskViewModel = viewModel(),
    navigator: RootNavigator = koinInject()
) {
    val context = LocalContext.current
    viewModel.collectViewModelEffects {
        when (it) {
            NewTaskEffect.TaskAdded -> navigator.navigateUp()
        }
    }
    val state by viewModel.collectViewModelState()
    NewTaskContent(
        onEvent = viewModel::onEvent,
        state = state
    )

}

@Composable
private fun NewTaskContent(
    state: NewTaskState,
    onEvent: (NewTaskEvent) -> Unit,
    navigator: RootNavigator = koinInject()
) {
    val context = LocalContext.current
    val latestTask = remember {
        mutableStateOf(null as TaskEntity?)
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            createNotificationChannel(context)
            onEvent(NewTaskEvent.SubmitTask(latestTask.value!!))
        }
    }
    val title = remember {
        mutableStateOf("")
    }
    val description = remember {
        mutableStateOf("")
    }
    val date = remember {
        mutableStateOf(null as Day?)
    }
    val time = remember {
        mutableStateOf(null as Time?)
    }

    val showCalendar = remember {
        mutableStateOf(false)
    }
    val showTimePicker = remember {
        mutableStateOf(false)
    }
    val bgBlur = animateDpAsState(
        if (showCalendar.value || showTimePicker.value) 8.dp else 0.dp,
        animationSpec = tween(500)
    )
    if (showCalendar.value) {
        Calendar(
            onDismiss = {
                showCalendar.value = false
            },
            onConfirm = {
                date.value = it
                showCalendar.value = false
            },
            initialSelectedDay = date.value?.copy(
                month = date.value!!.month - 1
            )
        )
    }
    if (showTimePicker.value) {
        TimePicker(
            initialTime = time.value,
            onDismiss = {
                showTimePicker.value = false
            },
            onConfirm = {
                showTimePicker.value = false
                time.value = it
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .blur(bgBlur.value)
            .padding(screenPadding)
            .systemBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            IconButton(
                onClick = navigator::navigateUp,
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.End)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Blue500,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Text("یادآوری جدید", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(22.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                NewTaskInput(
                    value = title.value,
                    onValueChange = {
                        title.value = it
                        onEvent(NewTaskEvent.RemoveTitleError)
                    },
                    placeholder = "یک کار جدید تعریف کنید",
                    error = "عنوان اجباری است".takeIf {
                        state.titleFieldError
                    }
                )
                NewTaskInput(
                    value = date.value?.toString().orEmpty(),
                    title = "تاریخ",
                    icon = R.drawable.ic_calendar_edit,
                    placeholder = "بدون تاریخ",
                    onClick = {
                        showCalendar.value = true
                    },
                    onReset = {
                        date.value = null
                    }
                )
                NewTaskInput(
                    value = time.value?.toString().orEmpty(),
                    title = "زمان یادآوری",
                    icon = R.drawable.ic_ring,
                    placeholder = "بدون یادآوری",
                    onClick = {
                        showTimePicker.value = true
                    },
                    onReset = {
                        date.value = null
                    }
                )
                NewTaskInput(
                    value = description.value,
                    onValueChange = {
                        description.value = it
                    },
                    placeholder = "توضیحات",
                    modifier = Modifier.heightIn(min = 120.dp)
                )
            }
        }
        Column() {
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    val date = date.value
                    val time = time.value
                    if (date != null && time == null) {
                        Toast.makeText(
                            context,
                            "لطفا زمان یادآوری را انتخاب کنید.",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }
                    val reminderTime = if (date != null || time != null) {
                        PersianCalendar().apply {
                            date?.let {
                                set(date.year, date.month - 1, date.day)
                            }
                            time?.let {
                                set(Calendar.HOUR_OF_DAY, time.hour)
                                set(Calendar.MINUTE, time.minutes)
                                set(Calendar.SECOND, 1)
                            }

                        }.toCivil().timeInMillis
                    } else {
                        null
                    }
                    val taskEntity = TaskEntity(
                        title = title.value,
                        description = description.value,
                        reminderTime = reminderTime,
                    )

                    if (taskEntity.shouldNotify) {
                        if (context.canScheduleExactAlarms().not()) {
                            context.requestExactAlarmPermission()
                        } else {
                            latestTask.value = TaskEntity(
                                title = title.value,
                                description = description.value,
                                reminderTime = reminderTime,
                            )
                            permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                        }
                    } else {
                        onEvent(NewTaskEvent.SubmitTask(taskEntity))
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 64.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue500
                ),
                shape = RoundedCornerShape(
                    10.dp
                )
            ) {
                Text("ایجاد", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
        }
    }
}

@Composable
fun NewTaskInput(
    value: String,
    title: String,
    icon: Int,
    placeholder: String,
    onReset: () -> Unit,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(8.dp)
    Row(
        Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(shape)
            .border(
                width = 2.dp,
                color = Blue500,
                shape = shape
            )
            .clickable {
                onClick()
            }
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(35.dp),
            tint = Color.Black
        )
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Spacer(Modifier.weight(1f))
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            AnimatedVisibility(
                visible = value.isNotBlank()
            ) {
                Button(
                    onClick = onReset,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Purple500
                    ),
                    contentPadding = PaddingValues(),
                    modifier = Modifier.size(35.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
            Button(
                onClick = onClick,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple500
                ),
                contentPadding = PaddingValues(
                    horizontal = 8.dp,
                    vertical = 0.dp
                ),
                modifier = Modifier.height(35.dp)
            ) {
                Text(
                    text = value.takeIf { it.isNotBlank() } ?: placeholder,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

        }
    }
}

@Composable
fun NewTaskInput(
    modifier: Modifier = Modifier.heightIn(min = 60.dp),
    value: String,
    error: String? = null,
    onValueChange: (String) -> Unit,
    placeholder: String,
) {
    val shape = RoundedCornerShape(8.dp)
    val isFocused = remember {
        mutableStateOf(false)
    }
    Box(
        modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .clip(shape)
            .border(
                width = 2.dp,
                color = Blue500,
                shape = shape
            )
            .padding(horizontal = 14.dp, vertical = 14.dp)
            .onFocusChanged {
                isFocused.value = it.isFocused
            },
        contentAlignment = Alignment.TopStart
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            decorationBox = {
                if (error != null) {
                    Text(
                        text = error,
                        fontSize = 18.sp,
                        color = Color(0xfff80A0a).copy(.4f),
                        fontWeight = FontWeight.Medium
                    )
                } else if (isFocused.value.not() && value.isEmpty()) {
                    Text(
                        text = placeholder,
                        fontSize = 18.sp,
                        color = Color(0xff94A3B8),
                        fontWeight = FontWeight.Medium
                    )
                }
                it()
            },
            modifier = Modifier.fillMaxSize(),
            textStyle = LocalTextStyle.current.copy(
                fontSize = 18.sp,
                color = Blue500
            )
        )
    }
}

@Preview
@Composable
private fun NewTaskPreview() {
    Prev() {
        NewTaskContent(
            onEvent = {

            },
            state = NewTaskState()
        )
    }
}