package com.training.model

import java.io.Serializable

class StadiumModel(): Serializable{

    lateinit var id: String
    lateinit var name: String
    var owner_id: String = "null"
    lateinit var location_str: String
    var long: Double = 0.0
    var lat: Double = 0.0
    lateinit var fields: List<Int>
    lateinit var inventory: InventoryModel
    var active: Boolean = false
    var assigned: Boolean = false
    
    constructor(
        id: String,
        name: String,
        owner_id: String,
        location_str: String,
        location: Pair<Double, Double>,
        fields: List<Int>,
        inventory: InventoryModel,
        active: Boolean,
        Assigned: Boolean
    ) : this() {
        this.id = id
        this.name = name
        this.owner_id =owner_id
        this.location_str = location_str
        this.lat = location.first
        this.long = location.second
        this.fields = fields
        this.inventory = inventory
        this.active = active
        this.assigned = Assigned
    }

    constructor(
        id: String?,
        name: String?,
        location_str: String?,
        location: Pair<Double?, Double?>,
        inventory: InventoryModel,
    ) : this() {
        id?.let { this.id = it }
        name?.let {this.name = it}
        this.owner_id = "null"
        location_str?.let{this.location_str = it}
        location.first?.let {this.lat = it}
        location.second?.let {this.long = it}
        this.fields = arrayListOf()
        this.inventory = inventory
        this.active = false
        this.assigned = false
    }

}