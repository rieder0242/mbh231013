package hu.riean.prim.bl

/**
 * Searcher lehetséges állapotai
 * @see Searcher.runningState
 */
enum class RunningState {
    SHUTDOWN, STOPPED, STARTING, RUN
}