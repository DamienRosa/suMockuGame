package com.dgr.sumocku.game

data class Cell(
    var row: Int,
    var col: Int,
    var value: Int,
    var isStartingCell: Boolean = false
)