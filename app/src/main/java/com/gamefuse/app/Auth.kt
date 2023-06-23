package com.gamefuse.app

class Connect{
    companion object{
        @JvmStatic lateinit var authToken: String
        @JvmStatic
        var list_friends: MutableList<String> = mutableListOf()
    }
}