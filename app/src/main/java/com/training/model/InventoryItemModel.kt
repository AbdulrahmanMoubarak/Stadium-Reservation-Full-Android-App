package com.training.model

import java.io.Serializable

data class InventoryItemModel(
    var stadium_id: Int,
    var item_name: String,
    var quantity: Int
): Serializable