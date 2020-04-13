package com.dgr.sumocku.ui

import android.os.Bundle
import androidx.lifecycle.Observer
import com.dgr.sumocku.R
import com.dgr.sumocku.extensions.BaseActivity
import com.dgr.sumocku.ui.custom.SudokuBoardView
import com.dgr.sumocku.viewmodel.PlayViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.generic.instance

class GameActivity : BaseActivity(), SudokuBoardView.OnTouchListener {

    private val viewModel: PlayViewModel by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sbv_game.setTouchListener(this)

        viewModel.sudokuGame.selectedCell.observe(this, Observer {
            updateSelectedCellUI(it.first, it.second)
        })
    }

    private fun updateSelectedCellUI(row: Int, col: Int) {
        sbv_game.updateSelectedCellUI(row, col)
    }

    override fun onCellSelected(row: Int, col: Int) {
        viewModel.sudokuGame.updateSelectedCell(row, col)
    }
}
