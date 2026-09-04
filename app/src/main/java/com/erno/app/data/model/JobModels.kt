package com.erno.app.data.model

data class DurationPay(
    val hours: Int,
    val price: Int
)

data class JobPost(
    val id: String,
    val title: String,
    val category: String,
    val location: String,
    val description: String,
    val payStructure: List<DurationPay>,
    val status: String = "Active", // "Active", "Completed"
    val postedTime: String = "Just now",
    val applicantsCount: Int = 0
)

data class WorkerJob(
    val id: String,
    val title: String,
    val location: String,
    val distance: String,
    val description: String,
    val payStructure: List<DurationPay>,
    val isNew: Boolean = false,
    val selectedHours: Int = 2,
    val selectedPay: Int = 189,
    val status: String = "Available", // "Available", "Accepting", "Accepted", "Upcoming", "Ongoing", "Completed"
    val scheduledTime: String = "20 May, 11:00 AM",
    val latitude: Double = 28.5355,
    val longitude: Double = 77.2641
)

data class EarningRecord(
    val id: String,
    val jobTitle: String,
    val hours: String,
    val date: String,
    val amount: Int
)
