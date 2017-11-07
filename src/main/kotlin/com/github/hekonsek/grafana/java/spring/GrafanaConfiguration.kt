package com.github.hekonsek.grafana.java.spring

import com.github.hekonsek.grafana.java.Authentication
import com.github.hekonsek.grafana.java.BasicAuthentication
import com.github.hekonsek.grafana.java.Grafana
import com.github.hekonsek.grafana.java.GrafanaTemplates
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class GrafanaConfiguration {

    @Bean
    open fun grafana(authentication: Authentication) : Grafana = Grafana(authentication)

    @Bean
    @ConditionalOnMissingBean
    open fun authentication(@Value("\${grafana.username:admin}") username : String,
                            @Value("\${grafana.password:admin}") password : String) : Authentication =
            BasicAuthentication(username, password)

    @Bean
    open fun grafanaTemplates() : GrafanaTemplates = GrafanaTemplates()

}