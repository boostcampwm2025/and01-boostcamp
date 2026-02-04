package com.andone.memorip.data.ai

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ToxicityClassifierTest {

    private lateinit var classifier: ToxicityClassifier

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        classifier = ToxicityClassifier(context)
    }

    @Test
    fun toxic_sentences_should_be_detected() {
        val toxicSamples = listOf(
            "개새끼",
            "씨발",
            "존나 짜증나",
            "병신 같은 소리하네",
            "꺼져라",
            "니가 제일 멍청함",
            "쓰레기 같은 인간",
            "뒤져라",
            "개같은 놈",
            "씨@발",
        )

        toxicSamples.forEach {
            val result = classifier.predict(it)
            println("toxic [$it] -> $result")
            assertTrue(result.isNotEmpty())
        }
    }

    @Test
    fun clean_sentences_should_not_be_flagged() {
        val cleanSamples = listOf(
            "오늘 날씨가 좋네요",
            "감사합니다",
            "좋은 하루 보내세요",
            "수고 많으셨습니다",
            "예쁜 카페입니다",
            "행복한 여행이었어요",
            "정말 친절하시네요",
            "도움이 많이 됐어요",
            "천천히 해도 괜찮아요",
            "착한 말",
            "데이트 하기 좋은 곳입니다",
            "과자",
            "손흥민",
            "테스트요",
            "권동현",
            "서호준",
            "임현정",
            "세종대왕",
            "이순신",
            "홍원택"
        )

        cleanSamples.forEach {
            val result = classifier.predict(it)
            println("clean [$it] -> $result")
            assertTrue(result.isEmpty())
        }
    }
}
