package com.github.hekonsek.grafana.java.model

class Graph(innerModel: MutableMap<String, Any>, val targets : MutableList<GraphTarget>) : Panel(innerModel) {

    companion object {
        fun parseGraph(model: Map<String, Any>) : Graph {
            val targets = (model["targets"] as List<Map<String, Any>>).map { GraphTarget(it.toMutableMap()) }.toMutableList()
            return Graph(HashMap(model), targets)
        }
    }

    override fun model() : Map<String, Any> {
        val model = HashMap(innerModel)
        model["targets"] = targets
        return model
    }

}