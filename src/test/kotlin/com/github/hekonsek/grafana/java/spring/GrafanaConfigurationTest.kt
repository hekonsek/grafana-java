package com.github.hekonsek.grafana.java.spring

import com.github.hekonsek.grafana.java.Grafana
import com.github.hekonsek.grafana.java.GrafanaTemplates
import com.github.hekonsek.spring.boot.docker.spotify.HttpReadinessProbe
import com.github.hekonsek.spring.boot.docker.spotify.NamedContainer
import com.github.hekonsek.spring.boot.docker.spotify.SpotifyDockerAutoConfiguration
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
@SpringBootTest(classes = arrayOf(GrafanaConfiguration::class, SpotifyDockerAutoConfiguration::class, GrafanaConfigurationTest::class))
open class GrafanaConfigurationTest {

    @Autowired
    lateinit var grafana : Grafana

    @Autowired
    lateinit var grafanaTemplates : GrafanaTemplates


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
        val dataSource = grafanaTemplates.elasticSearchDataSource(name, index, timestampField)

        // When
        grafana.saveDataSource(dataSource)

        // Then
        val fetchedDataSource = grafana.listDataSources().find { it["name"] == name }
        assertThat(fetchedDataSource).isNotNull
    }

    @Test
    fun shouldReadElasticSearchDataSource() {
        // Given
        val dataSource = grafanaTemplates.elasticSearchDataSource(name, index, timestampField)

        // When
        grafana.saveDataSource(dataSource)

        // Then
        val fetchedDataSource = grafana.listDataSources().find { it["name"] == name }
        assertThat(fetchedDataSource).isNotNull
    }

    @Test
    fun shouldDeleteElasticSearchDataSource() {
        // Given
        val dataSource = grafanaTemplates.elasticSearchDataSource(name, index, timestampField)
        val id = grafana.saveDataSource(dataSource)

        // When
        val fetchedDataSource = grafana.readDataSource(id)

        // Then
        assertThat(fetchedDataSource).isNotNull
    }

    @Test
    fun shouldCreateGraphiteDataSource() {
        // Given
        val dataSource = grafanaTemplates.graphiteDataSource(name, "http://localhost:1234")

        // When
        val id = grafana.saveDataSource(dataSource)

        // Then
        val fetchedDataSource = grafana.readDataSource(id)
        assertThat(fetchedDataSource).isNotNull
    }

}