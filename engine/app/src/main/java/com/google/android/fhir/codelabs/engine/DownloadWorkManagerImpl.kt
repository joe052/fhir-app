package com.google.android.fhir.codelabs.engine

import com.google.android.fhir.sync.DownloadWorkManager
import com.google.android.fhir.sync.Request
import org.hl7.fhir.r4.model.Resource
import org.hl7.fhir.r4.model.ResourceType

class DownloadWorkManagerImpl: DownloadWorkManager {
    override suspend fun getNextRequest(): Request? {
        TODO("Not yet implemented")
    }

    override suspend fun getSummaryRequestUrls(): Map<ResourceType, String> {
        TODO("Not yet implemented")
    }

    override suspend fun processResponse(response: Resource): Collection<Resource> {
        TODO("Not yet implemented")
    }
}