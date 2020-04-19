package com.dgr.sumocku.game

import androidx.lifecycle.MutableLiveData

class SudokuGame {

    var selectedCell = MutableLiveData<Pair<Int, Int>>()
    var cellsList = MutableLiveData<List<Cell>>()

    private var selectedRow = -1
    private var selectedCol = -1

    private var board: Board

    init {
        val cells = List(9 * 9) { i -> Cell(i / 9, i % 9, i % 9) }
        cells[45].isStartingCell = true
        cells[79].isStartingCell = true

        selectedCell.postValue(Pair(selectedRow, selectedCol))
        board = Board(9, cells)
        cellsList.postValue(board.cells)
    }

    fun handleInput(number: Int) {
        if (selectedRow == -1 || selectedCol == -1) return
        if (!board.getCell(selectedRow, selectedCol)!!.isStartingCell) return

        board.getCell(selectedRow, selectedCol)!!.value = number
        cellsList.postValue(board.cells)
    }

    fun updateSelectedCell(row: Int, col: Int) {
        if (board.getCell(row, col)!!.isStartingCell) return

        selectedRow = row
        selectedCol = col
        selectedCell.postValue(Pair(row, col))
    }

}