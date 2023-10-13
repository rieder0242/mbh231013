package hu.riean.prim.dto

import java.math.BigInteger

/**
 * Prím listánál használatos
 * @see hu.riean.prim.controller.PrimController.list
 */
class PrimListResponse {
    val success: Boolean = true

    /**
     * Prímek listája
     */
    var list = ArrayList<BigInteger>()

    /**
     * Azt jelzi, hogy többet kértünk le mint a megengedett prímek száma
     */
    var truncated: Boolean = false
}