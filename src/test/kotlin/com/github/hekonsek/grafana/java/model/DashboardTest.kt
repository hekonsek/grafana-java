package com.github.hekonsek.grafana.java.model

import com.github.hekonsek.grafana.java.model.Row.Companion.rowWithGraph
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*

class DashboardTest {

    companion object {
        val title = UUID.randomUUID().toString()
        val alias = UUID.randomUUID().toString()
        val datasource = UUID.randomUUID().toString()
    }

    @Test
    fun shouldAddTargetToGraph() {
        // Given
        val dashboard = Dashboard.emptyDashboard(title)
        val row = rowWithGraph(title)
        dashboard.rows.add(row)
        val graph = dashboard.rows.first().panels.first() as Graph

        // When
        graph.targets.add(GraphTarget.newGraphTarget(alias, datasource))

        // Then
        val dashboardJson = dashboard.model()["dashboard"] as Map<String, Any>
        val rows = dashboardJson["rows"] as List<Map<String, Any>>
        val panels = rows.first()["panels"] as List<Map<String, Any>>
        val targets = panels.first()["targets"]as List<Map<String, Any>>
        assertThat(targets).hasSize(1)
    }

}