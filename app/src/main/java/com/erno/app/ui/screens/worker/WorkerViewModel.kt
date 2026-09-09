package com.erno.app.ui.screens.worker

import androidx.lifecycle.ViewModel
import com.erno.app.data.model.DurationPay
import com.erno.app.data.model.EarningRecord
import com.erno.app.data.model.JobPost
import com.erno.app.data.model.WorkerJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class WorkerState(
    val workerName: String = "Aman",
    val currentLocation: String = "Okhla Market Area",
    val totalEarnings: Int = 1247,
    val availableJobs: List<WorkerJob> = listOf(
        WorkerJob(
            id = "w_delivery_1",
            title = "Express Delivery Partner",
            location = "Okhla Market to Sector 18 Noida",
            distance = "1.8 km away",
            description = "Deliver grocery & parcel items from Okhla Store to Sector 18 Noida address safely.",
            isNew = true,
            isDeliveryJob = true,
            pickupLocation = "Okhla Market Store #12",
            dropLocation = "Flat 402, Sector 18 Noida",
            packageType = "Parcel Box (2.5 kg)",
            payStructure = listOf(
                DurationPay(1, 149),
                DurationPay(2, 249),
                DurationPay(3, 349)
            )
        ),
        WorkerJob(
            id = "w1",
            title = "Shop Helper",
            location = "Okhla Market Area",
            distance = "2.1 km away",
            description = "Need a shop helper for assisting in customer handling and store work.",
            isNew = true,
            payStructure = listOf(
                DurationPay(1, 99),
                DurationPay(2, 189),
                DurationPay(3, 279),
                DurationPay(4, 349)
            )
        ),
        WorkerJob(
            id = "w2",
            title = "Counter Staff",
            location = "Okhla Market Area",
            distance = "2.4 km away",
            description = "Counter staff required for billing and customer query support.",
            isNew = false,
            payStructure = listOf(
                DurationPay(1, 99),
                DurationPay(2, 189),
                DurationPay(3, 279)
            )
        )
    ),
    val selectedJob: WorkerJob? = null,
    val myJobs: List<WorkerJob> = listOf(
        WorkerJob(
            id = "w1_assigned",
            title = "Express Delivery Partner",
            location = "Okhla Market Store to Sector 18 Noida",
            distance = "1.8 km away",
            description = "Deliver grocery & parcel items from Okhla Store to Sector 18 Noida address safely.",
            selectedHours = 1,
            selectedPay = 149,
            status = "Upcoming",
            scheduledTime = "20 May, 11:00 AM",
            isDeliveryJob = true,
            pickupLocation = "Okhla Market Store #12",
            dropLocation = "Flat 402, Sector 18 Noida",
            packageType = "Grocery / Parcel Box",
            payStructure = listOf(DurationPay(1, 149))
        ),
        WorkerJob(
            id = "w2_assigned",
            title = "Shop Helper",
            location = "Okhla Market Area",
            distance = "2.1 km away",
            description = "Need a shop helper for assisting in customer handling and store work.",
            selectedHours = 2,
            selectedPay = 189,
            status = "Completed",
            scheduledTime = "18 May, 10:00 AM",
            payStructure = listOf(DurationPay(2, 189))
        )
    ),
    val earningsHistory: List<EarningRecord> = listOf(
        EarningRecord("e1", "Express Delivery Partner", "1 Hour", "20 May, 2025", 149),
        EarningRecord("e2", "Shop Helper", "2 Hours", "18 May, 2025", 189),
        EarningRecord("e3", "Counter Staff", "4 Hours", "15 May, 2025", 349)
    )
)

class WorkerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(WorkerState())
    val uiState: StateFlow<WorkerState> = _uiState.asStateFlow()

    fun selectJob(job: WorkerJob) {
        _uiState.update { it.copy(selectedJob = job) }
    }

    fun selectDurationPay(hours: Int, pay: Int) {
        _uiState.update { state ->
            val currentJob = state.selectedJob ?: return@update state
            state.copy(selectedJob = currentJob.copy(selectedHours = hours, selectedPay = pay))
        }
    }

    fun acceptCurrentJob() {
        val state = _uiState.value
        val currentJob = state.selectedJob ?: return
        val acceptedJob = currentJob.copy(status = "Upcoming", scheduledTime = "Today, 11:00 AM")
        _uiState.update {
            it.copy(
                selectedJob = acceptedJob,
                myJobs = listOf(acceptedJob) + it.myJobs.filter { j -> j.id != acceptedJob.id }
            )
        }
    }

    fun addJobFromShopkeeper(job: JobPost) {
        val newWorkerJob = WorkerJob(
            id = job.id,
            title = job.title,
            location = job.location,
            distance = "1.2 km away",
            description = job.description,
            isNew = true,
            payStructure = job.payStructure,
            selectedHours = job.payStructure.firstOrNull()?.hours ?: 1,
            selectedPay = job.payStructure.firstOrNull()?.price ?: 149,
            status = "Available",
            scheduledTime = "Today, " + job.postedTime,
            isDeliveryJob = job.isDeliveryJob,
            pickupLocation = job.pickupLocation,
            dropLocation = job.dropLocation,
            packageType = job.packageType
        )
        _uiState.update {
            it.copy(
                availableJobs = listOf(newWorkerJob) + it.availableJobs.filter { j -> j.id != newWorkerJob.id }
            )
        }
    }
}
