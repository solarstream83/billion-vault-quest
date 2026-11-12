package com.Pacvazo7.Rilcer.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StatRow(
    cards: List<StatCardData>,
    modifier: Modifier = Modifier
) {
    val visible = cards.filter { it.amount != 0 }
    if (visible.size < 2) return
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        visible.forEach { card ->
            StatCardCell(card)
        }
    }
}

@Composable
private fun RowScope.StatCardCell(card: StatCardData) {
    StatCard(data = card, modifier = Modifier.weight(1f))
}
