package com.github.hekonsek.grafana.java.model

import org.junit.Test
import java.lang.IllegalArgumentException

class PanelTest {

    @Test(expected = IllegalArgumentException::class)
    fun shouldValidateEmptyModel() {
        Panel(mutableMapOf())
    }

}