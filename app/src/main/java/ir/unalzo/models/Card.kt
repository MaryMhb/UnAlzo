package ir.unalzo.models

import android.content.Context
import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable

@Serializable
data class Card(
    val id: Int,
    val icon: String,
    val color: String
) {
    fun getIconResource(context: Context): Int {
        return context.resources.getIdentifier(icon, "drawable", context.packageName)
    }

    fun getComposeColor(): Color {
        return Color(android.graphics.Color.parseColor(color))
    }
}