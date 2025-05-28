package com.daccvo.models

sealed class Endpoint( val path : String){
    data object Root : Endpoint(path = "/")
    data object GeneretePdf : Endpoint(path = "/generetepdf")

}