package com.zivkovic.project250.ui.feature.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.zivkovic.project250.R

@Composable
fun HeaderSection(
    username: String = "Guest",
    profileImageUrl: String? = null,
    onProfileClick: () -> Unit = {},
    onBellClick: () -> Unit = {}
) {
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .statusBarsPadding()
        .padding(16.dp)) {
        val(profilePic, nameColumn, bellIcon) = createRefs()

        Box(
            modifier = Modifier
                .size(55.dp)
                .clip(androidx.compose.foundation.shape.CircleShape)
                .clickable { onProfileClick() }
                .constrainAs(profilePic) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            if (!profileImageUrl.isNullOrEmpty()) {
                coil.compose.AsyncImage(
                    model = coil.request.ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                        .data(profileImageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = null,
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Column(modifier = Modifier.constrainAs(nameColumn) {
            start.linkTo(profilePic.end, margin = 8.dp)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)

        }) {
            Text(text = "Profile", fontSize = 14.sp)
            Text(text = username, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Icon(
            imageVector = androidx.compose.material.icons.Icons.Default.Notifications,
            contentDescription = null,
            modifier = Modifier
                .clickable { onBellClick() }
                .constrainAs(bellIcon) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )
    }
}