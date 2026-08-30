package com.zivkovic.project250.ui.components

import android.graphics.fonts.Font
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.zivkovic.project250.R

@Composable
fun TopBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    trailingIcon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    onTrailingClick: (() -> Unit)? = null,
    titleColorRes: Int = R.color.black,
    trailingIconTint: androidx.compose.ui.graphics.Color? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp)
        ) {
            navigationIcon()
        }

        Text(
            text = title,
            color = colorResource(titleColorRes),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )

        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (trailingIcon != null) {
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = null,
                    modifier = Modifier.clickable { onTrailingClick?.invoke() },
                    tint = trailingIconTint ?: androidx.compose.ui.graphics.Color.Black
                )
            }
            actions()
        }
    }
}