package com.zivkovic.project250.ui.feature.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileSettings(
    isAdmin: Boolean = false,
    onEditProfileClick: () -> Unit = {},
    onContactSupportClick: () -> Unit = {},
    onManageCarsClick: () -> Unit = {}
) {
    Column {
        SettingsItem(
            icon = Icons.Default.Edit,
            title = "Edit Profile",
            onClick = onEditProfileClick
        )
        SettingsItem(
            icon = Icons.Default.Email,
            title = "Contact Support",
            onClick = onContactSupportClick
        )
        if (isAdmin) {
            SettingsItem(
                icon = Icons.Default.Menu,
                title = "Manage Cars (Admin)",
                onClick = onManageCarsClick
            )
        }
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = Color.Black
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
    }
}

@Preview
@Composable
fun ProfileSettingsPreview() {
    ProfileSettings(isAdmin = true)
}