package com.zivkovic.project250.ui.feature.profile

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.zivkovic.project250.LoginActivity
import com.zivkovic.project250.R
import com.zivkovic.project250.domain.UserProfile

@Composable
fun ProfileScreen(
    onBack: () -> Unit = {},
    onManageCarsClick: () -> Unit = {},
    onAdminManageCarsClick: () -> Unit = {}, // New callback
    onEditProfileClick: () -> Unit = {},
    onFavoritesClick: () -> Unit = {}
) {
    val scroll = rememberScrollState()
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    // Create a temporary UserProfile from FirebaseUser
    val userProfile = remember(currentUser) {
        if (currentUser != null) {
            UserProfile(
                fullName = currentUser.displayName ?: "User",
                phoneNumber = currentUser.phoneNumber ?: "",
                profilePicUrl = currentUser.photoUrl?.toString() ?: "",
                email = currentUser.email ?: "",
                isAdmin = currentUser.email == "admin@admin.com" // Simple admin check
            )
        } else {
            null
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.white))
    ) {
        ProfileHeader(
            user = userProfile,
            onBack = onBack,
            onEditClick = onEditProfileClick
        )
        
        Column(
            Modifier
                .fillMaxSize()
                .padding(top = 280.dp) // Adjusted overlap
                .clip(shape = RoundedCornerShape(topEnd = 32.dp, topStart = 32.dp))
                .background(color = colorResource(id = R.color.white))
                .verticalScroll(state = scroll)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            // 3 Action Buttons
            ProfileQuickActions(
                onMyAdsClick = onManageCarsClick,
                onFavoritesClick = onFavoritesClick,
                onSettingsClick = { 
                    Toast.makeText(context, "Settings clicked", Toast.LENGTH_SHORT).show() 
                }
            )
            
            Spacer(Modifier.height(24.dp))
            
            // Menu List
            ProfileSettings(
                isAdmin = userProfile?.isAdmin == true,
                onEditProfileClick = onEditProfileClick,
                onContactSupportClick = {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:") // only email apps should handle this
                        putExtra(Intent.EXTRA_EMAIL, arrayOf("support@example.com"))
                        putExtra(Intent.EXTRA_SUBJECT, "Support Request")
                    }
                    try {
                        context.startActivity(intent)
                    } catch (e: Exception) {
                         Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT).show()
                    }
                },
                onManageCarsClick = onAdminManageCarsClick // Use Admin callback here
            )
            
            Spacer(Modifier.height(24.dp))

            // Log Out Button
            Button(
                onClick = {
                    auth.signOut()
                    val intent = Intent(context, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .height(60.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "LOG OUT", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            
            Spacer(Modifier.height(32.dp))
        }
    }
}