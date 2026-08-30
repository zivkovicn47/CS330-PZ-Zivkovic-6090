package com.zivkovic.project250.ui.feature.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip // Added import
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zivkovic.project250.R
import com.zivkovic.project250.domain.CategoryModel

@Composable
fun CategoryList(
    categories: List<CategoryModel>,
    selectedId: Int? = null,
    onCategoryClick: (Int) -> Unit = {},
    modifier: Modifier = Modifier
){
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(categories){category->
            val isSelected = category.idInt == selectedId
            // Fixed Style: Always White background to support logos
            val backgroundColor = Color.White
            // Selection is shown via BORDER (Thick Black vs Thin Gray)
            val borderStroke = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, Color.Black) else androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray)

            Column(modifier=Modifier
                .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(55.dp)
                        .clip(CircleShape)
                        .background(
                            color = backgroundColor,
                            shape = CircleShape
                        )
                        .border(borderStroke, CircleShape)
                        .clickable { onCategoryClick(category.idInt) },
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(category.picUrl)
                            .crossfade(true)
                            .error(R.drawable.car)
                            .fallback(R.drawable.car)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp).padding(1.dp),
                        contentScale = ContentScale.Fit,
                        placeholder = painterResource(R.drawable.car),
                        error = painterResource(R.drawable.car)
                    )
                }
                Text(
                    text = category.title,
                    color = Color.Black,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top=4.dp),
                    fontWeight = if (isSelected) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal
                )
            }
        }
    }
}
@Preview
@Composable
fun CategoryListPreview(){
    val categories = listOf(
        CategoryModel(id = 0, title = "BMW", picUrl = "url1"),
        CategoryModel(id = 1, title = "Audi", picUrl = "url2"),
        CategoryModel(id = 2, title = "Mercedes", picUrl = "url3")
    )
    CategoryList(
        categories = categories,
        selectedId = 1,
        onCategoryClick = {}
    )
}