package com.training.model

import java.io.Serializable

class FieldModel(): Serializable{

    lateinit var game: String
    var capacity: Int = 0
    var available: Boolean = true
    var price  = 0.0f
    lateinit var stadium_key: String


    constructor(
        game: String,
        capacity: Int
    ) : this() {

        this.game = game
        this.capacity = capacity
        this.available = true
    }

    constructor(
        game: String,
        capacity: Int,
        available: Boolean,
        price: Float
    ) : this() {

        this.price = price
        this.game = game
        this.capacity = capacity
        this.available = available
    }


}