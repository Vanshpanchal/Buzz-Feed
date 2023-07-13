package com.example.app

import retrofit2.Call
import retrofit2.http.GET

interface api {
    @GET("users")
    fun getUserdata(): Call<User>


}