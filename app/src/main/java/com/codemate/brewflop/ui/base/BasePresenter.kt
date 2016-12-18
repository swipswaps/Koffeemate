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

package com.codemate.brewflop.ui.base

open class BasePresenter<T: MvpView> : Presenter<T> {
    private var mvpView: T? = null

    override fun attachView(mvpView: T) {
        this.mvpView = mvpView
    }

    override fun detachView() {
        mvpView = null
    }

    fun getView(): T? {
        return mvpView
    }

    fun ensureViewIsAttached() {
        if (!isViewAttached()) {
            throw ViewNotAttachedException()
        }
    }

    fun isViewAttached(): Boolean {
        return mvpView != null
    }

    class ViewNotAttachedException : RuntimeException("View not attached! Please call attachView() first.")
}