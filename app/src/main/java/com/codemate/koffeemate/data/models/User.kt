/*
 * Copyright 2017 Codemate Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codemate.koffeemate.data.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

class UserListResponse {
    var members = listOf<User>()
}

fun List<User>.isFreshEnough(maxStaleness: Long): Boolean {
    val oldestAcceptedTimestamp = System.currentTimeMillis() - maxStaleness
    val sorted = sortedByDescending(User::last_updated)
    val freshestUser = sorted.first()

    return freshestUser.last_updated > oldestAcceptedTimestamp
}

open class User(
        @PrimaryKey
        open var id: String = "",
        open var name: String = "",
        open var profile: Profile = Profile(),
        open var real_name: String? = null,
        open var is_bot: Boolean = false,
        open var deleted: Boolean = false,
        open var last_updated: Long = 0
) : RealmObject() {
    override fun equals(other: Any?): Boolean {
        if (other is User) {
            return id == other.id
        }

        return this == other
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + profile.hashCode()
        result = 31 * result + (real_name?.hashCode() ?: 0)
        result = 31 * result + is_bot.hashCode()
        result = 31 * result + deleted.hashCode()
        result = 31 * result + last_updated.hashCode()
        return result
    }
}