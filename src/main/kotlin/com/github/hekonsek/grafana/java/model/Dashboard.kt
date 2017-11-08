package com.github.hekonsek.grafana.java.model

import com.github.hekonsek.grafana.java.model.Panel.Companion.parsePanel
import json4dummies.Json

class Dashboard(val innerModel: MutableMap<String, Any>, val rows : MutableList<Row> = mutableListOf()) {

    companion object {
        fun emptyDashboard(title: String): Dashboard {
            val dashboard = Json.fromJson(javaClass.getResourceAsStream("/dashboard.json").readBytes())
            val dashboardDefinition = dashboard["dashboard"] as Map<String, Any>
            return Dashboard((dashboard + ("dashboard" to dashboardDefinition.plus("title" to title))).toMutableMap())
        }

        fun parseDashboard(model: Map<String, Any>) : Dashboard {
            val innerModel = HashMap(model)
            val definition = innerModel["dashboard"] as MutableMap<String, Any>
            val rows = (definition["rows"] as List<Map<String, Any>>).map { parseRow(it.toMutableMap()) }.toMutableList()
            definition.remove("rows")
            return Dashboard(innerModel, rows)
        }
    }

    fun model() : Map<String, Any> {
        val result = HashMap(innerModel)
        val definition = result["dashboard"] as MutableMap<String, Any>
        definition["rows"] = rows.map { it.model() }
        result["overwrite"] = true
        return result
    }

}

class Row(val innerModel: MutableMap<String, Any>, val panels : MutableList<Panel> = mutableListOf()) {

    fun model() : Map<String, Any> {
        val result = HashMap(innerModel)
        result["panels"] = panels.map { it.model() }
        return result
    }

}

fun parseRow(model: Map<String, Any>) : Row {
    val innerModel = HashMap(model)
    val panels = (innerModel["panels"] as List<Map<String, Any>>).map { parsePanel(it.toMutableMap()) }.toMutableList()
    innerModel.remove("panels")
    return Row(innerModel, panels)
}