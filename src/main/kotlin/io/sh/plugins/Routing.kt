package io.sh.plugins

import io.ktor.server.routing.*
import io.ktor.server.http.content.*
import io.ktor.server.application.*
import io.sh.ChartService
import java.util.concurrent.TimeUnit

fun Application.configureRouting() {

    routing {
        // Static plugin. Try to access `/static/index.html`
        static("/") {
            ChartService.renderChart()
            TimeUnit.SECONDS.sleep(5)
            resources("static")
        }
    }
}
