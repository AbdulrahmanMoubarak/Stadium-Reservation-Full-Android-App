package com.training.factory

import com.training.ui.admin.AdminActivity
import com.training.ui.customer.CustomerActivity
import com.training.ui.owner.OwnerActivity

class UserActivityFactory {
    fun getActivityClass(activityType: String) =
        when (activityType.lowercase()) {
            "admin" -> {
                AdminActivity::class.java
            }

            "customer" -> {
                CustomerActivity::class.java
            }

            "owner" -> {
                OwnerActivity::class.java
            }

            else -> {
                CustomerActivity::class.java
            }
        }
}