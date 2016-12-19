package com.codemate.koffeemate.ui.main

import com.codemate.koffeemate.data.BrewingProgressUpdater
import com.codemate.koffeemate.data.local.CoffeeEventRepository
import com.codemate.koffeemate.data.local.CoffeePreferences
import com.codemate.koffeemate.data.network.SlackApi
import com.codemate.koffeemate.ui.base.BasePresenter
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MainPresenter @Inject constructor(
        val coffeePreferences: CoffeePreferences,
        val coffeeEventRepository: CoffeeEventRepository,
        val brewingProgressUpdater: BrewingProgressUpdater,
        val slackApi: SlackApi
) : BasePresenter<MainView>() {
    fun startDelayedCoffeeAnnouncement(newCoffeeMessage: String) {
        ensureViewIsAttached()

        if (!brewingProgressUpdater.isUpdating
                && !coffeePreferences.isCoffeeAnnouncementChannelSet()) {
            getView()?.noAnnouncementChannelSet()
            return
        }

        if (!brewingProgressUpdater.isUpdating) {
            getView()?.newCoffeeIsComing()

            brewingProgressUpdater.startUpdating(
                    updateListener = { progress ->
                        getView()?.updateCoffeeProgress(progress)
                    },
                    completeListener = {
                        val channel = coffeePreferences.getCoffeeAnnouncementChannel()

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

                        updateLastBrewingEventTime()
                    }
            )
        } else {
            getView()?.showCancelCoffeeProgressPrompt()
        }
    }

    fun updateLastBrewingEventTime() {
        coffeeEventRepository.getLastBrewingEvent()?.let {
            getView()?.setLastBrewingEvent(it)
        }
    }

    fun cancelCoffeeCountDown() {
        ensureViewIsAttached()

        getView()?.updateCoffeeProgress(0)
        getView()?.resetCoffeeViewStatus()

        brewingProgressUpdater.reset()
    }

    fun launchUserSelector() {
        if (!coffeePreferences.isAccidentChannelSet()) {
            getView()?.noAccidentChannelSet()
        } else {
            getView()?.launchUserSelector()
        }
    }
}