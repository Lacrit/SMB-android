package com.example.miniproject1.db

import android.app.Application
import android.content.*
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.text.TextUtils
import com.example.miniproject1.viewModels.ProductViewModel

class ProductsProvider : ContentProvider() {
    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val cursor : Cursor? = when (uriMatcher.match(uri)) {
            1 -> ProductDb.getDatabase(context!!).productDao().getProductsWithCursor()
            2 -> ProductDb.getDatabase(context!!).productDao().getProductWithCursor(uri.pathSegments[1].toInt())
            else -> throw java.lang.IllegalArgumentException("Unknown URI $uri")
        }

        cursor!!.setNotificationUri(context!!.contentResolver, uri)
        return cursor
    }

    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
    }

    override fun onCreate(): Boolean {
        uriMatcher.addURI("miniproject1", "products", 1)
        uriMatcher.addURI("miniproject1", "products/#", 2)
        return true
    }
//
//    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
//        var count = 0
//        count =
//            when (uriMatcher.match(uri)) {
//                1 -> db.delete(ProviderHelper.TABLE_NAME, selection, selectionArgs)
//                2 -> {
//                    val id = uri.pathSegments[1]
//                    db.delete(
//                        ProviderHelper.TABLE_NAME,
//                        "${ProviderHelper.KEY_ROW_ID} = $id" + if (!TextUtils.isEmpty(
//                                selection
//                            )
//                        ) " AND ($selection)" else "",
//                        selectionArgs
//                    )
//                }
//                else -> throw java.lang.IllegalArgumentException("Unknown URI $uri")
//            }
//
//        context!!.contentResolver.notifyChange(uri, null)
//        return count
//    }
//
//    override fun getType(uri: Uri): String? {
//        return when (uriMatcher.match(uri)) {
//            1 -> "vnd.android.cursor.dir/vnd.com.example.provider.products"
//            2 -> "vnd.android.cursor.item/vnd.com.example.provider.products"
//            else -> throw IllegalArgumentException("Uri is not supported: $uri")
//        }
//    }
//
//    override fun insert(uri: Uri, values: ContentValues?): Uri? {
//        val rowID = db.insert(ProviderHelper.TABLE_NAME, "", values)
//
//        if (rowID > 0) {
//            val uriId = ContentUris.withAppendedId(
//                providerHelper.getProductsUri(),
//                rowID
//            )
//            context!!.contentResolver.notifyChange(uriId, null)
//            return uriId
//        }
//
//        throw SQLException("Failed to add a record into $uri")
//    }
//
//    override fun query(
//        uri: Uri, projection: Array<String>?, selection: String?,
//        selectionArgs: Array<String>?, sortOrder: String?
//    ): Cursor {
//        val qb = SQLiteQueryBuilder()
//
//        uriMatcher.match(uri)
//        qb.tables = ProviderHelper.TABLE_NAME
//        when (uriMatcher.match(uri)) {
//            1 -> qb.tables = ProviderHelper.TABLE_NAME
//            2 -> qb.appendWhere("${ProviderHelper.KEY_ROW_ID} = ${uri.pathSegments[1]}")
//            else -> throw java.lang.IllegalArgumentException("Unknown URI $uri")
//        }
//
//        val cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder)
//        cursor.setNotificationUri(context!!.contentResolver, uri)
//        return cursor
//    }
//
//    override fun update(
//        uri: Uri, values: ContentValues?, selection: String?,
//        selectionArgs: Array<String>?
//    ): Int {
//        var count = 0
//        count =
//            when (uriMatcher.match(uri)) {
//                2 -> {
//                    val id = uri.pathSegments[1]
//                    db.update(
//                        ProviderHelper.TABLE_NAME,
//                        values,
//                        "${ProviderHelper.KEY_ROW_ID} = $id",
//                        selectionArgs
//                    )
//                }
//                else -> throw java.lang.IllegalArgumentException("Unknown URI $uri")
//            }
//        context!!.contentResolver.notifyChange(uri, null)
//        return count
//    }

}