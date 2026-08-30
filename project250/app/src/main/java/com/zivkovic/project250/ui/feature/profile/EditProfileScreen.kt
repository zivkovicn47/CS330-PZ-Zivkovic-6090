package com.zivkovic.project250.ui.feature.profile

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBack: () -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val currentUser = auth.currentUser
    val context = LocalContext.current

    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var photoUrl by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            fullName = currentUser.displayName ?: ""
            photoUrl = currentUser.photoUrl?.toString() ?: ""
            
            // Try fetch from Firestore for phone/additional data
            try {
                val doc = firestore.collection("users").document(currentUser.uid).get().await()
                if (doc.exists()) {
                    val firestoreName = doc.getString("fullName")
                    val firestorePhone = doc.getString("phoneNumber")
                    val firestorePhoto = doc.getString("profilePicUrl")
                    
                    if (!firestoreName.isNullOrEmpty()) fullName = firestoreName
                    if (!firestorePhone.isNullOrEmpty()) phoneNumber = firestorePhone
                    if (!firestorePhoto.isNullOrEmpty()) photoUrl = firestorePhoto
                }
            } catch (e: Exception) {
                // Ignore initial fetch error
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone Number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = photoUrl,
                onValueChange = { photoUrl = it },
                label = { Text("Profile Image URL") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (currentUser == null) return@Button
                    isLoading = true
                    
                    val userUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(fullName)
                        .setPhotoUri(if (photoUrl.isNotEmpty()) android.net.Uri.parse(photoUrl) else null)
                        .build()

                    currentUser.updateProfile(userUpdates).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Update Firestore
                            val userData = hashMapOf(
                                "fullName" to fullName,
                                "phoneNumber" to phoneNumber,
                                "profilePicUrl" to photoUrl,
                                "email" to (currentUser.email ?: "")
                            )
                            
                            firestore.collection("users").document(currentUser.uid)
                                .set(userData)
                                .addOnSuccessListener {
                                    isLoading = false
                                    Toast.makeText(context, "Profile Updated!", Toast.LENGTH_SHORT).show()
                                    onBack()
                                }
                                .addOnFailureListener {
                                    isLoading = false
                                    Toast.makeText(context, "Failed to update Firestore: ${it.message}", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            isLoading = false
                            Toast.makeText(context, "Failed to update Auth Profile: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                enabled = !isLoading
            ) {
                Text(
                    text = if (isLoading) "SAVING..." else "SAVE CHANGES",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
