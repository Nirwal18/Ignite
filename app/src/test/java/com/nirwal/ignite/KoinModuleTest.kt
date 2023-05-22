package com.nirwal.ignite

import com.nirwal.ignite.di.appModule
import org.koin.test.KoinTest
import org.koin.test.verify.verify
import kotlin.test.Test

class KoinModuleTest:KoinTest {
    @Test
    fun checkAllModules() {
        appModule.verify()
    }
}