package com.andone.memorip

import android.app.Application
import com.andone.memorip.domain.auth.TokenRefresher
import com.andone.memorip.domain.repository.AuthRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class MemoripApplication : Application()