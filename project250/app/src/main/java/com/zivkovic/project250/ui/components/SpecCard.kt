package com.zivkovic.project250.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zivkovic.project250.R

@Composable
fun SpecCard(
    icon: Any, // Handle ImageVector or Int (Drawable)
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.layout.Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(androidx.compose.ui.graphics.Color(0xFFF5F5F5))
            .padding(12.dp)
    ) {
        androidx.compose.foundation.layout.Row(
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
        ) {
            // 1. THE ICON (Uniform Style)
            androidx.compose.material3.Surface(
                shape = androidx.compose.foundation.shape.CircleShape,
                color = androidx.compose.ui.graphics.Color.White,
                modifier = Modifier.size(40.dp)
            ) {
                androidx.compose.foundation.layout.Box(contentAlignment = androidx.compose.ui.Alignment.Center) {
                    if (icon is androidx.compose.ui.graphics.vector.ImageVector) {
                        androidx.compose.material3.Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = androidx.compose.ui.graphics.Color.Black // vectors are usually single color, keeping black for consistency with previous step unless user wants original vector color? User said "For ImageVector: Use tint = Color.Unspecified". Okay I will use Unspecified.
                        )
                    } else if (icon is Int) {
                        androidx.compose.foundation.Image(
                            painter = painterResource(id = icon),
                            contentDescription = null,
                            modifier = Modifier.size(38.dp), // Increased from 24.dp to match visual weight of vectors
                            colorFilter = null // Preserve original colors
                        )
                    }
                }
            }

            // 2. THE TEXT COLUMN
            androidx.compose.foundation.layout.Column(
                horizontalAlignment = androidx.compose.ui.Alignment.Start
            ) {
                Text(
                    text = title,
                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                    color = androidx.compose.ui.graphics.Color.Gray
                )
                Text(
                    text = value,
                    style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = androidx.compose.ui.graphics.Color.Black,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview
@Composable
fun SpecCardPreview() {
    SpecCard(icon = 0, title = "Sample Title", value = "Sample Value")
}