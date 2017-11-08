package com.github.hekonsek.grafana.java.model

import org.apache.commons.lang3.Validate

class Panel(val innerModel: MutableMap<String, Any>) {

    init {
        Validate.isTrue(innerModel.isNotEmpty(), "Panel model cannot be empty.")
    }

    companion object {
        fun parsePanel(model: Map<String, Any>) : Panel {
            return Panel(HashMap(model))
        }
    }

    // JSON model generation

    fun model() : Map<String, Any> = innerModel

    // Builder API

    fun title(title: String) {
        innerModel["title"] = title
    }

    fun bars(bars: Boolean) {
        innerModel["bars"] = bars
    }

}