package com.example.battle_royale_snake_game.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

const val GAME_TICK_MS = 300L

enum class PlayerDirection {
  RIGHT,
  LEFT,
  FORWARD,
  BACKWARDS,
}

@Composable
fun HomeScreen() {
  val localDensity = LocalDensity.current

  var screenCenterHeightDp by remember { mutableStateOf(0.dp) }
  var screenCenterWidthDp by remember { mutableStateOf(0.dp) }

  val pixelSizeDp = 24.dp

  var playerDirection by remember { mutableStateOf(PlayerDirection.FORWARD) }
  var hasPlayerMovedInTick = false
  var playerShiftPixelsUnitInX by remember { mutableStateOf(0) }
  var playerShiftPixelsUnitInY by remember { mutableStateOf(0) }
  val playerX by remember {
    derivedStateOf {
      screenCenterWidthDp - (pixelSizeDp / 2) - (playerShiftPixelsUnitInX * pixelSizeDp)
    }
  }
  val playerY by remember {
    derivedStateOf {
      screenCenterHeightDp - (pixelSizeDp / 2) - (playerShiftPixelsUnitInY * pixelSizeDp)
    }
  }

  fun movePlayer() {
    when (playerDirection) {
      PlayerDirection.FORWARD -> playerShiftPixelsUnitInY += 1
      PlayerDirection.BACKWARDS -> playerShiftPixelsUnitInY -= 1
      PlayerDirection.LEFT -> playerShiftPixelsUnitInX += 1
      PlayerDirection.RIGHT -> playerShiftPixelsUnitInX -= 1
    }
  }

  LaunchedEffect(Unit) {
    while (true) {
      movePlayer()
      hasPlayerMovedInTick = false

      delay(GAME_TICK_MS)
    }
  }

  Column(modifier = Modifier.padding(top = 32.dp, start = 24.dp)) {
    Row {
      Text(text = "DIRECTION: ")
      when (playerDirection) {
        PlayerDirection.FORWARD -> Text(text = "FORWARD")
        PlayerDirection.BACKWARDS -> Text(text = "BACKWARDS")
        PlayerDirection.LEFT -> Text(text = "LEFT")
        PlayerDirection.RIGHT -> Text(text = "RIGHT")
      }
    }
    Row {
      Text(text = "HAS MOVED IN THIS TICK: $hasPlayerMovedInTick")
    }
  }

  Box(
    modifier = Modifier
      .pointerInput(hasPlayerMovedInTick) {
        detectDragGestures { change, dragAmount ->
          change.consume()

          val (x, y) = dragAmount

          if (hasPlayerMovedInTick) return@detectDragGestures

          when (playerDirection) {
            PlayerDirection.LEFT, PlayerDirection.RIGHT -> {
              when {
                y > 10 -> {
                  playerDirection = PlayerDirection.BACKWARDS
                  hasPlayerMovedInTick = true
                }

                y < -10 -> {
                  playerDirection = PlayerDirection.FORWARD
                  hasPlayerMovedInTick = true
                }
              }
            }

            PlayerDirection.FORWARD, PlayerDirection.BACKWARDS -> {
              when {
                x < -10 -> {
                  playerDirection = PlayerDirection.LEFT
                  hasPlayerMovedInTick = true
                }

                x > 10 -> {
                  playerDirection = PlayerDirection.RIGHT
                  hasPlayerMovedInTick = true
                }
              }
            }
          }

        }
      }
  ) {
  }

  Box(modifier = Modifier
    .statusBarsPadding()
    .onGloballyPositioned { coordinates ->
      screenCenterHeightDp = with(localDensity) { coordinates.size.height.toDp() / 2 }
      screenCenterWidthDp = with(localDensity) { coordinates.size.width.toDp() / 2 }
    }) {
    Box(
      modifier = Modifier
        .size(pixelSizeDp)
        .offset(x = playerX, y = playerY)
        .background(colorScheme.primary)
    )
  }
}