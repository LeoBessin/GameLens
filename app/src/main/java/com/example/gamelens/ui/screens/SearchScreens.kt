package com.example.gamelens.ui.screens

import android.R.attr.contentDescription
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.gamelens.ui.MyError
import com.example.gamelens.ui.viewmodel.MainViewModel
import com.example.gamelens.ui.viewmodel.PictureBean
import com.example.gamelens.R
import com.example.gamelens.ui.theme.GameLensTheme

@Composable
fun FilterOptions(
    modifier: Modifier = Modifier,
    onModeChange: (Boolean) -> Unit = {}
) {
    var isGridMode by remember { mutableStateOf(false) }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(
            onClick = {
                isGridMode = false
                onModeChange(false)
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.List,
                contentDescription = "Mode Liste",
                tint = if (!isGridMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        IconButton(
            onClick = {
                isGridMode = true
                onModeChange(true)
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_apps_24),
                contentDescription = "Mode Grille",
                tint = if (isGridMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    searchText: MutableState<String>,
    onSearch: () -> Unit = {}
)  {
    TextField(
        value = searchText.value,
        onValueChange = { it:String -> searchText.value = it },
        placeholder = { Text(stringResource(R.string.search_hint), color = MaterialTheme.colorScheme.onSurfaceVariant) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = MaterialTheme.colorScheme.primary
            )
        }, // Définir le bouton "Entrée" comme action de recherche
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            onSearch()
        }),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(8.dp),
        singleLine = true
    )
}


@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel(),
    onPictureClick : (PictureBean)->Unit
) {
    val searchText = remember { mutableStateOf("") }
    val list = viewModel.dataList.collectAsState().value
    val runInProgress = viewModel.runInProgress.collectAsState().value
    val errorMessage = viewModel.errorMessage.collectAsState().value
    val isGridMode = remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(top = 16.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SearchBar(
                    searchText = searchText,
                    onSearch = {viewModel.searchGames(searchText.value)}
                )
            }
            item {
                FilterOptions(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    onModeChange = { isGridMode.value = it }
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    AnimatedVisibility(
                        visible = runInProgress,
                    ) {
                        CircularProgressIndicator()
                    }
                    AnimatedVisibility(
                        visible = errorMessage != "",
                    ) {
                        MyError(errorMessage = errorMessage)
                    }
                }
            }
            // switch between grid and list mode
            if (isGridMode.value) {
                item {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 600.dp), // Ajustez la hauteur selon le besoin
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        content = {
                            items(list.size) { index ->
                                PictureCardItem(
                                    modifier = Modifier.padding(8.dp),
                                    data = list[index],
                                    onClick = onPictureClick
                                )
                            }
                        }
                    )
                }
            } else {
                items(list.size) { index ->
                    PictureRowItem(data = list[index], onClick = onPictureClick)
                }
            }
        }
        ActionButtons(
            onClear = {
                searchText.value = ""
                viewModel.loadGames(1)
            },
            onLoad = {
                viewModel.searchGames(searchText.value)
            }
        )
    }
}

@Composable
fun ActionButtons(
    onClear: () -> Unit,
    onLoad: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
    ) {
        Button(
            onClick = onClear,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(50)
        ) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Clear",
                tint = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.clear_filter), color = MaterialTheme.colorScheme.onPrimary)
        }

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = onLoad,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(50)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Load",
                tint = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.load_data), color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PictureRowItem(
    modifier: Modifier = Modifier,
    data: PictureBean,
    onClick: (PictureBean)->Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    Card(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(4.dp)
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxWidth()
        ) {
            // Image et Spacer inchangés
            GlideImage(
                model = data.url,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clickable { onClick(data) }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f).clickable { isExpanded = !isExpanded }) {
                Text(
                    text = data.title,
                    modifier = Modifier.padding(top = 8.dp),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = if (isExpanded) data.longText else (data.longText.take(20) + "..."),
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .animateContentSize()
                )
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
            .padding(8.dp)
            .aspectRatio(0.8f) // Largeur proportionnelle à la hauteur
            .height(240.dp)
            .clickable { onClick(data) },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            GlideImage(
                model = data.url,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.Center)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = data.title,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 22.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = data.longText.take(40) + "...",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SearchScreenPreview() {
    val viewModel: MainViewModel = viewModel()
    // Simulate some data for preview
    GameLensTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
        ) { innerPadding ->
            com.example.gamelens.ui.screens.SearchScreen(
                modifier = Modifier.padding(innerPadding),
                viewModel = viewModel,
                onPictureClick = { pictureBean ->
                    // Handle picture click, e.g., navigate to detail screen
                }
            )
        }
    }
}