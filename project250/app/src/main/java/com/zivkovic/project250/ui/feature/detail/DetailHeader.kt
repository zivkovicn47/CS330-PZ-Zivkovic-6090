package com.zivkovic.project250.ui.feature.detail

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.zivkovic.project250.R
import com.zivkovic.project250.ui.components.TopBar

@Composable
fun DetailHeader(
    picUrl: String?,
    onBack: () -> Unit,
    onFav: () -> Unit,
    isFavorite: Boolean = false,
    isOwner: Boolean = false,
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showFullImage by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(color = colorResource(R.color.black))
    ) {
        AsyncImage(
            model = coil.request.ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                .data(picUrl)
                .crossfade(true)
                .error(R.drawable.car)
                .fallback(R.drawable.car)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.car),
            modifier = Modifier
                .fillMaxSize()
                .clickable { showFullImage = true }
        )

        TopBar(
            title = "", // Hide title in header
            navigationIcon = {
                androidx.compose.material3.Surface(
                    shape = androidx.compose.foundation.shape.CircleShape,
                    color = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.4f),
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { onBack() }
                ) {
                    androidx.compose.foundation.layout.Box(contentAlignment = Alignment.Center) {
                        androidx.compose.material3.Icon(
                            imageVector = androidx.compose.material.icons.Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = androidx.compose.ui.graphics.Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            },
            titleColorRes = R.color.white,
            modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal = 16.dp),
            actions = {
                if (isOwner) {
                    androidx.compose.material3.Surface(
                        shape = androidx.compose.foundation.shape.CircleShape,
                        color = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.4f),
                        modifier = Modifier.size(40.dp).clickable { onEdit() }
                    ) {
                        androidx.compose.foundation.layout.Box(contentAlignment = Alignment.Center) {
                            androidx.compose.material3.Icon(
                                imageVector = androidx.compose.material.icons.Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = androidx.compose.ui.graphics.Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    androidx.compose.material3.Surface(
                        shape = androidx.compose.foundation.shape.CircleShape,
                        color = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.4f),
                        modifier = Modifier.size(40.dp).clickable { onDelete() }
                    ) {
                        androidx.compose.foundation.layout.Box(contentAlignment = Alignment.Center) {
                            androidx.compose.material3.Icon(
                                imageVector = androidx.compose.material.icons.Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = androidx.compose.ui.graphics.Color.Red,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                androidx.compose.material3.Surface(
                    shape = androidx.compose.foundation.shape.CircleShape,
                    color = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.4f),
                    modifier = Modifier.size(40.dp).clickable { onFav() }
                ) {
                    androidx.compose.foundation.layout.Box(contentAlignment = Alignment.Center) {
                        androidx.compose.material3.Icon(
                            imageVector = if (isFavorite) androidx.compose.material.icons.Icons.Filled.Favorite else androidx.compose.material.icons.Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) androidx.compose.ui.graphics.Color.Red else androidx.compose.ui.graphics.Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        )
    }

    if (showFullImage) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = { showFullImage = false },
            properties = androidx.compose.ui.window.DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = coil.request.ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                        .data(picUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Full Screen Car",
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    contentScale = ContentScale.FillWidth
                )
                // Close Button
                androidx.compose.material3.IconButton(
                    onClick = { showFullImage = false },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 48.dp, end = 16.dp)
                        .background(androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.5f), androidx.compose.foundation.shape.CircleShape)
                ) {
                     androidx.compose.material3.Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.Close,
                        contentDescription = "Close",
                        tint = androidx.compose.ui.graphics.Color.White
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun DetailHeaderPreview() {
    DetailHeader(
        picUrl = "https://res.cloudinary.com/dhzuuct8o/image/upload/v1769125101/sedan_b8sfwq.png",
        onBack = {},
        onFav = {}
    )
}
