package com.github.hekonsek.grafana.java

import json4dummies.Json.Companion.fromJson

class GrafanaTemplates {

    fun elasticSearchDataSource(name: String, index: String, timeField: String): Map<String, Any> {
        val template = fromJson(javaClass.getResourceAsStream("/datasource-elasticsearch.json").readBytes())
                .plus(Pair("name", name)).plus(Pair("database", index))
        val jsonData = template.getValue("jsonData") as Map<String, Any>
        return template.plus(Pair("jsonData", jsonData.plus(Pair("timeField", timeField))))
    }

    fun graphiteDataSource(name: String, url: String): Map<String, Any> =
            fromJson(javaClass.getResourceAsStream("/datasource-graphite.json").readBytes())
                    .plus(Pair("name", name)).plus(Pair("url", url))


}
