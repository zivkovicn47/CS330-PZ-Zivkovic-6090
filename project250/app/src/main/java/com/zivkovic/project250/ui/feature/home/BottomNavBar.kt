package com.zivkovic.project250.ui.feature.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Add


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.pm.ShortcutInfoCompat
import com.zivkovic.project250.R

@Composable
fun BottomNavBar(
    onHomeClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onAddClick: () -> Unit = {},
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color.Transparent,
        shape = RoundedCornerShape(50.dp),
        shadowElevation = 8.dp
    ) {
        BottomAppBar(
            containerColor = Color.Black,
            contentPadding = PaddingValues(horizontal = 16.dp),
            tonalElevation = 0.dp
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = onHomeClick) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Filled.Home,
                            contentDescription = "Home",
                            tint = Color.White
                        )
                    }
                }
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = onFavoriteClick) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Favorite,
                            contentDescription = "Favorites",
                            tint = Color.White
                        )
                    }
                }
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = onAddClick) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Add,
                            contentDescription = "Add",
                            tint = Color.White
                        )
                    }
                }
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = onProfileClick) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun BottomNavBarPreview() {
    BottomNavBar(onProfileClick = {})
}