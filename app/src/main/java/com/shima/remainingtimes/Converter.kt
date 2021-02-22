package com.shima.remainingtimes

import androidx.databinding.InverseMethod

object Converter {
    @JvmStatic
    @InverseMethod("toIntTime")
    fun toStringTime(int: Int): String {
        val h = int / 60
        val m = int % 60
        return "%1$02d:%2$02d".format(h, m)
    }

    @JvmStatic
    fun toIntTime(string: String): Int {
        val array = string.split(":")
        val hour = (Integer.parseInt(array[0]) * 60)
        val minute = Integer.parseInt(array[1])
        return (hour + minute)
    }
}
