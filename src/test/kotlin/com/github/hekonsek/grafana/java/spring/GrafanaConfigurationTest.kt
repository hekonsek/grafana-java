package com.github.hekonsek.grafana.java.spring

import com.github.hekonsek.grafana.java.Grafana
import com.github.hekonsek.grafana.java.model.Dashboard.Companion.emptyDashboard
import com.github.hekonsek.grafana.java.model.GrafanaModelTemplates
import com.github.hekonsek.grafana.java.model.Graph
import com.github.hekonsek.grafana.java.model.GraphTarget
import com.github.hekonsek.grafana.java.model.Row.Companion.rowWithGraph
import com.github.hekonsek.spring.boot.docker.spotify.HttpReadinessProbe
import com.github.hekonsek.spring.boot.docker.spotify.NamedContainer
import com.spotify.docker.client.messages.ContainerConfig
import com.spotify.docker.client.messages.HostConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.test.context.junit4.SpringRunner
import java.util.UUID.randomUUID

@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(GrafanaConfiguration::class, GrafanaConfigurationTest::class))
open class GrafanaConfigurationTest {

    @Autowired
    lateinit var grafana : Grafana

    val modelTemplates = GrafanaModelTemplates()

    val name = randomUUID().toString()

    val index = randomUUID().toString()

    val timestampField = randomUUID().toString()

    @Bean
    open fun grafanaContainer() : NamedContainer {
        val hostConfig = HostConfig.builder().networkMode("host").build()
        val containerConfig = ContainerConfig.builder().image("grafana/grafana").hostConfig(hostConfig).build()
        return NamedContainer("grafana", containerConfig, HttpReadinessProbe("http://localhost:3000"))
    }

    // Data source operations tests

    @Test
    fun shouldCreateElasticSearchDataSource() {
        // Given
        val dataSource = modelTemplates.elasticSearchDataSource(name, index, timestampField)

        // When
        grafana.saveDataSource(dataSource)

        // Then
        val fetchedDataSource = grafana.listDataSources().find { it["name"] == name }
        assertThat(fetchedDataSource).isNotNull
    }

    @Test
    fun shouldReadElasticSearchDataSource() {
        // Given
        val dataSource = modelTemplates.elasticSearchDataSource(name, index, timestampField)

        // When
        grafana.saveDataSource(dataSource)

        // Then
        val fetchedDataSource = grafana.listDataSources().find { it["name"] == name }
        assertThat(fetchedDataSource).isNotNull
    }

    @Test
    fun shouldDeleteElasticSearchDataSource() {
        // Given
        val dataSource = modelTemplates.elasticSearchDataSource(name, index, timestampField)
        val id = grafana.saveDataSource(dataSource)

        // When
        val fetchedDataSource = grafana.readDataSource(id)

        // Then
        assertThat(fetchedDataSource).isNotNull
    }

    @Test
    fun shouldCreateGraphiteDataSource() {
        // Given
        val dataSource = modelTemplates.graphiteDataSource(name, "http://localhost:1234")

        // When
        val id = grafana.saveDataSource(dataSource)

        // Then
        val fetchedDataSource = grafana.readDataSource(id)
        assertThat(fetchedDataSource).isNotNull
    }

    // Dashboard operations tests

    @Test
    fun shouldCreateDashboard() {
        // Given
        val dashboard = emptyDashboard(name)

        // When
        val id = grafana.saveDashboard(dashboard)

        // Then
        val fetchedDashboard = grafana.readDashboard(id)
        assertThat(fetchedDashboard).isNotNull()
    }

    @Test
    fun shouldAddRowToExistingDashboard() {
        // Given
        val dashboard = emptyDashboard(name)
        val id = grafana.saveDashboard(dashboard)
        var fetchedDashboard = grafana.readDashboard(id)

        // When
        val row = rowWithGraph(name)
        fetchedDashboard.rows.add(row)
        grafana.saveDashboard(fetchedDashboard)

        // Then
        fetchedDashboard = grafana.readDashboard(id)
        assertThat(fetchedDashboard.rows).hasSize(1)
    }

    @Test
    fun shouldAddTargetToGraph() {
        // Given
        val dashboard = emptyDashboard(name)
        val id = grafana.saveDashboard(dashboard)
        var fetchedDashboard = grafana.readDashboard(id)
        val row = rowWithGraph(name)
        fetchedDashboard.rows.add(row)
        var graph = fetchedDashboard.rows.first().panels.first() as Graph
        graph.targets.add(GraphTarget.newGraphTarget(name, name))

        // When
        grafana.saveDashboard(fetchedDashboard)

        // Then
        fetchedDashboard = grafana.readDashboard(id)
        graph = fetchedDashboard.rows.first().panels.first() as Graph
        assertThat(graph.targets).hasSize(1)
    }

}