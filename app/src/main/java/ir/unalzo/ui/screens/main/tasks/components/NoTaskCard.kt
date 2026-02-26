package ir.unalzo.ui.screens.main.tasks.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.unalzo.ui.theme.Blue100
import ir.unalzo.ui.theme.Pink500
import ir.unalzo.ui.theme.Purple500

@Composable
fun NoTaskCard() {
    Box(
        Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Blue100)
            .padding(22.dp)
        ,
        contentAlignment = Alignment.Center
    ) {
        Text("یک کار برای انجام تعریف کنید :)", color = Purple500, fontSize = 18.sp)
    }
}