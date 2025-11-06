package com.toqsoft.moviebrowser.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.toqsoft.moviebrowser.data.model.Movie
import com.toqsoft.moviebrowser.presentation.navigation.Screen
import com.toqsoft.moviebrowser.presentation.viewmodel.MovieViewModel

@Composable
fun MovieListScreen(
    navController: NavController,
    viewModel: MovieViewModel = hiltViewModel()
) {
    var query by remember { mutableStateOf("") }
    var selectedLanguageName by remember { mutableStateOf("All") }

    val searchResults by viewModel.searchResults.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    val movies = viewModel.movies.collectAsLazyPagingItems()

    val languages = listOf(
        "All" to "",
        "English" to "en",
        "Tamil" to "ta",
        "Hindi" to "hi",
        "Spanish" to "es",
        "French" to "fr",
        "Japanese" to "ja",
        "Korean" to "ko",
        "Chinese" to "zh"
    )

    Column {
        LanguageDropdown(selectedLanguageName) { languageCode, displayName ->
            selectedLanguageName = displayName
            viewModel.selectLanguage(languageCode)
            if (query.isNotEmpty()) {
                viewModel.search(query, languageCode)
            }
        }


        SearchBar(query = query, onQueryChanged = { newQuery ->
            query = newQuery
            val code = languages.find { it.first == selectedLanguageName }?.second
            if (newQuery.isNotEmpty()) {
                viewModel.search(newQuery, code)
            }
        })

        if (query.isEmpty()) {
            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(movies.itemCount) { index ->
                    val movie = movies[index]
                    movie?.let {
                        MovieCard(
                            movie = it,
                            isFavorite = favorites.contains(it.id),
                            onFavoriteClick = { viewModel.toggleFavorite(it) },
                            onClick = { navController.navigate(Screen.Detail.createRoute(it.id)) }
                        )
                    }
                }
            }
        } else {
            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(searchResults) { movie ->
                    MovieCard(
                        movie = movie,
                        isFavorite = favorites.contains(movie.id),
                        onFavoriteClick = { viewModel.toggleFavorite(movie) },
                        onClick = { navController.navigate(Screen.Detail.createRoute(movie.id)) }
                    )
                }
            }
        }
    }
}

@Composable
fun LanguageDropdown(
    selectedLanguageName: String,
    onLanguageSelected: (languageCode: String, displayName: String) -> Unit
) {
    val languages = listOf(
        "All" to "",
        "English" to "en",
        "Tamil" to "ta",
        "Hindi" to "hi",
        "Spanish" to "es",
        "French" to "fr",
        "Japanese" to "ja",
        "Korean" to "ko",
        "Chinese" to "zh"
    )

    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
            Text(text = selectedLanguageName)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.fillMaxWidth()) {
            languages.forEach { (name, code) ->
                DropdownMenuItem(
                    text = { Text(name) },
                    onClick = {
                        onLanguageSelected(code, name)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Composable
fun MovieCard(
    movie: Movie,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            Box {
                Image(
                    painter = rememberAsyncImagePainter("https://image.tmdb.org/t/p/w500${movie.poster_path}"),
                    contentDescription = movie.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.7f),
                    contentScale = ContentScale.Crop
                )

                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color.White
                    )
                }
            }

            Text(
                text = movie.title,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
@Composable
fun SearchBar(query: String, onQueryChanged: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        placeholder = { Text("Search movies...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChanged("") }) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear search")
                }
            }
        },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF0F0F0),
            unfocusedContainerColor = Color(0xFFF0F0F0),
            cursorColor = Color.Black,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        ),
        shape = RoundedCornerShape(24.dp),
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
    )
}
