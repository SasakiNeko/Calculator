package com.sasaki.calculator

import androidx.lifecycle.ViewModel

class CalculatorViewModel(private val listener: OnError) : ViewModel() {

    interface OnError {
        fun notifyIntOverFlow()
        fun notifyInvalidFormula()
    }

    enum class CalculateType {
        ADDITION,
        SUBTRACTION,
        MULTIPLICATION,
        DIVISION
    }

    /** 左項の整数 */
    var leftInteger: Int? = null

    /** 右項の整数 */
    var rightInteger: Int? = null

    /** 選択された演算子のタイプ */
    var currentCalculateType: CalculateType? = null

    /** 現在表示されているテキスト */
    var currentDisplayText: String = ""
        get() = currentDisplayLeftInteger + getOperator() + currentDisplayRightInteger

    /** 現在表示されている左項のテキスト */
    var currentDisplayLeftInteger = ""
    /** 現在表示されている右項のテキスト */
    var currentDisplayRightInteger = ""

    /**
     * 計算に使用したデータをクリアする処理
     *
     */
    fun clearCalculateData() {
        leftInteger = null
        rightInteger = null
        currentCalculateType = null
        currentDisplayText = ""
        currentDisplayLeftInteger = ""
        currentDisplayRightInteger = ""
    }

    /**
     * 左項、右項のどちらかに整数をセットする
     *
     * @param number 整数
     */
    fun setInteger(number: Int) {
        if (currentCalculateType == null) setLeftInteger(number)
        else setRightInteger(number)
    }

    /**
     * 計算を行う
     *
     * @param complete 計算が終了したことを伝える
     * @param error 左項、右項のどちらかが入力されていないことを通知する
     */
    fun calculate(complete: (String) -> Unit, error: () -> (Unit)) {
        val unwrapInteger1 = leftInteger ?: return error()
        val unwrapInteger2 = rightInteger ?: return error()

        when (currentCalculateType) {
            CalculateType.ADDITION -> complete(add(unwrapInteger1, unwrapInteger2).toString())
            CalculateType.SUBTRACTION -> complete(subtract(unwrapInteger1, unwrapInteger2).toString())
            CalculateType.MULTIPLICATION -> complete(
                multiply(
                    unwrapInteger1,
                    unwrapInteger2
                ).toString()
            )
            CalculateType.DIVISION -> {
                if (unwrapInteger2 == 0) listener.notifyInvalidFormula()
                else complete(divide(unwrapInteger1, unwrapInteger2).toString())
            }
            else -> {}
        }
    }

    /**
     * 左項に値をセットする
     *
     * @param number 整数
     */
    private fun setLeftInteger(number: Int) {
        if (leftInteger == 0) return
        if (leftInteger == null) {
            leftInteger = number
            return
        }

        if ((leftInteger.toString() + number.toString()).toLong() > Int.MAX_VALUE) listener.notifyIntOverFlow()
        else leftInteger = (leftInteger.toString() + number.toString()).toInt()
    }

    /**
     * 右項に値をセットする
     *
     * @param number 整数
     */
    private fun setRightInteger(number: Int) {
        if (rightInteger == 0) return
        if (rightInteger == null) {
            rightInteger = number
            return
        }

        if ((rightInteger.toString() + number.toString()).toLong() > Int.MAX_VALUE) listener.notifyIntOverFlow()
        else rightInteger = (rightInteger.toString() + number.toString()).toInt()
    }

    private fun getOperator(): String {
        val unwrapCurrentCalculateType = currentCalculateType ?: return ""
        return when (unwrapCurrentCalculateType) {
            CalculateType.ADDITION -> "+"
            CalculateType.SUBTRACTION -> "-"
            CalculateType.MULTIPLICATION -> "×"
            CalculateType.DIVISION -> "÷"
        }
    }

    // 足し算
    private fun add(integer1: Int, integer2: Int): Long {
        return integer1.toLong() + integer2.toLong()
    }

    // 引き算
    private fun subtract(integer1: Int, integer2: Int): Int {
        return integer1 - integer2
    }

    // 掛け算
    private fun multiply(integer1: Int, integer2: Int): Long {
        return integer1.toLong() * integer2.toLong()
    }

    // 割り算
    private fun divide(integer1: Int, integer2: Int): Double {
        return integer1.toDouble() / integer2.toDouble()
    }
}