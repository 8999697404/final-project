package com.luisenricke.room.dao

@Suppress("unused")
interface PrimaryKey<X> {

    /**
     * Get a object existing in the table by ID.
     *
     * ``@Query("SELECT * FROM ${SCHEMA.TABLE} WHERE id = :id")``
     *
     * @param id: the id of the searched object.
     * @return the object requested.
     */
    fun get(id: Long): X?

    /**
     * Get a list of object existing in the table by ID's.
     *
     *  ``@Query("SELECT * FROM ${SCHEMA.TABLE} WHERE id IN(:ids)")``
     *
     * @param ids: the list id's of the searched objects.
     * @return the list objects requested.
     *
     */
    fun get(ids: LongArray): List<X>

    /**
     * Delete a row existing in the table by ID.
     *
     * ``@Query("DELETE FROM ${SCHEMA.TABLE} WHERE id = :id")``
     *
     * @param id: the id of the searched object.
     * @return the total number of rows dropped.
     */
    fun delete(id: Long): Int

    /**
     * Delete a list of object existing in the table by ID's.
     *
     *  ``@Query("DELETE FROM ${SCHEMA.TABLE} WHERE id IN(:ids)")``
     *
     * @param ids: the list id's of the searched objects.
     * @return the total number of rows dropped.
     *
     */
    fun deletes(ids: LongArray): Int
}
