package com.Pacvazo7.Rilcer.presentation.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Pacvazo7.Rilcer.data.sample.SampleData
import com.Pacvazo7.Rilcer.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ArtifactCollectionViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    val state: StateFlow<ArtifactCollectionUiState> = progressRepository.progress
        .map { progress ->
            val sets = SampleData.artifactSets.map { set ->
                ArtifactSetState(
                    id = set.id,
                    artifacts = set.artifacts,
                    owned = progress.ownedArtifacts,
                    complete = set.artifacts.all { progress.ownedArtifacts.contains(it.id) }
                )
            }
            ArtifactCollectionUiState(
                sets = sets,
                ownedCount = progress.ownedArtifacts.size
            )
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, ArtifactCollectionUiState())
}
