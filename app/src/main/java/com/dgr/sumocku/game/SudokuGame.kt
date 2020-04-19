package com.dgr.sumocku.game

import androidx.lifecycle.MutableLiveData

class SudokuGame {

    var selectedCell = MutableLiveData<Pair<Int, Int>>()
    var cellsList = MutableLiveData<List<Cell>>()
    val isTakingNotes = MutableLiveData<Boolean>()
    val highlightedKeys = MutableLiveData<Set<Int>>()

    private var _selectedRow = -1
    private var _selectedCol = -1
    private var _isTakingNotes = false
    private var _board: Board

    init {
        val cells = List(9 * 9) { i -> Cell(i / 9, i % 9, (i % 9) + 1) }
        cells[45].isStartingCell = true
        cells[79].isStartingCell = true

        selectedCell.postValue(Pair(_selectedRow, _selectedCol))
        _board = Board(9, cells)
        cellsList.postValue(_board.cells)
        isTakingNotes.postValue(_isTakingNotes)
    }

    fun handleInput(number: Int) {
        if (_selectedRow == -1 || _selectedCol == -1) return
        val cell = _board.getCell(_selectedRow, _selectedCol)!!
        if (cell.isStartingCell) return

        if (_isTakingNotes) {
            if (cell.notes.contains(number)) {
                cell.notes.remove(number)
            } else {
                cell.notes.add(number)
            }
            highlightedKeys.postValue(cell.notes)
        } else {
            cell.value = number
        }
        cellsList.postValue(_board.cells)
    }

    fun updateSelectedCell(row: Int, col: Int) {
        val cell = _board.getCell(row, col)!!
        if (cell.isStartingCell) return

        _selectedRow = row
        _selectedCol = col
        selectedCell.postValue(Pair(row, col))

        if (_isTakingNotes) {
            highlightedKeys.postValue(cell.notes)
        }
    }

    fun changeNoteTakingState() {
        _isTakingNotes = !_isTakingNotes
        isTakingNotes.postValue(_isTakingNotes)

        val curNotes: Set<Int> = when {
            _isTakingNotes -> _board.getCell(_selectedRow, _selectedCol)!!.notes
            else -> setOf()
        }
        highlightedKeys.postValue(curNotes)
    }

}