package com.example.gottest.ui.extension

fun List<String>.getSeparated(): String {
    var result = String()
    map { result += it; if (last() != it) result += ", " }
    return result
}