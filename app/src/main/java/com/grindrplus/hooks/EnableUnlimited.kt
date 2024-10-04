package com.grindrplus.hooks

import com.grindrplus.GrindrPlus
import com.grindrplus.utils.Hook
import com.grindrplus.utils.HookStage
import com.grindrplus.utils.hook

class EnableUnlimited : Hook(
    "Enable unlimited",
    "Enable Grindr Unlimited features"
) {
    private val userSession = "com.grindrapp.android.storage.b"
    private val subscribeToInterstitialsList = listOf(
        "q8.p\$a" // Chat ($1)
    )

    override fun init() {
        val userSessionClass = findClass(userSession)

        userSessionClass.hook( // hasFeature()
            "E", HookStage.BEFORE
        ) { param ->
            val disallowedFeatures = setOf("DisableScreenshot")
            param.setResult(param.arg(0, String::class.java) !in disallowedFeatures)
        }

        userSessionClass.hook( // isNoXtraUpsell()
            "k", HookStage.BEFORE
        ) { param ->
            param.setResult(true)
        }

        userSessionClass.hook( // isNoPlusUpsell()
            "D", HookStage.BEFORE
        ) { param ->
            param.setResult(true)
        }

        userSessionClass.hook( // isFree()
            "v", HookStage.BEFORE
        ) { param ->
            param.setResult(false)
        }

        userSessionClass.hook( // isFreeXtra()
            "t", HookStage.BEFORE
        ) { param ->
            param.setResult(false)
        }

        userSessionClass.hook( // isFreePlus()
            "B", HookStage.BEFORE
        ) { param ->
            param.setResult(false)
        }

        userSessionClass.hook( // isFreeUnlimited()
            "A", HookStage.BEFORE
        ) { param ->
            param.setResult(true)
        }

        subscribeToInterstitialsList.forEach {
            findClass(it)
                .hook("emit", HookStage.BEFORE) { param ->
                    val modelName = param.arg<Any>(0)::class.java.name
                    if (!modelName.contains("NoInterstitialCreated")
                        && !modelName.contains("OnInterstitialDismissed")
                    ) {
                        param.setResult(null)
                    }
                }
        }
    }
}