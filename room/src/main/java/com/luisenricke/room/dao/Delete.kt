package com.luisenricke.room.dao

import androidx.room.Delete

@Suppress("unused")
interface Delete<X> {
    /**
     * Delete a row from the table.
     *
     * @param row: the object to be deleted.
     * @return the total number of rows dropped.
     */
    @Delete
    fun delete(row: X): Int

    /**
     * Delete a list of rows with vararg from the table.
     *
     * @param rows: the objects to be deleted.
     * @return the total number of rows dropped.
     */
    @Delete
    fun deletes(vararg rows: X): Int

    /**
     * Delete a list of rows from the table.
     *
     * @param rows: the objects to be deleted.
     * @return the total number of rows dropped.
     */
    @Delete
    fun deletes(rows: List<X>): Int
}
