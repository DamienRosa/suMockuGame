package com.dgr.sumocku.game

data class Board(
    var size: Int,
    val cells: List<Cell>
) {

    fun getCell(row: Int, col: Int): Cell? = cells.find { it.col == col && it.row == row }

}