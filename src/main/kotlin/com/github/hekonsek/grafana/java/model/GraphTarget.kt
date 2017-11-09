package com.github.hekonsek.grafana.java.model

import json4dummies.Json

class GraphTarget(val innerModel: MutableMap<String, Any>) {

    companion object {
        fun parseGraphTarget(model: Map<String, Any>) : GraphTarget {
            return GraphTarget(HashMap(model))
        }

        fun newGraphTarget(alias: String, dataSource: String): GraphTarget {
            val targetModel = Json.fromJson(javaClass.getResourceAsStream("/dashboard-target.json").readBytes()).toMutableMap()
            targetModel["alias"] = alias
            targetModel["datasource"] = dataSource
            return GraphTarget.parseGraphTarget(targetModel)
        }
    }

    // JSON model generation

    fun model() : Map<String, Any> = innerModel

}