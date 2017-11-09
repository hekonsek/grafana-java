# Grafana client for Java

[![Version](https://img.shields.io/badge/Grafana%20Java-0.1-blue.svg)](https://github.com/hekonsek/grafana-java/releases)
[![Build](https://api.travis-ci.org/hekonsek/grafana-java.svg)](https://travis-ci.org/hekonsek/grafana-java)

**Grafana Java** provides Java client for [Grafana REST API](http://docs.grafana.org/http_api/).

## Maven setup

    <dependency>
      <groupId>com.github.hekonsek</groupId>
      <artifactId>grafana-java</artifactId>
      <version>0.1</version>
    </dependency>

## Usage

First of all create Grafana client instance:

```
Grafana grafana = new Grafana(new BasicAuthentication("admin", "admin"));
```

Then create dashboard you would like to save:

```
Dashboard dashboard = emptyDashboard(name);
Row row = rowWithGraph(name);
dashboard.getRows().add(row);
Graph graph = (Graph) dashboard.getRows().get(0).getPanels().get(0);
graph.getTargets().add(newGraphTarget(name, name));
```

And save it:

```
String dashboardId = grafana.saveDashboard(dashboard);
```