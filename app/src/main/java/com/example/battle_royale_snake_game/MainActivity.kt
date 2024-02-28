package com.example.battle_royale_snake_game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.battle_royale_snake_game.ui.screens.HomeScreen
import com.example.battle_royale_snake_game.ui.theme.BattleRoyaleSnakeGameTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    enableEdgeToEdge()

    setContent {
      BattleRoyaleSnakeGameTheme {
        Surface(
          modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
          color = MaterialTheme.colorScheme.background
        ) {
          HomeScreen()
        }
      }
    }
  }
}
