package com.example.footapp

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.footapp.ui.Order.HomeRepository
import com.example.footapp.worker.UploadWorker
import javax.inject.Inject

class UploadWorkerFactory @Inject constructor(
    private val homeRepository: HomeRepository
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            UploadWorker::class.java.name -> UploadWorker(
                appContext,
                workerParameters,
                homeRepository
            )

            else -> null
        }
    }
}