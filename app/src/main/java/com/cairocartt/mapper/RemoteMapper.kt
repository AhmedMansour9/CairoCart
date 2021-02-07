package com.cairocartt.mapper

import com.cairocartt.data.remote.model.Node
import com.cairocartt.data.remote.model.CategoriesResponse
import com.cairocartt.data.remote.model.CatModel


fun CategoriesResponse.DataCategory.toTree(): Node<CatModel>? {
    val root: Node<CatModel> = Node(CatModel(-1, null, false, -1, "", -1, -1, false,notChild = false))
    root.children = toCategories(childrenData) ?: mutableListOf()
    return root
}

 fun toCategories(childrenData: List<CategoriesResponse.DataCategory.ChildrenDataa?>?): MutableList<Node<CatModel>>? {
    return childrenData?.mapNotNullTo(mutableListOf()) { children ->
        val id = children?.id
        val name = children?.name
        if (id == null || name == null)
            null
        else {

            val node: Node<CatModel> = Node(
                CatModel(
                    id = id,
                    image = children.image,
                    name = name,
                    isActive = children.isActive ?: false,
                    isExpanded = false,
                    notChild = children.childrenData.isNullOrEmpty(),
                    level = children.level ?: 0,
                    productCount = children.productCount ?: 0,
                    parentId = children.parentId ?: 0

                )
            )
            node.children = toCategories(children.childrenData) ?: mutableListOf()
            node
        }

    }
}
