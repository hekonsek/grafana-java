package com.github.hekonsek.grafana.java.model

import json4dummies.Json.Companion.fromJson

class GrafanaModelTemplates {

    fun elasticSearchDataSource(name: String, index: String, timeField: String): Map<String, Any> {
        val template = fromJson(javaClass.getResourceAsStream("/datasource-elasticsearch.json").readBytes())
                .plus("name" to name).plus("database" to index)
        val jsonData = template["jsonData"] as Map<String, Any>
        return template.plus(Pair("jsonData", jsonData.plus(Pair("timeField", timeField))))
    }

    fun graphiteDataSource(name: String, url: String): Map<String, Any> =
            fromJson(javaClass.getResourceAsStream("/datasource-graphite.json").readBytes()) + ("name" to name) + ("url" to url)

    fun dashboardRowWithGraph(title: String): Row {
        val rowModel = fromJson(javaClass.getResourceAsStream("/dashboard-row.json").readBytes()).toMutableMap()
        val panelModel = fromJson(javaClass.getResourceAsStream("/dashboard-panel-graph.json").readBytes()).toMutableMap()

        val row = Row(rowModel, mutableListOf(Panel(panelModel)))
        row.panels.first().title(title)
        return row
    }

}
