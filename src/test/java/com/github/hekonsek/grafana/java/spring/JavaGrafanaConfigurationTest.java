package com.github.hekonsek.grafana.java.spring;

import com.github.hekonsek.grafana.java.BasicAuthentication;
import com.github.hekonsek.grafana.java.Grafana;
import com.github.hekonsek.grafana.java.model.Dashboard;
import com.github.hekonsek.grafana.java.model.Graph;
import com.github.hekonsek.grafana.java.model.Row;
import com.github.hekonsek.spring.boot.docker.spotify.HttpReadinessProbe;
import com.github.hekonsek.spring.boot.docker.spotify.NamedContainer;
import com.github.hekonsek.spring.boot.docker.spotify.SpotifyDockerAutoConfiguration;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.HostConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static com.github.hekonsek.grafana.java.model.Dashboard.emptyDashboard;
import static com.github.hekonsek.grafana.java.model.GraphTarget.newGraphTarget;
import static com.github.hekonsek.grafana.java.model.Row.rowWithGraph;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {GrafanaConfiguration.class, SpotifyDockerAutoConfiguration.class, JavaGrafanaConfigurationTest.class})
public class JavaGrafanaConfigurationTest {

    @Autowired
    Grafana grafana;

    String name = UUID.randomUUID().toString();

    @Bean
    NamedContainer grafanaContainer() {
        HostConfig hostConfig = HostConfig.builder().networkMode("host").build();
        ContainerConfig containerConfig = ContainerConfig.builder().image("grafana/grafana").hostConfig(hostConfig).build();
        return new NamedContainer("grafana", containerConfig, new HttpReadinessProbe("http://localhost:3000"));
    }

    // Data source operations tests

    @Test
    public void shouldCreateGrafanaClient() {
        Grafana grafana = new Grafana(new BasicAuthentication("admin", "admin"));
        assertThat(grafana).isNotNull();
    }


    @Test
    public void shouldAddTargetToGraph() {
        // Given
        Dashboard dashboard = emptyDashboard(name);
        Row row = rowWithGraph(name);
        dashboard.getRows().add(row);
        Graph graph = (Graph) dashboard.getRows().get(0).getPanels().get(0);
        graph.getTargets().add(newGraphTarget(name, name));

        // When
        String id = grafana.saveDashboard(dashboard);

        // Then
        dashboard = grafana.readDashboard(id);
        graph = (Graph) dashboard.getRows().get(0).getPanels().get(0);
        assertThat(graph.getTargets()).hasSize(1);
    }

}