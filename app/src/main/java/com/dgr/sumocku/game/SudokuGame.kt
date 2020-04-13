package com.dgr.sumocku.game

import androidx.lifecycle.MutableLiveData

class SudokuGame {

    var selectedCell = MutableLiveData<Pair<Int, Int>>()

    private var selectedRow = -1
    private var selectedCol = -1

    fun updateSelectedCell(row: Int, col: Int) {
        selectedRow = row
        selectedCol = col
        selectedCell.postValue(Pair(row, col))
    }

}