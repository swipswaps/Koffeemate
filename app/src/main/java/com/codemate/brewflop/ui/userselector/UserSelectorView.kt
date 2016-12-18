/*
 * Copyright 2016 Codemate Ltd
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

package com.codemate.brewflop.ui.userselector

import com.codemate.brewflop.data.network.models.User
import com.codemate.brewflop.ui.base.MvpView

interface UserSelectorView : MvpView {
    fun showUsers(users: List<User>)
    fun showProgress()
    fun hideProgress()
    fun showError()
    fun messagePostedSuccessfully()
    fun errorPostingMessage()
}