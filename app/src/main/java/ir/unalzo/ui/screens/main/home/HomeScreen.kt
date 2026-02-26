package ir.unalzo.ui.screens.main.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ir.unalzo.R
import ir.unalzo.base.collectViewModelState
import ir.unalzo.ui.components.RoundedButton
import ir.unalzo.ui.screens.main.tasks.components.TaskList
import ir.unalzo.ui.theme.Prev
import ir.unalzo.ui.theme.screenPadding

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel()
) {
    val state by viewModel.collectViewModelState()
    HomeContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun HomeContent(
    state: HomeState,
    onEvent:(HomeEvent)-> Unit
) {
    Column(
        modifier=Modifier
            .fillMaxSize()
            .padding(screenPadding)
    ) {
        Text(
            text = buildAnnotatedString {
                append("سلام")
                if(state.name.isNotBlank()){
                    append(" ${state.name}")
                }
                append("!")
            },
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(22.dp))
        HomeBanner()
        Spacer(Modifier.height(22.dp))
        RoundedButton(
            label = "کار های من",
            containerColor = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp),
            onClick = {},
            textColor = Color.Black
        )
        Spacer(Modifier.height(18.dp))
        TaskList(
            tasks = state.undoneTasks,
            onToggle = {
                onEvent(HomeEvent.ToggleTask(it))
            }
        )
    }
}

@Composable
private fun HomeBanner() {
    Image(
        painter = painterResource(R.drawable.home_banner),
        contentScale = ContentScale.Crop,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
    )
}

@Preview
@Composable
private fun HomePreview() {
    Prev {
        HomeContent(
            state = HomeState(
                name = "احسان",
            ),
            onEvent = {

            }
        )
    }
}