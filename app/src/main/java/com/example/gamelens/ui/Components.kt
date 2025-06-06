package com.example.gamelens.ui

import android.R.attr.data
import com.example.gamelens.R
import android.R.attr.maxLines
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.Placeholder
import com.example.gamelens.ui.viewmodel.PictureBean

@Composable
fun MyError(modifier:Modifier = Modifier, errorMessage:String?){
    Text(
        text = errorMessage ?: "",
        color = MaterialTheme.colorScheme.onError,
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.error,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp)
            .fillMaxWidth()
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PictureRowItem(
    modifier: Modifier = Modifier,
    data: PictureBean,
    onClick: (PictureBean)->Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
        .clickable { onClick(data) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxWidth()
                .height(150.dp)
        ) {
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .background(MaterialTheme.colorScheme.error)
                    .fillMaxSize()
            ) {

                GlideImage(
                    model = data.url,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(), // Fill both height and width
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .animateContentSize(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = data.title,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .weight(1f),
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${data.rating}",
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.animateContentSize(),
                        fontWeight = FontWeight.Bold
                    )
                }
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .animateContentSize()
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    IconList(
                        modifier = Modifier.weight(1f),
                        icons = data.platforms,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    MetaCriticScore(
                        modifier = Modifier
                            .size(36.dp)
                            .animateContentSize(),
                        score = data.metacritic
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .animateContentSize(),
                    verticalAlignment = Alignment.CenterVertically
                )  {
                    Text(
                        text = formatedDate(data.released),
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .weight(1f),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "~${data.playtime} hours",
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .animateContentSize()
                            .padding(top = 8.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PictureCardItem(
    modifier: Modifier = Modifier,
    data: PictureBean,
    onClick: (PictureBean) -> Unit
) {
    Card(
        modifier = modifier
            .height(240.dp)
            .clickable { onClick(data) },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            GlideImage(
                model = data.url,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = data.title,
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconList(
                            modifier = Modifier.weight(1f),
                            icons = data.platforms,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                        MetaCriticScore(
                            modifier = Modifier
                                .size(36.dp)
                                .padding(start = 8.dp),
                            score = data.metacritic
                        )
                    }
                    Text(
                        text = formatedDate(data.released),
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontSize = 14.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

fun formatedDate (date: String): String {
    return try {
        val inputFormat = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
        val outputFormat = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.US)
        outputFormat.format(inputFormat.parse(date) ?: java.util.Date())
    } catch (e: Exception) {
        date
    }
}

@Composable
fun MetaCriticScore(
    modifier: Modifier = Modifier,
    score: Int?
) {
    val color = score?.let {
        when {
            it >= 76 -> androidx.compose.ui.graphics.Color(0xFF4CAF50) // Vert
            it >= 51 -> androidx.compose.ui.graphics.Color(0xFFFF9800) // Orange
            it >= 26 -> androidx.compose.ui.graphics.Color(0xFFFFEB3B) // Jaune
            else -> androidx.compose.ui.graphics.Color(0xFFF44336) // Rouge
        }
    } ?: MaterialTheme.colorScheme.secondary

    Box(
        modifier = modifier
            .size(36.dp)
            .background(color, shape = RoundedCornerShape(6.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = score?.toString() ?: "N/A",
            fontSize = 18.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier.animateContentSize(),
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun IconList(
    modifier: Modifier = Modifier,
    icons: List<String>,
    color: Color = MaterialTheme.colorScheme.secondary
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val iconsParsed = icons.map { platform ->
            when (platform) {
                "linux" -> R.drawable.linux
                "pc" -> R.drawable.pc
                "mac" -> R.drawable.mac
                "xbox" -> R.drawable.xbox
                "playstation" -> R.drawable.playstation
                "android" -> R.drawable.android
                "ios" -> R.drawable.ios
                "nintendo" -> R.drawable.nintendo
                else -> R.drawable.baseline_question_mark_24 // Handle unexpected values
            }
        }
        iconsParsed.forEach { iconRes ->
            androidx.compose.foundation.Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier
                    .size(18.dp)
                    .padding(end = 4.dp),
                colorFilter = ColorFilter.tint(color),
            )
        }
    }
}

@Composable
fun StarRating(
    rating: Double,
    modifier: Modifier = Modifier,
    starSize: Dp = 24.dp,
    filledStar: Int = R.drawable.baseline_star_24,
    halfStar: Int = R.drawable.baseline_star_half_24,
    emptyStar: Int = R.drawable.baseline_star_outline_24,
    starColor: Color = MaterialTheme.colorScheme.secondary
) {
    val fullStars = rating.toInt()
    val hasHalfStar = (rating - fullStars) >= 0.5
    Row(modifier = modifier) {
        for (i in 1..5) {
            val iconRes = when {
                i <= fullStars -> filledStar
                i == fullStars + 1 && hasHalfStar -> halfStar
                else -> emptyStar
            }
            androidx.compose.foundation.Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(starSize),
                colorFilter = ColorFilter.tint(starColor)
            )
        }
    }
}
