package io.dnajd.projecttableservice.util

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

@Component
class QueryConstructor{
    @Value("\${spring.datasource.url}")
    lateinit var dbLocation: String
    @Value("\${spring.datasource.username}")
    lateinit var dbUsername: String
    @Value("\${spring.datasource.password}")
    lateinit var dbPassword: String

    private fun getConnection(
        body: (Connection) -> Unit
    ) {
        DriverManager.getConnection(dbLocation, dbUsername, dbPassword).use {
            body(it)
        }
    }

    fun executeUpdate(vararg queries: String): List<Int> {
        if(queries.isEmpty()){
            throw IllegalArgumentException()
        }
        val queryResults = mutableListOf<Int>()
        for(query in queries) {
            getConnection { connection ->
                connection.prepareStatement(query).use { prepareStatement ->
                    queryResults.add(prepareStatement.executeUpdate())
                    prepareStatement.closeOnCompletion()
                }
            }
        }
        return queryResults
    }

    fun executeQuery(vararg queries: String): List<ResultSet> {
        if(queries.isEmpty()){
            throw IllegalArgumentException()
        }
        val queryResults = mutableListOf<ResultSet>()
        for(query in queries) {
            getConnection { connection ->
                connection.prepareStatement(query).use { prepareStatement ->
                    queryResults.add(prepareStatement.executeQuery())
                    prepareStatement.closeOnCompletion()
                }
            }
        }
        return queryResults
    }

    fun execute(vararg queries: String): List<Boolean> {
        if(queries.isEmpty()){
            throw IllegalArgumentException()
        }
        val queryResults = mutableListOf<Boolean>()
        for(query in queries) {
            getConnection { connection ->
                connection.prepareStatement(query).use { prepareStatement ->
                    queryResults.add(prepareStatement.execute())
                    prepareStatement.closeOnCompletion()
                }
            }
        }
        return queryResults
    }
}