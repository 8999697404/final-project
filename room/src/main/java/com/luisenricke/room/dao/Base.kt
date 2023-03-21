package com.luisenricke.room.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Suppress("unused")
interface Base<X> {
    /**
     * Insert a row in the table.
     *
     * @param row: the object to be inserted.
     * @return id of the row inserted.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(row: X): Long

    /**
     * Insert a list of objects with vararg to the table.
     *
     * @param rows: the objects to be inserted.
     * @return the list of id's of the rows inserted.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun inserts(vararg rows: X): List<Long>

    /**
     * Insert a list of objects to the table.
     *
     * @param rows: the objects to be inserted.
     * @return the list of id's of the rows inserted.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun inserts(rows: List<X>): List<Long>

    /**
     * Count rows from the table.
     *
     * ``@Query("SELECT COUNT(*) FROM ${SCHEMA.TABLE}")``
     *
     * @return the total number of rows.
     */
    fun count(): Long

    /**
     * Get a list of objects existing in the table.
     *
     * ``@Query("SELECT * FROM ${SCHEMA.TABLE}")``
     *
     * @return list of objects requested.
     */
    fun get(): List<X>

    /**
     * Drop all rows existing in the table.
     *
     * ``@Query("DELETE FROM ${SCHEMA.TABLE}")``
     */
    fun drop()
}
