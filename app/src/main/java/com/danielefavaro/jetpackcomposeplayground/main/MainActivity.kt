package com.danielefavaro.jetpackcomposeplayground.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.danielefavaro.jetpackcomposeplayground.main.state.RatingState
import com.danielefavaro.jetpackcomposeplayground.main.viewmodel.MainViewModel
import com.danielefavaro.jetpackcomposeplayground.ui.components.ratingbar.RatingCompose
import com.danielefavaro.jetpackcomposeplayground.ui.components.ratingbar.model.SectorDatasetModel
import com.danielefavaro.jetpackcomposeplayground.ui.components.ratingbignumber.RatingBigNumberCompose
import com.danielefavaro.jetpackcomposeplayground.ui.theme.JetpackComposePlaygroundTheme
import com.danielefavaro.jetpackcomposeplayground.ui.theme.defaultMargin
import com.danielefavaro.jetpackcomposeplayground.ui.theme.defaultMarginL
import com.danielefavaro.jetpackcomposeplayground.ui.theme.defaultMarginXL

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposePlaygroundTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val ratingState by viewModel.ratingState.collectAsState()

                    Body(ratingState = ratingState)
                }
            }
        }
    }
}

@Composable
private fun Body(ratingState: RatingState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = defaultMarginL)
            .padding(vertical = defaultMarginXL)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(defaultMargin),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RatingCompose(
            score = ratingState.score,
            animDuration = ratingState.animDuration,
            animDelay = ratingState.animDelay,
            sectorDatasetModel = SectorDatasetModel(
                scoreMaxLabel = ratingState.scoreMaxLabel,
                scoreMinLabel = ratingState.scoreMinLabel,
                sectorList = ratingState.sectorList
            )
        )

        RatingBigNumberCompose(
            score = ratingState.score,
            animDuration = ratingState.animDuration * 2,
            animDelay = ratingState.animDelay
        )
    }
}
