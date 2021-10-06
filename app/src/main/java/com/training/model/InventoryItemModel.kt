package com.training.model

import java.io.Serializable

data class InventoryItemModel(
    var item_name: String,
    var quantity: Int
): Serializable