package com.example.gamelens.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.gamelens.ui.MetaCriticScore
import com.example.gamelens.ui.IconList
import com.example.gamelens.ui.StarRating
import com.example.gamelens.ui.formatedDate
import com.example.gamelens.ui.viewmodel.MainViewModel
import com.example.gamelens.ui.viewmodel.PictureBean

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun DetailScreen(
    pictureBean: PictureBean,
    navHostController: NavHostController,
    viewModel: MainViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Détails") },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            GlideImage(
                model = pictureBean.url,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(8.dp))
            // Titre
            Text(
                text = pictureBean.title,
                fontSize = 24.sp,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
            Spacer(Modifier.height(4.dp))
            StarRating(
                rating = pictureBean.rating,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .height(28.dp)
            )
            Spacer(Modifier.height(8.dp))
            // Plateformes et Metacritic
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconList(
                    modifier = Modifier.weight(1f),
                    icons = pictureBean.platforms
                )
                MetaCriticScore(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(start = 8.dp),
                    score = pictureBean.metacritic
                )
            }
            Spacer(Modifier.height(8.dp))
            // Date de sortie formatée
            Text(
                text = formatedDate(pictureBean.released),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(16.dp))
            // Autres infos (optionnel)
        }
    }
}