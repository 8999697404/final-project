package com.luisenricke.room.dao

import androidx.room.Update

@Suppress("unused")
interface Update<X> {
    /**
     * Update a row with an existing ID in the table and change the data.
     *
     * @param row: the object to be updated.
     * @return the total number of rows changed.
     */
    @Update
    fun update(row: X): Int

    /**
     * Update a list of objects with vararg which contains existing ID in the table and change the data.
     *
     * @param rows: the objects to be updated.
     * @return the total number of rows changed.
     */
    @Update
    fun updates(vararg rows: X): Int

    /**
     * Update a list of rows with an existing ID in the table and change the data.
     *
     * @param rows: the objects to be updated.
     * @return the total number of rows changed.
     */
    @Update
    fun updates(rows: List<X>): Int
}
