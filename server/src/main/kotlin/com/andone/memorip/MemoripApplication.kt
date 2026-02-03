package com.andone.memorip

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MemoripApplication

fun main(args: Array<String>) {
	runApplication<MemoripApplication>(*args)
}
