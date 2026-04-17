package com.app.neura.data.repository

import android.content.Context
import com.app.neura.data.local.AssetChallengeDataSource
import com.app.neura.data.model.Challenge

class ChallengeRepository(
    context: Context
) {
    private val dataSource = AssetChallengeDataSource(context)

    fun getChallenges(): List<Challenge> {
        return dataSource.loadChallenges()
    }
}