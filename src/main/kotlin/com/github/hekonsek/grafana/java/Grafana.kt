package com.github.hekonsek.grafana.java

import com.github.hekonsek.grafana.java.model.Dashboard
import com.github.hekonsek.grafana.java.model.Dashboard.Companion.parseDashboard
import json4dummies.Json
import json4dummies.Json.Companion.fromJson
import json4dummies.Json.Companion.toJson
import okhttp3.Credentials
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.create

/**
 * Java REST client for Grafana
 */
class Grafana @JvmOverloads constructor(val authentication: Authentication, val url: String = "http://localhost:3000/api") {

    private val json = MediaType.parse("application/json; charset=utf-8")

    private val dataSourcesEntityType = "datasources"

    private val dashboardsEntityType = "dashboards/db"

    private val http = when {
        authentication is BasicAuthentication -> OkHttpClient.Builder().authenticator({ route, response ->
            val credential = Credentials.basic(authentication.username, authentication.password)
            response.request().newBuilder().header("Authorization", credential).build()
        }).build()
        authentication is ApiKeyAuthentication -> OkHttpClient.Builder().authenticator({ route, response ->
            response.request().newBuilder().header("Authorization", "Bearer ${authentication.apiKey}").build()
        }).build()
        else -> throw IllegalArgumentException("Unknown authentication type.")
    }

    // Raw REST operations

    fun create(type: String, entity: Map<String, Any>): String {
        val body = create(json, toJson(entity))
        val request = Request.Builder().url("$url/$type").post(body).build()
        http.newCall(request).execute().use { response ->
            return when (response.code()) {
                200 -> {
                    val responseBody = Json.fromJson(response.body()?.bytes()!!)
                    val id = responseBody["id"]
                    if (id != null) {
                        "$id"
                    } else {
                        responseBody["slug"] as String
                    }
                }
                409 -> throw EntityAlreadyExistsException()
                422 -> {
                    val responseMap = fromJson(response.body()!!.bytes(), List::class.java)
                    throw IllegalArgumentException(responseMap.joinToString("\n"))
                }
                else -> throw RuntimeException("Unknown result:" + response)
            }
        }
    }

    fun read(type: String, id: Any): Map<String, Any> {
        val request = Request.Builder().get().url("$url/$type/$id").build()
        http.newCall(request).execute().use { response ->
            return fromJson(response.body()?.bytes()!!)
        }
    }

    fun readList(type: String): List<Map<String, Any>> {
        val request = Request.Builder().get().url("${url}/${type}").build()
        http.newCall(request).execute().use { response ->
            return fromJson(response.body()?.bytes()!!, List::class.java) as List<Map<String, Any>>
        }
    }

    fun delete(type: String, id: Int) {
        val request = Request.Builder().delete().url("$url/$type/$id").build()
        http.newCall(request).execute().close()
    }

    // Data sources operations

    fun saveDataSource(dataSource: Map<String, Any>): Int = Integer.parseInt(create(dataSourcesEntityType, dataSource))

    fun readDataSource(id: Int): Map<String, Any> = read(dataSourcesEntityType, id)

    fun listDataSources(): List<Map<String, Any>> = readList(dataSourcesEntityType)

    fun deleteDataSource(id: Int) = delete(dataSourcesEntityType, id)

    // Dashboard operations

    fun saveDashboard(dashboard: Dashboard): String =
            create(dashboardsEntityType, dashboard.model())

    fun readDashboard(slug: String): Dashboard
        = parseDashboard(read(dashboardsEntityType, slug))

}

class EntityAlreadyExistsException : RuntimeException()