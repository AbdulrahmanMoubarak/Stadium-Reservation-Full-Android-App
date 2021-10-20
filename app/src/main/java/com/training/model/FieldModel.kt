package com.training.model

import java.io.Serializable

data class FieldModel(
    var game: String,
    var inventory_use: List<InventoryModel>,
    var capacity: Int,
    var available: Boolean
): Serializable