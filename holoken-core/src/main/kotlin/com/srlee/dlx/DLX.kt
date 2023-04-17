package com.srlee.dlx

open class DLX {
    private val root = DLXColumn()
    private val trysolution = ArrayList<Int>()
    private var ColHdrs: Array<DLXColumn?> = arrayOfNulls(1) //TODO
    private var Nodes: Array<DLXNode?> = arrayOfNulls(1) //TODO
    private var numnodes = 0
    private var lastNodeAdded: DLXNode? = null
    private var numberOfSolutions = 0
    private var previousRow = -1
    private var solvetype: SolveType? = null
    protected fun init(numberOfColumns: Int, numberOfNodes: Int) {
        ColHdrs = arrayOfNulls(numberOfColumns + 1)
        for (c in 1..numberOfColumns) {
            ColHdrs[c] = DLXColumn()
        }
        Nodes = arrayOfNulls(numberOfNodes + 1)
        numnodes = 0 // None allocated
        var prev: DLXColumn? = root
        for (i in 1..numberOfColumns) {
            prev!!.right = ColHdrs[i]
            ColHdrs[i]!!.left = prev
            prev = ColHdrs[i]
        }
        root.left = ColHdrs[numberOfColumns]
        ColHdrs[numberOfColumns]!!.right = root
    }

    private fun coverColumn(column: DLXColumn?) {
        column!!.right!!.left = column.left
        column.left!!.right = column.right
        var i = column.down
        while (i !== column) {
            var j = i!!.right
            while (j !== i) {
                j!!.down!!.up = j.up
                j.up!!.down = j.down
                (j as DLXNode).column.decrementSize()
                j = j.right
            }
            i = i.down
        }
    }

    private fun uncoverColumn(column: DLXColumn?) {
        var i = column!!.up
        while (i !== column) {
            var j = i!!.left
            while (j !== i) {
                (j as DLXNode).column.incrementSize()
                j.down!!.up = j
                j.up!!.down = j
                j = j.left
            }
            i = i.up
        }
        column.right!!.left = column
        column.left!!.right = column
    }

    private fun ChooseMinCol(): DLXColumn? {
        var minsize = Int.MAX_VALUE
        var search: DLXColumn
        var mincol: DLXColumn
        search = root.right as DLXColumn
        mincol = search
        while (search !== root) {
            if (search.size < minsize) {
                mincol = search
                minsize = mincol.size
                if (minsize == 0) {
                    break
                }
            }
            search = search.right as DLXColumn
        }
        return if (minsize == 0) {
            null
        } else {
            mincol
        }
    }

    protected fun addNode(column: Int, row: Int) {
        Nodes[++numnodes] = DLXNode(ColHdrs[column]!!, row)
        if (previousRow == row) {
            Nodes[numnodes]!!.left = lastNodeAdded
            Nodes[numnodes]!!.right = lastNodeAdded!!.right
            lastNodeAdded!!.right = Nodes[numnodes]
            Nodes[numnodes]!!.right!!.left = Nodes[numnodes]
        } else {
            previousRow = row
            Nodes[numnodes]!!.left = Nodes[numnodes]
            Nodes[numnodes]!!.right = Nodes[numnodes]
        }
        lastNodeAdded = Nodes[numnodes]
    }

    fun Solve(st: SolveType?): Int {
        solvetype = st
        numberOfSolutions = 0
        search(trysolution.size)
        return numberOfSolutions
    }

    private fun search(k: Int) {
        if (root.right === root) {
            numberOfSolutions++
            return
        }
        val chosenCol = ChooseMinCol()
        if (chosenCol != null) {
            coverColumn(chosenCol)
            var r = chosenCol.down
            while (r !== chosenCol) {
                if (k >= trysolution.size) {
                    trysolution.add((r as DLXNode).row)
                } else {
                    trysolution[k] = (r as DLXNode).row
                }
                var j = r.right
                while (j !== r) {
                    coverColumn((j as DLXNode).column)
                    j = j.right
                }
                search(k + 1)
                if (solvetype == SolveType.ONE && numberOfSolutions > 0) // Stop as soon as we find 1 solution
                {
                    return
                }
                if (solvetype == SolveType.MULTIPLE && numberOfSolutions > 1) // Stop as soon as we find multiple solutions
                {
                    return
                }
                j = r.left
                while (j !== r) {
                    uncoverColumn((j as DLXNode).column)
                    j = j.left
                }
                r = r.down
            }
            uncoverColumn(chosenCol)
        }
    }

    enum class SolveType {
        ONE, MULTIPLE
    }
}