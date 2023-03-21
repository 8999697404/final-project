package com.luisenricke.helpyourself.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.luisenricke.helpyourself.database.entity.Contact
import com.luisenricke.helpyourself.database.entity.Contact.SCHEMA
import com.luisenricke.room.dao.Base
import com.luisenricke.room.dao.Delete
import com.luisenricke.room.dao.PrimaryKey
import com.luisenricke.room.dao.Update

@Suppress("unused")
@Dao
abstract class ContactDAO : Base<Contact>, Update<Contact>, Delete<Contact>, PrimaryKey<Contact> {

    @Query("SELECT COUNT(*) FROM ${SCHEMA.TABLE}")
    abstract override fun count(): Long

    @Query("SELECT * FROM ${SCHEMA.TABLE}")
    abstract override fun get(): List<Contact>

    @Query("DELETE FROM ${SCHEMA.TABLE}")
    abstract override fun drop()

    @Query("SELECT * FROM ${SCHEMA.TABLE} WHERE id = :id")
    abstract override fun get(id: Long): Contact?

    @Query("SELECT * FROM ${SCHEMA.TABLE} WHERE id IN(:ids)")
    abstract override fun get(ids: LongArray): List<Contact>

    @Query("DELETE FROM ${SCHEMA.TABLE} WHERE id = :id")
    abstract override fun delete(id: Long): Int

    @Query("DELETE FROM ${SCHEMA.TABLE} WHERE id IN(:ids)")
    abstract override fun deletes(ids: LongArray): Int

    @Query("SELECT COUNT(*) FROM ${SCHEMA.TABLE} WHERE isHighlighted = 1")
    abstract fun countHighlighted(): Long

    @Query("SELECT * FROM ${SCHEMA.TABLE} WHERE isHighlighted = 1")
    abstract fun getHighlighted(): List<Contact>

    @Query("SELECT COUNT(*) FROM ${SCHEMA.TABLE} WHERE phone = :phone")
    abstract fun countByPhone(phone: String): Long
}
