package com.luisenricke.room.dao

@Suppress("unused")
@Deprecated(message = "This interfaces its just a reference query.")
/**
 * @param X is a right table.
 * @param Y is a left table.
 */
interface InnerJoin<X, Y> {
    /**
     * Get a list of objects in the left table existing in the right table.
     *
     * ``@Query(``
     *
     * > ``"""``
     *
     * > ``SELECT * FROM ${X.SCHEMA.TABLE} AS LEFT_ ``
     *
     * > ``INNER JOIN ${SCHEMA.TABLE} AS RIGHT_ ``
     *
     * > ``ON LEFT_.${X.SCHEMA.ID} = RIGHT_.${SCHEMA.X_ID} ``
     *
     * > ``WHERE RIGHT_.${SCHEMA.Y_ID} = :idY ``
     *
     * > ``"""``
     *
     * ``)``
     *
     * @param idY: the id of the right table.
     * @return the list of objects matches.
     */
    fun getX(idY: Long): List<X>


    /**
     * Get a list of objects in the right table existing in the left table.
     *
     * ``@Query(``
     *
     * > ``"""``
     *
     * > ``SELECT * FROM ${Y.SCHEMA.TABLE} AS RIGHT_ ``
     *
     * > ``INNER JOIN ${SCHEMA.TABLE} AS LEFT_ ``
     *
     * > ``ON RIGHT_.${Y.SCHEMA.ID} = LEFT_.${SCHEMA.Y_ID} ``
     *
     * > ``WHERE LEFT_.${SCHEMA.X_ID} = :idX ``
     *
     * > ``"""``
     *
     * ``)``
     *
     * @param idX: the id of the right table.
     * @return the list of objects matches.
     */
    fun getY(idX: Long): List<Y>
}
