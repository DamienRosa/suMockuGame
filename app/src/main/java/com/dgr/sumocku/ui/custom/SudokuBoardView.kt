package com.dgr.sumocku.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.dgr.sumocku.game.Cell
import kotlin.math.min

class SudokuBoardView(
    context: Context,
    attributeSet: AttributeSet
) : View(context, attributeSet) {

    private lateinit var listener: OnTouchListener

    private var cells: List<Cell>? = null

    private val sqrtSize = 3
    private val size = 9

    private var cellSizePixels = 0F
    private var noteSizePixels = 0F

    private var selectedRow = -1
    private var selectedCol = -1

    private val thickBoxPaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 4F
    }

    private val thinBoxPaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.GRAY
        strokeWidth = 2F
    }

    private val selectedBoxPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#6EAD3A")
    }

    private val conflictingBoxPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#EFEDEF")
    }

    private val textPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.BLACK
    }

    private val startingCellTextPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.BLACK
        typeface = Typeface.DEFAULT_BOLD
    }

    private val noteTextPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.BLACK
    }

    private val startingCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.BLACK
        color = Color.parseColor("#ACACAC")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val sizePixels = min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(sizePixels, sizePixels)
    }

    override fun onDraw(canvas: Canvas) {
        updateMeasurements(width)
        fillCells(canvas)
        drawLines(canvas)
        drawText(canvas)
    }

    private fun updateMeasurements(width: Int) {
        cellSizePixels = (width / size).toFloat()
        noteSizePixels = cellSizePixels / sqrtSize.toFloat()
        noteTextPaint.textSize = noteSizePixels
        textPaint.textSize = cellSizePixels / 1.5F
        startingCellTextPaint.textSize = cellSizePixels / 1.5F
    }

    private fun fillCells(canvas: Canvas) {
        if (selectedCol == -1 || selectedRow == -1) return

        cells?.forEach {
            val r = it.row
            val c = it.col
            when {
                it.isStartingCell -> fillCell(canvas, r, c, startingCellPaint)
                r == selectedRow && c == selectedCol -> fillCell(canvas, r, c, selectedBoxPaint)
                r == selectedRow || c == selectedCol -> fillCell(canvas, r, c, conflictingBoxPaint)
                r / sqrtSize == selectedRow / sqrtSize && c / sqrtSize == selectedCol / sqrtSize ->
                    fillCell(canvas, r, c, conflictingBoxPaint)
            }
        }
    }

    private fun fillCell(canvas: Canvas, r: Int, c: Int, paint: Paint) {
        canvas.drawRect(
            c * cellSizePixels,
            r * cellSizePixels,
            (c + 1) * cellSizePixels,
            (r + 1) * cellSizePixels,
            paint
        )
    }

    private fun drawLines(canvas: Canvas) {
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), thickBoxPaint)

        for (i in 1 until size) {
            val paintToUse = when (i % sqrtSize) {
                0 -> thickBoxPaint
                else -> thinBoxPaint
            }

            canvas.drawLine(
                i * cellSizePixels,
                0F,
                i * cellSizePixels,
                height.toFloat(),
                paintToUse
            )

            canvas.drawLine(
                0F,
                i * cellSizePixels,
                width.toFloat(),
                i * cellSizePixels,
                paintToUse
            )
        }
    }

    private fun drawText(canvas: Canvas) {
        cells?.forEach { cell ->
            val textBounds = Rect()

            if (cell.value == 0) {
                // draw notes
                cell.notes.forEach { note ->
                    val rowCell = (note - 1) / sqrtSize
                    val colCell = (note - 1) % sqrtSize
                    val value = note.toString()

                    noteTextPaint.getTextBounds(value, 0, value.length, textBounds)
                    val textWidth = noteTextPaint.measureText(value)
                    val textHeight = textBounds.height()

                    canvas.drawText(
                        value,
                        (cell.col * cellSizePixels) + (colCell * noteSizePixels) + noteSizePixels / 2 - textWidth / 2,
                        (cell.row * cellSizePixels) + (rowCell * noteSizePixels) + noteSizePixels / 2 + textHeight / 2,
                        noteTextPaint
                    )
                }

            } else {
                val row = cell.row
                val col = cell.col
                val value = cell.value.toString()

                val paintToUse =
                    when {
                        cell.isStartingCell -> startingCellTextPaint
                        else -> textPaint
                    }

                paintToUse.getTextBounds(value, 0, value.length, textBounds)
                val textWidth = paintToUse.measureText(value)
                val textHeight = textBounds.height()

                canvas.drawText(
                    value,
                    (col * cellSizePixels) + cellSizePixels / 2 - textWidth / 2,
                    (row * cellSizePixels) + cellSizePixels / 2 + textHeight / 2,
                    textPaint
                )
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean =
        when (event.action) {
            MotionEvent.ACTION_DOWN -> handlePressDown(event.x, event.y)
            else -> false
        }

    private fun handlePressDown(x: Float, y: Float): Boolean {
        val possibleSelectedRow = (y / cellSizePixels).toInt()
        val possibleSelectedCol = (x / cellSizePixels).toInt()
        listener.onCellSelected(possibleSelectedRow, possibleSelectedCol)
        return true
    }

    fun updateSelectedCellUI(row: Int, col: Int) {
        selectedRow = row
        selectedCol = col
        invalidate()
    }

    fun updateCells(cells: List<Cell>) {
        this.cells = cells
        invalidate()
    }

    interface OnTouchListener {
        fun onCellSelected(row: Int, col: Int)
    }

    fun setTouchListener(listener: OnTouchListener) {
        this.listener = listener
    }
}