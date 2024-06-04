package com.example.footapp.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.footapp.ui.Order.HomeRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

class UploadWorker @Inject constructor(
    context: Context,
    params: WorkerParameters,
    private val homeRepository: HomeRepository,
) :
    CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val bills = homeRepository.getAllBillInLocal()
        if (bills.isNotEmpty()) {
            var hasError = false
            bills.forEach { bill ->
                try {
                    flow { emit(homeRepository.postBillOffline(bill)) }
                        .catch { e ->
                            // Log the error or handle it as needed
                            Log.e(TAG, e.message.toString())
                            hasError = true
                        }.onCompletion { homeRepository.deleteBill(bill) }
                        .collect {}
                } catch (e: Exception) {
                    // Handle any other exceptions that might occur
                    hasError = true
                }
                // Nếu có lỗi, ngừng việc gửi các hóa đơn còn lại
                if (hasError) {
                    return Result.failure()
                }
            }
        }
        return Result.success()
    }

    companion object {
        // unique name for the work
        const val WORKER_NAME = "UploadWorker"
        private const val TAG = "BackgroundUploadWorker"
    }
}