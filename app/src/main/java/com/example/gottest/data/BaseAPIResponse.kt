package com.example.gottest.data

import com.squareup.moshi.Json

data class BaseAPIResponse(
    @field:Json(name = "url") val url: String,
    @field:Json(name = "name") val name: String
) 