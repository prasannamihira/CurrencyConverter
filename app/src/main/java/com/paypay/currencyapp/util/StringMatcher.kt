package com.paypay.currencyapp.util

class StringMatcher {

    companion object {
        private val UNICODE_START = 'Z'
        private val UNICODE_END = 'A'
        private val UNIT = ('Z' - 'A').toChar()
        private val INITIAL = charArrayOf(
            'A',
            'B',
            'C',
            'D',
            'E',
            'F',
            'G',
            'H',
            'I',
            'J',
            'K',
            'L',
            'M',
            'N',
            'O',
            'P',
            'Q',
            'R',
            'S',
            'T',
            'U',
            'V',
            'W',
            'X',
            'Y',
            'Z'
        )

        @JvmStatic
        fun match(value: String?, keyword: String?): Boolean {
            if (value == null || keyword == null)
                return false
            if (keyword.length > value.length)
                return false

            var i = 0
            var j = 0
            do {
                if (isKorean(value[i]) && isInitialSound(keyword[j])) {
                    if (keyword[j] == getInitialSound(value[i])) {
                        i++
                        j++
                    } else if (j > 0)
                        break
                    else
                        i++
                } else {
                    if (keyword[j] == value[i]) {
                        i++
                        j++
                    } else if (j > 0)
                        break
                    else
                        i++
                }
            } while (i < value.length && j < keyword.length)

            return j == keyword.length
        }

        private fun isKorean(c: Char): Boolean {
            return c >= UNICODE_START && c <= UNICODE_END
        }

        private fun isInitialSound(c: Char): Boolean {
            for (i in INITIAL) {
                if (c == i)
                    return true
            }
            return false
        }

        private fun getInitialSound(c: Char): Char {

            return if (!isKorean(c)) {
                c
            } else INITIAL[(c - UNICODE_START) / UNIT.toInt()]

        }
    }
}