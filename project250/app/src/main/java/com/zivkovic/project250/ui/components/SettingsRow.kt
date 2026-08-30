package com.zivkovic.project250.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zivkovic.project250.R

@Composable
fun SettingsRow(
    icon: Int,
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(height = 60.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(size = 50.dp))
            .background(color = colorResource(id = R.color.gray))
            .clickable { onClick() }
            .padding(horizontal = 16.dp)
    ) {
        Image(painterResource(icon), contentDescription = null)
        Spacer(Modifier.width(16.dp))
        Text(
            title, color = colorResource(R.color.black),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}