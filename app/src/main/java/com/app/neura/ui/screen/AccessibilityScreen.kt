package com.app.neura.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.neura.data.model.AccessibilitySettings
import com.app.neura.data.model.TextScale
import com.app.neura.data.model.ThemeMode
import com.app.neura.data.model.ColorVisionMode
import com.app.neura.ui.component.SectionCard
import com.app.neura.ui.component.SelectableOptionButton
import com.app.neura.ui.component.SecondaryActionButton

@Composable
fun AccessibilityScreen(
    settings: AccessibilitySettings,
    onSettingsChange: (AccessibilitySettings) -> Unit,
    onBack: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .navigationBarsPadding()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                Text(
                    text = "Accessibility",
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            item {
                Text(
                    text = "Adjust the app appearance to improve readability.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            item {
                SectionCard(title = "Theme") {
                    SelectableOptionButton(
                        text = "System",
                        selected = settings.themeMode == ThemeMode.SYSTEM,
                        onClick = {
                            onSettingsChange(settings.copy(themeMode = ThemeMode.SYSTEM))
                        }
                    )

                    SelectableOptionButton(
                        text = "Light",
                        selected = settings.themeMode == ThemeMode.LIGHT,
                        onClick = {
                            onSettingsChange(settings.copy(themeMode = ThemeMode.LIGHT))
                        }
                    )

                    SelectableOptionButton(
                        text = "Dark",
                        selected = settings.themeMode == ThemeMode.DARK,
                        onClick = {
                            onSettingsChange(settings.copy(themeMode = ThemeMode.DARK))
                        }
                    )
                }
            }

            item {
                SectionCard(title = "Text size") {
                    SelectableOptionButton(
                        text = "Small",
                        selected = settings.textScale == TextScale.SMALL,
                        onClick = {
                            onSettingsChange(settings.copy(textScale = TextScale.SMALL))
                        }
                    )

                    SelectableOptionButton(
                        text = "Normal",
                        selected = settings.textScale == TextScale.NORMAL,
                        onClick = {
                            onSettingsChange(settings.copy(textScale = TextScale.NORMAL))
                        }
                    )

                    SelectableOptionButton(
                        text = "Large",
                        selected = settings.textScale == TextScale.LARGE,
                        onClick = {
                            onSettingsChange(settings.copy(textScale = TextScale.LARGE))
                        }
                    )

                    SelectableOptionButton(
                        text = "Extra large",
                        selected = settings.textScale == TextScale.EXTRA_LARGE,
                        onClick = {
                            onSettingsChange(settings.copy(textScale = TextScale.EXTRA_LARGE))
                        }
                    )
                }

            }

            item {
                SectionCard(title = "Contrast") {
                    SelectableOptionButton(
                        text = "Standard contrast",
                        selected = !settings.highContrast,
                        onClick = {
                            onSettingsChange(settings.copy(highContrast = false))
                        }
                    )

                    SelectableOptionButton(
                        text = "High contrast",
                        selected = settings.highContrast,
                        onClick = {
                            onSettingsChange(settings.copy(highContrast = true))
                        }
                    )
                }
            }

            item {
                SectionCard(title = "Color vision") {
                    SelectableOptionButton(
                        text = "Default",
                        selected = settings.colorVisionMode == ColorVisionMode.DEFAULT,
                        onClick = {
                            onSettingsChange(settings.copy(colorVisionMode = ColorVisionMode.DEFAULT))
                        }
                    )

                    SelectableOptionButton(
                        text = "Protanopia",
                        selected = settings.colorVisionMode == ColorVisionMode.PROTANOPIA,
                        onClick = {
                            onSettingsChange(settings.copy(colorVisionMode = ColorVisionMode.PROTANOPIA))
                        }
                    )

                    SelectableOptionButton(
                        text = "Deuteranopia",
                        selected = settings.colorVisionMode == ColorVisionMode.DEUTERANOPIA,
                        onClick = {
                            onSettingsChange(settings.copy(colorVisionMode = ColorVisionMode.DEUTERANOPIA))
                        }
                    )

                    SelectableOptionButton(
                        text = "Tritanopia",
                        selected = settings.colorVisionMode == ColorVisionMode.TRITANOPIA,
                        onClick = {
                            onSettingsChange(settings.copy(colorVisionMode = ColorVisionMode.TRITANOPIA))
                        }
                    )
                }
            }

            item {
                SectionCard(title = "Neuroaccessibility") {
                    SelectableOptionButton(
                        text = if (settings.calmMode) "Calm mode: On" else "Calm mode: Off",
                        selected = settings.calmMode,
                        onClick = {
                            onSettingsChange(settings.copy(calmMode = !settings.calmMode))
                        }
                    )

                    SelectableOptionButton(
                        text = if (settings.focusMode) "Focus mode: On" else "Focus mode: Off",
                        selected = settings.focusMode,
                        onClick = {
                            onSettingsChange(settings.copy(focusMode = !settings.focusMode))
                        }
                    )

                    SelectableOptionButton(
                        text = if (settings.readingHelper) "Reading helper: On" else "Reading helper: Off",
                        selected = settings.readingHelper,
                        onClick = {
                            onSettingsChange(settings.copy(readingHelper = !settings.readingHelper))
                        }
                    )

                    SelectableOptionButton(
                        text = if (settings.confirmDestructiveActions) {
                            "Confirm delete actions: On"
                        } else {
                            "Confirm delete actions: Off"
                        },
                        selected = settings.confirmDestructiveActions,
                        onClick = {
                            onSettingsChange(
                                settings.copy(
                                    confirmDestructiveActions = !settings.confirmDestructiveActions
                                )
                            )
                        }
                    )

                    SelectableOptionButton(
                        text = if (settings.reduceMotion) "Reduce motion: On" else "Reduce motion: Off",
                        selected = settings.reduceMotion,
                        onClick = {
                            onSettingsChange(settings.copy(reduceMotion = !settings.reduceMotion))
                        }
                    )
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Preview",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(
                            text = "This is how challenge text will look.",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Text(
                            text = "Readable text helps reduce visual fatigue.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            item {
                SecondaryActionButton(
                    text = "Back",
                    onClick = onBack
                )
            }
        }
    }
}