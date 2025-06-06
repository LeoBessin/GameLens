package com.example.gamelens.ui.screens

import android.R.attr.searchMode
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.GameLensTheme
import com.example.gamelens.ui.MyError
import com.example.gamelens.ui.viewmodel.MainViewModel
import com.example.gamelens.ui.viewmodel.PictureBean
import com.example.gamelens.R
import com.example.gamelens.ui.PictureCardItem
import com.example.gamelens.ui.PictureRowItem
import kotlinx.coroutines.launch

@Composable
            fun FilterOptions(
                modifier: Modifier = Modifier,
                currentMode: Boolean = false,
                onModeChange: (Boolean) -> Unit = {}
            ) {
                Row(
                    modifier = modifier,
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = {
                            onModeChange(false)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.List,
                            contentDescription = "Mode Liste",
                            tint = if (!currentMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = {
                            onModeChange(true)
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_apps_24),
                            contentDescription = "Mode Grille",
                            tint = if (currentMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
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
    onPictureClick: (PictureBean) -> Unit
) {
    val searchText = remember { mutableStateOf("") }
    val list = viewModel.dataList.collectAsState().value
    val runInProgress = viewModel.runInProgress.collectAsState().value
    val errorMessage = viewModel.errorMessage.collectAsState().value
    val isGridMode = rememberSaveable { mutableStateOf(false) }
    val currentPage = rememberSaveable { mutableIntStateOf(1) }
    val previous = viewModel.previous.collectAsState().value
    val next = viewModel.next.collectAsState().value
    val searchMode = rememberSaveable { mutableStateOf("allGame") }
    val onSearch = {
        searchMode.value = "specificGame"
        currentPage.intValue = 1
        viewModel.searchGames(searchText.value, currentPage.intValue)
    }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        modifier = modifier,
        bottomBar = {
            ActionButtons(
                onClear = {
                    searchText.value = ""
                    currentPage.intValue = 1
                    searchMode.value = "allGame"
                    viewModel.loadGames(1)
                },
                onLoad = {
                    currentPage.intValue = 1
                    searchMode.value = "allGame"
                    onSearch()
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(top = 16.dp, bottom = innerPadding.calculateBottomPadding())
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                state = listState,
            ) {
                item {
                    SearchBar(
                        searchText = searchText,
                        onSearch = onSearch
                    )
                }
                item {
                    FilterOptions(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        onModeChange = { isGridMode.value = it },
                        currentMode = isGridMode.value
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
                item {
                    PaginationButtons(
                        canGoPrevious = previous != null,
                        canGoNext = next != null,
                        currentPage = currentPage.intValue,
                        onPrevious = {
                            if (previous != null) {
                                currentPage.intValue -= 1
                                if (searchMode.value == "allGame") {
                                    viewModel.loadGames(currentPage.intValue)
                                } else {
                                    viewModel.searchGames(searchText.value, currentPage.intValue)
                                }
                            }
                        },
                        onNext = {
                            if (next != null) {
                                currentPage.intValue += 1
                                if (searchMode.value == "allGame") {
                                    viewModel.loadGames(currentPage.intValue)
                                } else {
                                    viewModel.searchGames(searchText.value, currentPage.intValue)
                                }
                            }
                        }
                    )
                }
                if (isGridMode.value) {
                    item {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 600.dp),
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
                item {
                    PaginationButtons(
                        canGoPrevious = previous != null,
                        canGoNext = next != null,
                        currentPage = currentPage.intValue,
                        onPrevious = {
                            if (previous != null) {
                                currentPage.intValue -= 1
                                if (searchMode.value == "allGame") {
                                    viewModel.loadGames(currentPage.intValue)
                                } else {
                                    viewModel.searchGames(searchText.value, currentPage.intValue)
                                }
                                coroutineScope.launch {
                                    listState.scrollToItem(0)
                                }
                            }
                        },
                        onNext = {
                            if (next != null) {
                                currentPage.intValue += 1
                                if (searchMode.value == "allGame") {
                                    viewModel.loadGames(currentPage.intValue)
                                } else {
                                    viewModel.searchGames(searchText.value, currentPage.intValue)
                                }
                                coroutineScope.launch {
                                    listState.scrollToItem(0)
                                }
                            }
                        }
                    )
                }
            }
        }
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

@Composable
fun PaginationButtons(
    canGoPrevious: Boolean,
    canGoNext: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    currentPage: Int
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Button(
            onClick = onPrevious,
            enabled = canGoPrevious
        ) {
            Text("Précédent")
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "Page $currentPage"
        )
        Spacer(modifier = Modifier.width(16.dp))
        Button(
            onClick = onNext,
            enabled = canGoNext
        ) {
            Text("Suivant")
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