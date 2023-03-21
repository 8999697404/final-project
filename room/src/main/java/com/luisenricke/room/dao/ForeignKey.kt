package com.luisenricke.room.dao

@Suppress("unused")
@Deprecated(message = "This interfaces its just a reference query.")
/**
 * @param X is a child table.
 * @param Y is a parent table.
 */
interface ForeignKey<X, Y> {
    /**
     * Count rows from the table X where exist in Y.
     *
     * ``@Query("SELECT COUNT(*) FROM  ${SCHEMA.TABLE} WHERE Y_id IS NOT NULL")``
     *
     * @return the total number of rows.
     */
    fun countByY(): Long

    /**
     * Count rows from the table X where not exist in Y .
     *
     * ``@Query("SELECT COUNT(*) FROM  ${SCHEMA.TABLE} WHERE Y_id IS NULL")``
     *
     * @return the total number of rows.
     */
    fun countNotByY(): Long

    /**
     * Count rows from the table by FK reference.
     *
     * ``@Query("SELECT COUNT(*) FROM  ${SCHEMA.TABLE} WHERE Y_id = :fk")``
     *
     * @param fk: the id of the reference table.
     * @return the total number of rows.
     */
    fun countByY(fk: Long): Long

    /**
     * Get a list of objects from the table X where exist in Y.
     *
     * ``@Query("SELECT * FROM  ${SCHEMA.TABLE} WHERE Y_id IS NOT NULL")``
     *
     * @return the total number of rows.
     */
    fun getByY(): List<X>

    /**
     * Get a list of objects from the table X where not exist in Y .
     *
     * ``@Query("SELECT * FROM  ${SCHEMA.TABLE} WHERE Y_id IS NULL")``
     *
     * @return the total number of rows.
     */
    fun getNotByY(): List<X>

    /**
     * Get a list of objects from the table by FK reference.
     *
     * ``@Query("SELECT * FROM  ${SCHEMA.TABLE} WHERE Y_id = :fk")``
     *
     * @param fk: the id of the reference table.
     * @return the total number of rows.
     */
    fun getByY(fk: Long): List<X>

    /**
     * Drop all rows existing in the table by ID of the reference.
     *
     * ``@Query("DELETE FROM ${SCHEMA.TABLE} WHERE Y_id = :fk")``
     *
     * @param fk: the id of the reference table.
     */
    fun dropByY(fk: Long)
}
