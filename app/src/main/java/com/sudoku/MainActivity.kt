package com.sudoku

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private lateinit var gridLayout: GridLayout
    private val board = Array(9) { IntArray(9) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         gridLayout = findViewById(R.id.board)
        val bg = ContextCompat.getDrawable(this, R.drawable.input_background)

        for (i in 0 until 9) {
            for (j in 0 until 9) {
                val editText = EditText(this)
                editText.layoutParams = GridLayout.LayoutParams().apply {
                    columnSpec = GridLayout.spec(j)
                    rowSpec = GridLayout.spec(i)
                    width = 100
                    height =200

                }
                editText.background = bg
                editText.setPadding(8, 8, 8, 8)
                editText.inputType = InputType.TYPE_CLASS_NUMBER
                editText.setTextColor(Color.BLACK)
                editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
                editText.setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        editText.selectAll()
                    }
                }
                gridLayout.addView(editText)
            }
        }
        val button = findViewById<Button>(R.id.solve_button)
        button.setOnClickListener {
            if (getBoard()) {
                if (solveSudoku(0, 0)) {
                    setBoard()
                } else {
                    val textView = findViewById<TextView>(R.id.messageText)
                    textView.text = "Invalid input"
                }
            }
        }
    }
    private fun getBoard(): Boolean {
        for (i in 0 until 9) {
            for (j in 0 until 9) {
                val editText = gridLayout.getChildAt(i * 9 + j) as EditText
                val value = editText.text.toString()
                if (value.isNotEmpty()) {
                    val num = value.toInt()
                    if (num !in 1..9) {
                        return false
                    }
                    board[i][j] = num
                }
            }
        }
        return true
    }
    private fun setBoard() {
        for (i in 0 until 9) {
            for (j in 0 until 9) {
                val editText = gridLayout.getChildAt(i * 9 + j) as EditText
                val value = board[i][j].toString()
                editText.setText(value)
                editText.clearFocus()
            }
        }
    }
    private fun solveSudoku(row: Int, col: Int): Boolean {
        var i = row
        var j = col
        if (i == 9) {
            i = 0
            j++
            if (j == 9) {
                return true
            }
        }
        if (board[i][j] != 0) {
            return solveSudoku(i + 1, j)
        }
        for (num in 1..9) {
            if (isValid(i, j, num)) {
                board[i][j] = num
                if (solveSudoku(i + 1, j)) {
                    return true
                }
                board[i][j] = 0
            }
        }
        return false
    }
    private fun isValid(row: Int, col: Int, num: Int): Boolean {
        for (i in 0 until 9) {
            if (board[row][i] == num) {
                return false
            }
        }
        for (i in 0 until 9) {
            if (board[i][col] == num) {
                return false
            }
        }

        val boxRowStart = (row / 3) * 3
        val boxColStart = (col / 3) * 3

        for (i in boxRowStart until boxRowStart + 3) {
            for (j in boxColStart until boxColStart + 3) {
                if (board[i][j] == num) {
                    return false
                }
            }
        }

        return true
    }
 }