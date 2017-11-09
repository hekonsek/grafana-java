package com.github.hekonsek.grafana.java.model

class Target(val innerModel: MutableMap<String, Any>) {

    companion object {
        fun parseTarget(model: Map<String, Any>) : Target {
            return Target(HashMap(model))
        }
    }

    // JSON model generation

    fun model() : Map<String, Any> = innerModel

}