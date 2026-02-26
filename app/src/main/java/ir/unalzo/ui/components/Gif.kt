package ir.unalzo.ui.components

import android.os.Build.VERSION.SDK_INT
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder

@Composable
fun Gif(
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    gif: Int,
) {
    val context = LocalContext.current
    AsyncImage(
        model = gif,
        contentDescription = null,
        modifier = modifier,
        imageLoader = remember {
            ImageLoader.Builder(context)
                .components {
                    if (SDK_INT >= 28) {
                        add(AnimatedImageDecoder.Factory())
                    } else {
                        add(GifDecoder.Factory())
                    }
                }
                .build()
        },
        contentScale = contentScale
    )
}