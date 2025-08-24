package com.example.rickandmortyapp.presentation.characterlist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rickandmortyapp.domain.model.CharacterFilter
import com.example.rickandmortyapp.domain.model.CharacterGender
import com.example.rickandmortyapp.domain.model.CharacterSpecies
import com.example.rickandmortyapp.domain.model.CharacterStatus
import com.example.rickandmortyapp.domain.model.CharacterType
import com.example.rickandmortyapp.presentation.utils.displayFilterTitle
import com.example.rickandmortyapp.presentation.utils.displayName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    characterFilter: CharacterFilter,
    onFilterChange: (CharacterFilter) -> Unit,
    onApplyFilters: () -> Unit,
    onResetFilters: () -> Unit,
    onComponentClose: () -> Unit,
    sheetState: SheetState,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onComponentClose,
        sheetState = sheetState,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Filters",
                style = MaterialTheme.typography.titleLarge
            )
            FilterGroup(
                title = CharacterGender::class.java.displayFilterTitle(),
                options = CharacterGender.entries,
                selected = characterFilter.gender,
                onSelectedChange = { characterGender ->
                    onFilterChange(characterFilter.copy(gender = characterGender))
                },
                displayName = CharacterGender::displayName
            )
            HorizontalDivider()
            FilterGroup(
                title = CharacterSpecies::class.java.displayFilterTitle(),
                options = CharacterSpecies.entries,
                selected = characterFilter.species,
                onSelectedChange = { characterSpecies ->
                    onFilterChange(characterFilter.copy(species = characterSpecies))
                },
                displayName = CharacterSpecies::displayName
            )
            HorizontalDivider()
            FilterGroup(
                title = CharacterStatus::class.java.displayFilterTitle(),
                options = CharacterStatus.entries,
                selected = characterFilter.status,
                onSelectedChange = { characterStatus ->
                    onFilterChange(characterFilter.copy(status = characterStatus))
                },
                displayName = CharacterStatus::displayName
            )
            HorizontalDivider()
            FilterGroup(
                title = CharacterType::class.java.displayFilterTitle(),
                options = CharacterType.entries,
                selected = characterFilter.type,
                onSelectedChange = { characterType ->
                    onFilterChange(characterFilter.copy(type = characterType))
                },
                displayName = CharacterType::displayName
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = onResetFilters,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Reset")
                }
                Button(
                    onClick = onApplyFilters,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Apply")
                }
            }
        }
    }
}

@Composable
fun <T : Enum<T>> FilterGroup(
    title: String,
    options: List<T>,
    selected: T?,
    onSelectedChange: (T?) -> Unit,
    displayName: (T) -> String
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { option ->
                FilterChip(
                    selected = option == selected,
                    onClick = {
                        if (option == selected) {
                            onSelectedChange(null)
                        } else {
                            onSelectedChange(option)
                        }
                    },
                    label = {
                        Text(displayName(option))
                    }
                )
            }
        }
    }
}