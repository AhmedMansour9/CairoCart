package com.cairocart

import com.airbnb.epoxy.TypedEpoxyController
import com.cairocart.adapter.categoriesAdapter
import com.cairocart.data.remote.model.Node
import com.cairocart.data.remote.model.CatModel
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class TreeItemController(
    val onClicked: (node: Node<CatModel>) -> Unit
) : TypedEpoxyController<Node<CatModel>>() {

    override fun buildModels(root: Node<CatModel>) {
        buildTreeItemsModels(root.children)
    }

    private fun buildTreeItemsModels(nodes: List<Node<CatModel>>) {
        nodes.forEach { node ->

            categoriesAdapter {
                id(node.value.id)
                itemData(node.value)
                itemClickedListener { onClicked(node) }
            }
            if (node.value.isExpanded) {
                buildTreeItemsModels(node.children)
            }
        }
    }
}
