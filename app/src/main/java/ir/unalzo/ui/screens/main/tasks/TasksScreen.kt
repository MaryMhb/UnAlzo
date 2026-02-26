package ir.unalzo.ui.screens.main.tasks

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ir.unalzo.R
import ir.unalzo.navigation.navigator.RootNavigator
import ir.unalzo.navigation.routes.RootRoute
import ir.unalzo.ui.components.RoundedButton
import ir.unalzo.ui.screens.main.tasks.components.TaskList
import ir.unalzo.ui.screens.main.tasks.components.getFakeTasks
import ir.unalzo.ui.theme.Blue100
import ir.unalzo.ui.theme.Blue500
import ir.unalzo.ui.theme.Prev
import ir.unalzo.ui.theme.screenPadding
import org.koin.compose.koinInject

@Composable
fun TasksScreen(
    viewModel: TasksViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    TasksContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun TasksContent(
    state: TasksState,
    navigator: RootNavigator = koinInject(),
    onEvent: (TasksEvent) -> Unit
) {
    val showDone = remember {
        mutableStateOf(false)
    }
    Box(modifier = Modifier.padding(screenPadding)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                RoundedButton(
                    label = "کار های من",
                    modifier = Modifier.weight(1f),
                    containerColor = animateColorAsState(
                        targetValue = if (showDone.value) Color.White else Blue100
                    ).value,
                    onClick = {
                        showDone.value = false
                    },
                    contentPadding = PaddingValues(
                        vertical = 12.dp
                    ),
                    textColor = Color.Black,
                    fontSize = 16.sp
                )
                RoundedButton(
                    label = "انجام شده",
                    modifier = Modifier.weight(1f),
                    containerColor = animateColorAsState(
                        targetValue = if (showDone.value.not()) Color.White else Blue100
                    ).value,
                    onClick = {
                        showDone.value = true
                    },
                    contentPadding = PaddingValues(
                        vertical = 12.dp
                    ),
                    textColor = Color.Black,
                    fontSize = 16.sp
                )
            }
            Spacer(Modifier.height(22.dp))
            TaskList(
                tasks = state.tasks?.filter {
                    it.isDone == showDone.value
                },
                showNoTaskCard = !(showDone.value),
                onToggle = {
                    onEvent(TasksEvent.ToggleTask(it))
                }
            )
        }
        IconButton(
            onClick = {
                navigator.navigate(RootRoute.NewTask)
            },
            modifier = Modifier
                .padding(8.dp)
                .size(70.dp)
                .align(Alignment.BottomStart)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_add),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                tint = Blue500
            )
        }
    }
}

@Preview
@Composable
private fun TasksPreview() {
    Prev() {
        TasksContent(
            state = TasksState(
                tasks = getFakeTasks()
            )
        ) { }
    }
}