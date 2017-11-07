package com.github.hekonsek.grafana.java

interface Authentication

class ApiKeyAuthentication(val apiKey: String) : Authentication

class BasicAuthentication(val username: String, val password: String) : Authentication