package ir.unalzo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onFirstVisible
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppInput(
    modifier: Modifier = Modifier,
    value: String,
    focusAtStart: Boolean = false,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    val focusRequester = remember {
        FocusRequester()
    }

    Box(
        modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(horizontal = 18.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            decorationBox = {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = Color(0xff94A3B8),
                        fontSize = 20.sp
                    )
                }
                it()
            },
            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester)
                .onFirstVisible {
                    if(focusAtStart){
                        focusRequester.requestFocus()
                    }
                }
            ,
            textStyle = LocalTextStyle.current.copy(
                fontSize = 20.sp
            )
        )
    }
}