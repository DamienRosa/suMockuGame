package com.dgr.sumocku.viewmodel

import androidx.lifecycle.ViewModel
import com.dgr.sumocku.game.SudokuGame

class PlayViewModel : ViewModel() {

    val sudokuGame = SudokuGame()

}