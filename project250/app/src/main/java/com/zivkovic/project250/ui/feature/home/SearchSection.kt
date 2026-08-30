package com.zivkovic.project250.ui.feature.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.automirrored.filled.List

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.zivkovic.project250.R

@Composable
@Preview
fun SearchSection(
    query: String = "",
    onQueryChange: (String) -> Unit = {}
) {
    ConstraintLayout(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(50.dp)
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(50.dp))
            .padding(horizontal = 4.dp)
    ) {
        val (searchInput, iconBox) = createRefs()

        TextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Search car...")},
            leadingIcon = {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Search,
                    contentDescription = null
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            modifier = Modifier
                .constrainAs(searchInput){
                    start.linkTo(parent.start)
                    end.linkTo(iconBox.start, margin = 8.dp)
                    width = Dimension.fillToConstraints
                }
        )
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color.Black, CircleShape)
                .constrainAs(iconBox){
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = androidx.compose.material.icons.Icons.AutoMirrored.Filled.List,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}