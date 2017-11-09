package com.github.hekonsek.grafana.java.model

import json4dummies.Json

class Row(val innerModel: MutableMap<String, Any>, val panels : MutableList<Panel> = mutableListOf()) {

    companion object {
        fun parseRow(model: Map<String, Any>) : Row {
            val innerModel = HashMap(model)
            val panels = (innerModel["panels"] as List<Map<String, Any>>).map { Panel.parsePanel(it.toMutableMap()) }.toMutableList()
            innerModel.remove("panels")
            return Row(innerModel, panels)
        }

        fun rowWithGraph(title: String): Row {
            val rowModel = Json.fromJson(javaClass.getResourceAsStream("/dashboard-row.json").readBytes()).toMutableMap()
            val panelModel = Json.fromJson(javaClass.getResourceAsStream("/dashboard-panel-graph.json").readBytes()).toMutableMap()

            val row = Row(rowModel, mutableListOf(Panel.parsePanel(panelModel)))
            row.panels.first().title(title)
            return row
        }
    }

    fun model() : Map<String, Any> {
        val result = HashMap(innerModel)
        result["panels"] = panels.map { it.model() }
        return result
    }

}