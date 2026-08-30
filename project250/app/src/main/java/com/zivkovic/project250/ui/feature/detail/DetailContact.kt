package com.zivkovic.project250.ui.feature.detail

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zivkovic.project250.R

/**
 * Prikaz opcionog polja phone (ugovor o sinhronizaciji, tacka 6).
 * Ako oglas nema broj, korisniku se javlja da prodavac nije ostavio kontakt.
 */
@Composable
fun DetailContact(phone: String?) {
    val context = LocalContext.current
    val number = phone?.trim().orEmpty()

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Text(
            text = "Contact",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = colorResource(R.color.black)
        )

        if (number.isEmpty()) {
            Text(
                text = "The seller did not leave a contact number.",
                color = Color.Gray,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        } else {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = number,
                    color = colorResource(R.color.black),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
                        try {
                            context.startActivity(intent)
                        } catch (e: ActivityNotFoundException) {
                            Toast.makeText(
                                context,
                                "No dialer app available on this device",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier.height(48.dp),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.black),
                        contentColor = colorResource(R.color.white)
                    )
                ) {
                    Icon(Icons.Default.Call, contentDescription = null)
                    Text(text = "  Call", fontSize = 16.sp)
                }
            }
        }
    }
}

@Preview
@Composable
fun DetailContactPreview() {
    DetailContact(phone = "+381 60 123 4567")
}

@Preview
@Composable
fun DetailContactEmptyPreview() {
    DetailContact(phone = null)
}
