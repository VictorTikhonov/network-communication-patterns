package ru.tikhonov.utils

import io.github.oshai.kotlinlogging.KotlinLogging
import ru.tikhonov.benchmark.Benchmark
import ru.tikhonov.benchmark.GrpcBenchmark

private val logger = KotlinLogging.logger {}


fun startBenchmark(benchmark: Benchmark) {

    while (true) {
        val clientCount = readPositiveInt("Количество клиентов: ")
        if (clientCount == null) {
            logger.warn { "Некорректный ввод, прогон пропущен" }
            continue
        }

        val messagesPerClient = readPositiveInt("Сообщений на клиента: ")
        if (messagesPerClient == null) {
            logger.warn { "Некорректный ввод, прогон пропущен" }
            continue
        }

        benchmark.start(
            clientCount = clientCount,
            messagesPerClient = messagesPerClient,
        )

        if (!askContinue()) {
            logger.info { "Завершение" }
            break
        }
    }
}

fun startGrpcBenchmark(benchmark: GrpcBenchmark) {
    while (true) {
        val messagesPerClient = readPositiveInt("Количество сообщений в потоке: ")
        if (messagesPerClient == null) {
            logger.warn { "Некорректный ввод, прогон пропущен" }
            continue
        }

        logger.info {
            "\n\n\nПоток из $messagesPerClient сообщений"
        }

        benchmark.start(messagesPerClient)

        if (!askContinue()) {
            logger.info { "Завершение" }
            break
        }
    }
}

private fun readPositiveInt(prompt: String): Int? {
    print(prompt)
    val line = readlnOrNull()?.trim() ?: return null
    return line.toIntOrNull()?.takeIf { it > 0 }
}

private fun askContinue(): Boolean {
    print("Продолжить? (y/n): ")
    val answer = readlnOrNull()?.trim()?.lowercase() ?: return false
    return answer == "y" || answer == "yes" || answer == "д" || answer == "да"
}