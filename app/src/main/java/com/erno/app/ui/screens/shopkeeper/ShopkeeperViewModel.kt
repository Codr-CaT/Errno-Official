package com.erno.app.ui.screens.shopkeeper

import androidx.lifecycle.ViewModel
import com.erno.app.data.model.DurationPay
import com.erno.app.data.model.JobPost
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ShopkeeperState(
    val ownerName: String = "Rahul",
    val activeJobsCount: Int = 8,
    val totalApplicationsCount: Int = 34,
    val recentJobs: List<JobPost> = listOf(
        JobPost(
            id = "1",
            title = "Shop Helper",
            category = "Shop Helper",
            location = "Okhla Market Area",
            description = "Need a shop helper for assisting in customer handling and store work.",
            payStructure = listOf(
                DurationPay(1, 99),
                DurationPay(2, 189),
                DurationPay(3, 279),
                DurationPay(4, 349)
            ),
            status = "Active",
            postedTime = "Posted 2h ago",
            applicantsCount = 5
        ),
        JobPost(
            id = "2",
            title = "Counter Staff",
            category = "Counter Staff",
            location = "Okhla Market Area",
            description = "Counter staff required for billing and customer query support.",
            payStructure = listOf(
                DurationPay(1, 99),
                DurationPay(2, 189),
                DurationPay(3, 279),
                DurationPay(4, 349)
            ),
            status = "Active",
            postedTime = "Posted 1d ago",
            applicantsCount = 3
        ),
        JobPost(
            id = "3",
            title = "Store Assistant",
            category = "Store Assistant",
            location = "Okhla Market Area",
            description = "Assisting in inventory management and restocking shelves.",
            payStructure = listOf(
                DurationPay(1, 99),
                DurationPay(2, 189)
            ),
            status = "Active",
            postedTime = "13Sep0",
            applicantsCount = 2
        ),
        JobPost(
            id = "4",
            title = "Packing Staff",
            category = "Packing Staff",
            location = "Okhla Market Area",
            description = "Help with item packing and shipping dispatch preparation.",
            payStructure = listOf(
                DurationPay(1, 99),
                DurationPay(2, 189),
                DurationPay(3, 279),
                DurationPay(4, 349)
            ),
            status = "Completed",
            postedTime = "Posted 3d ago",
            applicantsCount = 0
        )
    ),
    // Draft job for creation
    val draftTitle: String = "Shop Helper",
    val draftCategory: String = "Shop Helper",
    val draftLocation: String = "Okhla Market Area",
    val draftDescription: String = "Need a shop helper for assisting in customer handling and store work.",
    val draftPayStructure: List<DurationPay> = listOf(
        DurationPay(1, 99),
        DurationPay(2, 189),
        DurationPay(3, 279),
        DurationPay(4, 349),
        DurationPay(5, 419),
        DurationPay(6, 489),
        DurationPay(7, 559),
        DurationPay(8, 629)
    ),
    val lastPostedJob: JobPost? = null
)

class ShopkeeperViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ShopkeeperState())
    val uiState: StateFlow<ShopkeeperState> = _uiState.asStateFlow()

    fun updateTitle(title: String) {
        _uiState.update { it.copy(draftTitle = title) }
    }

    fun updateCategory(category: String) {
        _uiState.update { it.copy(draftCategory = category) }
    }

    fun updateLocation(location: String) {
        _uiState.update { it.copy(draftLocation = location) }
    }

    fun updateDescription(description: String) {
        _uiState.update { it.copy(draftDescription = description) }
    }

    fun removeDurationPay(item: DurationPay) {
        _uiState.update { state ->
            itCopy(state, draftPayStructure = state.draftPayStructure.filter { dp -> dp.hours != item.hours })
        }
    }

    fun updatePrice(hours: Int, newPrice: Int) {
        _uiState.update { state ->
            val updated = state.draftPayStructure.map {
                if (it.hours == hours) it.copy(price = newPrice) else it
            }
            itCopy(state, draftPayStructure = updated)
        }
    }

    fun addDurationPay(hours: Int, price: Int) {
        _uiState.update { state ->
            val existing = state.draftPayStructure.filter { it.hours != hours }
            val newList = (existing + DurationPay(hours, price)).sortedBy { it.hours }
            itCopy(state, draftPayStructure = newList)
        }
    }

    fun postJob(): JobPost {
        val state = _uiState.value
        val newJob = JobPost(
            id = System.currentTimeMillis().toString(),
            title = state.draftTitle.ifBlank { "Shop Helper" },
            category = state.draftCategory.ifBlank { "Shop Helper" },
            location = state.draftLocation.ifBlank { "Okhla Market Area" },
            description = state.draftDescription,
            payStructure = state.draftPayStructure,
            status = "Active",
            postedTime = "Posted just now",
            applicantsCount = 0
        )
        _uiState.update {
            it.copy(
                recentJobs = listOf(newJob) + it.recentJobs,
                activeJobsCount = it.activeJobsCount + 1,
                lastPostedJob = newJob
            )
        }
        return newJob
    }

    private fun itCopy(state: ShopkeeperState, draftPayStructure: List<DurationPay>): ShopkeeperState {
        return state.copy(draftPayStructure = draftPayStructure)
    }
}
