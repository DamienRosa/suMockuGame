package com.dgr.sumocku.ui

import android.os.Bundle
import com.dgr.sumocku.R
import com.dgr.sumocku.extensions.BaseActivity
import com.dgr.sumocku.extensions.observe
import com.dgr.sumocku.game.Cell
import com.dgr.sumocku.ui.custom.SudokuBoardView
import com.dgr.sumocku.viewmodel.PlayViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.generic.instance

class GameActivity : BaseActivity(), SudokuBoardView.OnTouchListener {

    private val viewModel: PlayViewModel by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViewModel()
        initView()
    }

    private fun initView() {
        sbv_game.setTouchListener(this)

        val buttonsList = listOf(
            btn_one, btn_two, btn_three,
            btn_four, btn_five, btn_six,
            btn_seven, btn_eight, btn_nine
        )
        buttonsList.forEachIndexed { index, button ->
            button.setOnClickListener { viewModel.sudokuGame.handleInput(index + 1) }
        }
    }

    private fun initViewModel() {
        observe(viewModel.sudokuGame.selectedCell) {
            sbv_game.updateSelectedCellUI(it.first, it.second)
        }

        observe(viewModel.sudokuGame.cellsList) { updateCells(it) }
    }

    private fun updateCells(cells: List<Cell>?) {
        cells?.let {
            sbv_game.updateCells(cells)
        }
    }

    override fun onCellSelected(row: Int, col: Int) {
        viewModel.sudokuGame.updateSelectedCell(row, col)
    }
}
