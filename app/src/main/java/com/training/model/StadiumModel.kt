package com.training.model

import java.io.Serializable

data class StadiumModel(
    var id: Int,
    var name: String,
    var owner_id: Int,
    var location_str: String,
    var location: Pair<Int, Int>,
    var fields: List<FieldModel>,
    var inventory: List<InventoryItemModel>,
    var active: Boolean
): Serializable