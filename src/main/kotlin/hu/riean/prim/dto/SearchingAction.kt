package hu.riean.prim.dto

import hu.riean.prim.bl.RunningState

/**
 * Keresés inditásánál vagy leállításánál ez a válasz
 * @see hu.riean.prim.controller.SearchingController
 */
public class SearchingAction(var success: Boolean, var previousState: RunningState) {


}