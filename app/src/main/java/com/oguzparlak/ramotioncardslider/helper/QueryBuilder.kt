package com.oguzparlak.ramotioncardslider.helper

import android.net.Uri
import android.support.annotation.NonNull

abstract class QueryBuilder<in T>(@NonNull private val baseUrl: String) {

    /**
     * Base uri of an endpoint
     */
    val baseUri: Uri = Uri.parse(baseUrl)

    /**
     * Returns a query string based on the type of
     * the query
     */
    internal abstract fun getQuery(query: T): String

}
