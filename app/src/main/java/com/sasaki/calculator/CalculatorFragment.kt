package com.sasaki.calculator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class CalculatorFragment : Fragment(), CalculatorViewModel.OnError {

    private var fragmentView: View? = null
    private var toast: Toast? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentView = inflater.inflate(R.layout.fragment_calculator, container, false)

        setupCalculatorNumericKeys()
        setupCalculateTypeKeys()
        setupClearKey()
        setupEqualKey()

        return fragmentView
    }

    private val viewModel: CalculatorViewModel = CalculatorViewModel(this)

    companion object {
        @JvmStatic
        fun newInstance() = CalculatorFragment()
    }

    private fun setupCalculatorNumericKeys() {
        fragmentView?.findViewById<Button>(R.id.button_0)?.setOnClickListener {
            viewModel.setInteger(0)
            showInteger()
        }
        fragmentView?.findViewById<Button>(R.id.button_1)?.setOnClickListener {
            viewModel.setInteger(1)
            showInteger()
        }
        fragmentView?.findViewById<Button>(R.id.button_2)?.setOnClickListener {
            viewModel.setInteger(2)
            showInteger()
        }
        fragmentView?.findViewById<Button>(R.id.button_3)?.setOnClickListener {
            viewModel.setInteger(3)
            showInteger()
        }
        fragmentView?.findViewById<Button>(R.id.button_4)?.setOnClickListener {
            viewModel.setInteger(4)
            showInteger()
        }
        fragmentView?.findViewById<Button>(R.id.button_5)?.setOnClickListener {
            viewModel.setInteger(5)
            showInteger()
        }
        fragmentView?.findViewById<Button>(R.id.button_6)?.setOnClickListener {
            viewModel.setInteger(6)
            showInteger()
        }
        fragmentView?.findViewById<Button>(R.id.button_7)?.setOnClickListener {
            viewModel.setInteger(7)
            showInteger()
        }
        fragmentView?.findViewById<Button>(R.id.button_8)?.setOnClickListener {
            viewModel.setInteger(8)
            showInteger()
        }
        fragmentView?.findViewById<Button>(R.id.button_9)?.setOnClickListener {
            viewModel.setInteger(9)
            showInteger()
        }
    }

    private fun setupCalculateTypeKeys() {
        fragmentView?.findViewById<Button>(R.id.button_addition)?.setOnClickListener {
            if (viewModel.currentDisplayLeftInteger.isBlank()) return@setOnClickListener
            viewModel.currentCalculateType = CalculatorViewModel.CalculateType.ADDITION
            showOperator()
        }
        fragmentView?.findViewById<Button>(R.id.button_subtraction)?.setOnClickListener {
            if (viewModel.currentDisplayLeftInteger.isBlank()) return@setOnClickListener
            viewModel.currentCalculateType = CalculatorViewModel.CalculateType.SUBTRACTION
            showOperator()
        }
        fragmentView?.findViewById<Button>(R.id.button_multiplication)?.setOnClickListener {
            if (viewModel.currentDisplayLeftInteger.isBlank()) return@setOnClickListener
            viewModel.currentCalculateType = CalculatorViewModel.CalculateType.MULTIPLICATION
            showOperator()
        }
        fragmentView?.findViewById<Button>(R.id.button_division)?.setOnClickListener {
            if (viewModel.currentDisplayLeftInteger.isBlank()) return@setOnClickListener
            viewModel.currentCalculateType = CalculatorViewModel.CalculateType.DIVISION
            showOperator()
        }
    }

    private fun setupEqualKey() {
        fragmentView?.findViewById<Button>(R.id.button_equal)?.setOnClickListener {
            val resultView = fragmentView?.findViewById<TextView>(R.id.calculate_view)
            viewModel.calculate(
                complete = {
                    resultView?.text = it
                }, error = {
                    resultView?.text = ""
                }
            )
            viewModel.clearCalculateData()
        }
    }

    private fun setupClearKey() {
        fragmentView?.findViewById<Button>(R.id.button_clear)?.setOnClickListener {
            val resultView = fragmentView?.findViewById<TextView>(R.id.calculate_view)
                ?: return@setOnClickListener
            resultView.text = ""
            viewModel.clearCalculateData()
        }
    }

    /**
     * 数字キーが押された際に呼び出され入力された数値を表示する
     * 演算子が入力済みの場合は演算子も表示する
     */
    private fun showInteger() {
        val resultView = fragmentView?.findViewById<TextView>(R.id.calculate_view) ?: return

        if (viewModel.currentCalculateType == null) {
            viewModel.currentDisplayLeftInteger = viewModel.leftInteger.toString()
            resultView.text = viewModel.currentDisplayText
        } else {
            viewModel.currentDisplayRightInteger = viewModel.rightInteger.toString()
            resultView.text = viewModel.currentDisplayText
        }
    }

    private fun showOperator() {
        val resultView = fragmentView?.findViewById<TextView>(R.id.calculate_view) ?: return
        resultView.text = viewModel.currentDisplayText
    }

    override fun notifyIntOverFlow() {
        toast?.cancel()
        toast = Toast.makeText(context, "Intの最大値をこえるため入力できません。", Toast.LENGTH_SHORT)
        toast?.show()
    }

    override fun notifyInvalidFormula() {
        val resultView = fragmentView?.findViewById<TextView>(R.id.calculate_view) ?: return
        resultView.text = ""
        viewModel.clearCalculateData()
        Toast.makeText(context, "無効な計算式です。", Toast.LENGTH_SHORT).show()
    }
}