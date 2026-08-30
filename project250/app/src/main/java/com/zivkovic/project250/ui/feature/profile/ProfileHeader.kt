package com.zivkovic.project250.ui.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zivkovic.project250.R
import com.zivkovic.project250.domain.UserProfile

@Composable
fun ProfileHeader(
    user: UserProfile?,
    onBack: () -> Unit = {},
    onEditClick: () -> Unit = {}
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(color = colorResource(id = R.color.gray))
    ) {
        // Back Button
        Box(
            modifier = Modifier
                .padding(start = 16.dp, top = 48.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { onBack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Image with Edit Icon
            Box(
                contentAlignment = Alignment.BottomEnd
            ) {
                if (user?.profilePicUrl?.isNotEmpty() == true) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(user.profilePicUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(60.dp),
                            tint = Color.White
                        )
                    }
                }
                
                // Edit Icon
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable { onEditClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile",
                        modifier = Modifier.size(20.dp),
                        tint = Color.Black
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            // Name
            Text(
                text = user?.fullName ?: "Guest User",
                color = Color.Black,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            
            // Email
            if (user?.email?.isNotEmpty() == true) {
                Text(
                    text = user.email,
                    color = Color.DarkGray,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun ProfileHeaderPreview() {
    ProfileHeader(
        user = UserProfile(
            fullName = "Alex Johnson",
            phoneNumber = "1234567890",
            profilePicUrl = "",
            email = "alex@example.com"
        )
    )
}