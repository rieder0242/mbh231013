package hu.riean.prim.dto

/**
 * Hiba esetén ezt adjuk vissza
 */
class ActionFail(var message: String?) {
    var success: Boolean = false

}