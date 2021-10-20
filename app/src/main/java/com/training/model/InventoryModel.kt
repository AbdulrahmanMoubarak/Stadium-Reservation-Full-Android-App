package com.training.model

import java.io.Serializable

class InventoryModel(): Serializable{

    lateinit var items: HashMap<String, Int>

    constructor(items: HashMap<String, Int>) : this() {
        this.items = items
    }
}