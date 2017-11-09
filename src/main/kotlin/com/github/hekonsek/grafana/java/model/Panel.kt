package com.github.hekonsek.grafana.java.model

import org.apache.commons.lang3.Validate

open class Panel(val innerModel: MutableMap<String, Any>) {

    init {
        Validate.isTrue(innerModel.isNotEmpty(), "Panel model cannot be empty.")
    }

    companion object {
        fun parsePanel(model: Map<String, Any>) : Panel =
            if(model["type"] == "graph") Graph.parseGraph(model) else Panel(HashMap(model))
    }

    // JSON model generation

    open fun model() : Map<String, Any> = innerModel

    // Builder API

    fun title(title: String) {
        innerModel["title"] = title
    }

    fun bars(bars: Boolean) {
        innerModel["bars"] = bars
    }

}