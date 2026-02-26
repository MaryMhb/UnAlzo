package ir.unalzo.ui.screens.main.tasks.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.unalzo.R
import ir.unalzo.database.entities.TaskEntity
import ir.unalzo.ui.components.AppRadioButton
import ir.unalzo.ui.theme.Blue500
import ir.unalzo.ui.theme.Pink500
import ir.unalzo.ui.theme.Prev
import kotlin.time.Duration.Companion.hours

@Composable
fun ColumnScope.TaskList(
    tasks: List<TaskEntity>?,
    showNoTaskCard: Boolean = true,
    onToggle: (TaskEntity) -> Unit
) {
    AnimatedContent(
        targetState = tasks,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        }
    ) { tasks ->
        when {
            tasks == null -> Box(
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Blue500
                )
            }

            tasks.isEmpty() && showNoTaskCard -> NoTaskCard()
            else -> LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(tasks.reversed(), key = { it.id }) { task ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(
                                fadeInSpec = tween(500),
                                fadeOutSpec = tween(500),
                                placementSpec = tween(500)
                            )
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)
                            .clickable {
                                onToggle(task)
                            }
                            .padding(
                                horizontal = 16.dp,
                                vertical = 22.dp
                            ),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AppRadioButton(
                            checked = task.isDone
                        )
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = task.title,
                                    modifier = Modifier
                                        .weight(1f)
                                        .drawWithContent {
                                            if (task.isDone) {
                                                drawLine(
                                                    color = Color.Black,
                                                    start = Offset(
                                                        x = 0f,
                                                        y = size.height / 2
                                                    ),
                                                    end = Offset(
                                                        x = size.width,
                                                        y = size.height / 2
                                                    ),
                                                    strokeWidth = 2.dp.toPx()
                                                )
                                            }
                                            drawContent()
                                        },
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                if (task.reminderTime != null) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_ring),
                                        contentDescription = null,
                                        modifier = Modifier.size(25.dp),
                                        tint = Pink500
                                    )
                                }
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = task.description,
                                    modifier = Modifier.weight(1f),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal
                                )
                                task.reminderTimeString?.let { time ->
                                    Text(
                                        text = time,
                                        fontSize = 14.sp,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun TaskListPreview() {
    Prev(contentPadding = 12.dp) {
        Column {
            TaskList(
                tasks = getFakeTasks()
            ) { }
        }
    }
}

fun getFakeTasks(count: Int = 3): List<TaskEntity> {
    return MutableList(count) {
        TaskEntity(
            id = 1,
            title = "عنوان تسک",
            description = "محتویات تسک",
            isDone = it % 2 == 0,
            reminderTime = System.currentTimeMillis() + 1.hours.inWholeMilliseconds
        )
    }
}

@Preview
@Composable
private fun NoTaskPreview() {
    Prev(contentPadding = 12.dp) {
        Column {
            TaskList(
                tasks = emptyList()
            ) { }
        }
    }
}