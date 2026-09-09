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
            id = "dp_1",
            title = "Express Delivery Partner",
            category = "Delivery Partner",
            location = "Okhla Store -> Sector 18 Noida",
            description = "Deliver package from shop to customer address within 2 hours.",
            payStructure = listOf(
                DurationPay(1, 149),
                DurationPay(2, 249)
            ),
            status = "Active",
            postedTime = "Posted 30m ago",
            applicantsCount = 4,
            isDeliveryJob = true,
            pickupLocation = "Okhla Market Store #12",
            dropLocation = "Flat 402, Sector 18 Noida",
            packageType = "Electronics Parcel"
        ),
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
        )
    ),
    // Draft job for creation
    val draftTitle: String = "Delivery Partner",
    val draftCategory: String = "Delivery Partner",
    val draftLocation: String = "Okhla Market Area",
    val draftPickupLocation: String = "Okhla Market Store #12",
    val draftDropLocation: String = "Sector 18 Noida",
    val draftPackageType: String = "Parcel / Product Box",
    val draftIsDelivery: Boolean = true,
    val draftDescription: String = "Deliver parcel/products safely from shop to target location.",
    val draftPayStructure: List<DurationPay> = listOf(
        DurationPay(1, 149),
        DurationPay(2, 249),
        DurationPay(3, 349),
        DurationPay(4, 449)
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
        val isDelivery = category.equals("Delivery Partner", ignoreCase = true) || category.contains("Delivery", ignoreCase = true)
        _uiState.update {
            it.copy(
                draftCategory = category,
                draftIsDelivery = isDelivery,
                draftTitle = if (isDelivery) "Delivery Partner" else it.draftTitle
            )
        }
    }

    fun updateLocation(location: String) {
        _uiState.update { it.copy(draftLocation = location) }
    }

    fun updatePickupLocation(pickup: String) {
        _uiState.update { it.copy(draftPickupLocation = pickup) }
    }

    fun updateDropLocation(drop: String) {
        _uiState.update { it.copy(draftDropLocation = drop) }
    }

    fun updatePackageType(packageType: String) {
        _uiState.update { it.copy(draftPackageType = packageType) }
    }

    fun updateDescription(description: String) {
        _uiState.update { it.copy(draftDescription = description) }
    }

    fun resetDraft() {
        _uiState.update {
            it.copy(
                draftTitle = "Delivery Partner",
                draftCategory = "Delivery Partner",
                draftLocation = "Okhla Market Area",
                draftPickupLocation = "Okhla Market Store #12",
                draftDropLocation = "Sector 18 Noida",
                draftPackageType = "Parcel / Product Box",
                draftIsDelivery = true,
                draftDescription = "Deliver parcel/products safely from shop to target location.",
                draftPayStructure = listOf(
                    DurationPay(1, 149),
                    DurationPay(2, 249),
                    DurationPay(3, 349),
                    DurationPay(4, 449)
                )
            )
        }
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
            title = state.draftTitle.ifBlank { "Delivery Partner" },
            category = state.draftCategory.ifBlank { "Delivery Partner" },
            location = state.draftLocation.ifBlank { "Okhla Market Area" },
            description = state.draftDescription.ifBlank { "Deliver parcel/products safely." },
            payStructure = if (state.draftPayStructure.isNotEmpty()) state.draftPayStructure else listOf(DurationPay(1, 149), DurationPay(2, 249)),
            status = "Active",
            postedTime = "Posted just now",
            applicantsCount = 0,
            isDeliveryJob = state.draftIsDelivery,
            pickupLocation = state.draftPickupLocation,
            dropLocation = state.draftDropLocation,
            packageType = state.draftPackageType
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
