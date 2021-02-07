package com.cairocartt

import android.util.Log
import com.airbnb.epoxy.TypedEpoxyController
import com.cairocartt.adapter.CategoriesAdapter
import com.cairocartt.adapter.categoriesAdapter
import com.cairocartt.data.remote.model.Node
import com.cairocartt.data.remote.model.CatModel
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class TreeItemController(val itemClick:CategoriesAdapter.ItemListener,
    val onClicked: (node: Node<CatModel>) -> Unit
) : TypedEpoxyController<Node<CatModel>>() {

    private  val TAG = "TreeItemController"
    override fun buildModels(root: Node<CatModel>) {
        buildTreeItemsModels(root.children)

    }

    private fun buildTreeItemsModels(nodes: List<Node<CatModel>>) {
        nodes.forEach { node ->
            categoriesAdapter {
                id(node.value.id)
                itemData(node.value)
                itemClickedListener {
                    Log.d(TAG, "buildTreeItemsModels: "+node.value.notChild)
                    onClicked(node)
                }
            }
            if (node.value.isExpanded) {
                buildTreeItemsModels(node.children)
            }
        }
    }
}

