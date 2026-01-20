package com.andone.memorip

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.andone.memorip.navigation.rememberMemoripNavigator
import com.andone.memorip.presentation.screen.plan.component.PlanTimeTable
import com.andone.memorip.presentation.screen.plan.model.PlanUiState
import com.andone.memorip.presentation.screen.plan.model.TimeBlock
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.snackbar.SnackBarManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var snackbarManager: SnackBarManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MemoripTheme {
                var previewState by remember {
                    mutableStateOf(
                        PlanUiState(
                            blocks = listOf(
                                TimeBlock("0", 0, 60),
                                TimeBlock("1", 3 * 60, 60),
                                TimeBlock("2", 11 * 60 + 30, 90),
                                TimeBlock("3", 15 * 60, 45)
                            )
                        )
                    )
                }

                PlanTimeTable(
                    uiState = previewState,
                    onBlockMoved = { id, newStartMinute ->
                        previewState = previewState.copy(
                            blocks = previewState.blocks.map { block ->
                                if (block.id == id) {
                                    block.copy(startMinute = newStartMinute)
                                } else block
                            }
                        )
                        Log.d("전체 테이블", "${previewState.blocks}")
                    }
                )
//                val navigator = rememberMemoripNavigator()
//                MemoripApp(
//                    navigator = navigator,
//                    snackbarManager = snackbarManager
//                )
            }
        }
    }
}