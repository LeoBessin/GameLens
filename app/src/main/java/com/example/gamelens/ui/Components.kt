package com.example.gamelens.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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