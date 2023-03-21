package com.luisenricke.helpyourself.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.luisenricke.helpyourself.database.entity.Alert
import com.luisenricke.room.dao.Base
import com.luisenricke.room.dao.Delete
import com.luisenricke.room.dao.PrimaryKey
import com.luisenricke.room.dao.Update

@Suppress("unused")
@Dao
abstract class AlertDAO : Base<Alert>, Update<Alert>, Delete<Alert>, PrimaryKey<Alert> {

    @Query("SELECT COUNT(*) FROM ${Alert.SCHEMA.TABLE}")
    abstract override fun count(): Long

    @Query("SELECT * FROM ${Alert.SCHEMA.TABLE}")
    abstract override fun get(): List<Alert>

    @Query("DELETE FROM ${Alert.SCHEMA.TABLE}")
    abstract override fun drop()

    @Query("SELECT * FROM ${Alert.SCHEMA.TABLE} WHERE id = :id")
    abstract override fun get(id: Long): Alert?

    @Query("SELECT * FROM ${Alert.SCHEMA.TABLE} WHERE id IN(:ids)")
    abstract override fun get(ids: LongArray): List<Alert>

    @Query("DELETE FROM ${Alert.SCHEMA.TABLE} WHERE id = :id")
    abstract override fun delete(id: Long): Int

    @Query("DELETE FROM ${Alert.SCHEMA.TABLE} WHERE id IN(:ids)")
    abstract override fun deletes(ids: LongArray): Int

    @Query("SELECT * FROM ${Alert.SCHEMA.TABLE} ORDER BY ${Alert.SCHEMA.ID} LIMIT 1")
    abstract fun last(): Alert?

    @Query("DELETE FROM ${Alert.SCHEMA.TABLE} WHERE id = (SELECT MAX(id) FROM ${Alert.SCHEMA.TABLE})")
    abstract fun deleteLast()
}
