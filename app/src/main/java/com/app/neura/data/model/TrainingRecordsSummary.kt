package com.app.neura.data.model

data class TrainingRecordsSummary(
    val highlightTitle: String,
    val highlightMessage: String,
    val items: List<TrainingRecordItem>
)

data class TrainingRecordItem(
    val title: String,
    val value: String,
    val description: String,
    val icon: String
)