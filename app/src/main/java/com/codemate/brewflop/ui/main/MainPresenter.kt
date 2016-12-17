package com.codemate.brewflop.ui.main

import com.codemate.brewflop.data.BrewingProgressUpdater
import com.codemate.brewflop.data.local.CoffeePreferences
import com.codemate.brewflop.data.local.CoffeeEventRepository
import com.codemate.brewflop.data.network.SlackApi
import com.codemate.brewflop.ui.base.BasePresenter
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainPresenter(
        private val coffeePreferences: CoffeePreferences,
        private val coffeeEventRepository: CoffeeEventRepository,
        private val brewingProgressUpdater: BrewingProgressUpdater,
        private val slackApi: SlackApi
) : BasePresenter<MainView>() {
    fun startDelayedCoffeeAnnouncement(newCoffeeMessage: String) {
        ensureViewIsAttached()

        if (!brewingProgressUpdater.isUpdating
                && !coffeePreferences.isChannelNameSet()) {
            getView()?.noChannelNameSet()
            return
        }

        if (!brewingProgressUpdater.isUpdating) {
            getView()?.newCoffeeIsComing()

            brewingProgressUpdater.startUpdating(
                    updateListener = { progress ->
                        getView()?.updateCoffeeProgress(progress)
                    },
                    completeListener = {
                        val channel = coffeePreferences.getChannelName()

                        slackApi.postMessage(
                                channel,
                                newCoffeeMessage
                        ).enqueue(object : Callback<ResponseBody>{
                            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                                // TODO: Something. Do nothing for now.
                            }

                            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                                t?.printStackTrace()
                            }
                        })

                        getView()?.resetCoffeeViewStatus()
                        coffeeEventRepository.recordBrewingEvent()
                    }
            )
        } else {
            getView()?.showCancelCoffeeProgressPrompt()
        }
    }

    fun cancelCoffeeCountDown() {
        ensureViewIsAttached()

        getView()?.updateCoffeeProgress(0)
        getView()?.resetCoffeeViewStatus()

        brewingProgressUpdater.reset()
    }
}