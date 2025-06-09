package com.daccvo.utils

import com.itextpdf.kernel.colors.DeviceRgb

fun hexToDeviceRgb(hex: String): DeviceRgb {
    val cleanHex = hex.removePrefix("#")
    require(cleanHex.length == 6) { "Hex color must be 6 characters long" }

    val r = cleanHex.substring(0, 2).toInt(16)
    val g = cleanHex.substring(2, 4).toInt(16)
    val b = cleanHex.substring(4, 6).toInt(16)

    return DeviceRgb(r, g, b)
}

