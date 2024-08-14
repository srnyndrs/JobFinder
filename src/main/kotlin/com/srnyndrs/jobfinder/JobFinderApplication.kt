package com.srnyndrs.jobfinder

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JobFinderApplication

fun main(args: Array<String>) {
	runApplication<JobFinderApplication>(*args)
}
