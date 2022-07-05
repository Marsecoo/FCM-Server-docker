package com.raywenderlich.android.drinkit.schedule
import android.util.Log
import androidx.work.Worker
import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters

class JobWorker (appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
    companion object {
        private val TAG = "JobWorker"
    }
    override fun doWork(): ListenableWorker.Result {
        Log.d(TAG, "Performing long running task in scheduled job")
        // TODO(developer): add long running task here.
        return ListenableWorker.Result.success()
    }
}