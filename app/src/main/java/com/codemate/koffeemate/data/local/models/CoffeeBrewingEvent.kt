package com.codemate.koffeemate.data.local.models

import com.codemate.koffeemate.data.network.models.User
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class CoffeeBrewingEvent(
        @PrimaryKey
        open var id: String = "",
        open var time: Long = 0,
        open var isSuccessful: Boolean = false,
        open var user: User? = null
) : RealmObject()