package com.app.neura.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.neura.data.model.AccessibilitySettings
import com.app.neura.data.model.TextScale
import com.app.neura.data.model.ThemeMode
import com.app.neura.data.model.ColorVisionMode

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
                SettingsCard(title = "Theme") {
                    OptionButton(
                        text = "System",
                        selected = settings.themeMode == ThemeMode.SYSTEM,
                        onClick = {
                            onSettingsChange(settings.copy(themeMode = ThemeMode.SYSTEM))
                        }
                    )

                    OptionButton(
                        text = "Light",
                        selected = settings.themeMode == ThemeMode.LIGHT,
                        onClick = {
                            onSettingsChange(settings.copy(themeMode = ThemeMode.LIGHT))
                        }
                    )

                    OptionButton(
                        text = "Dark",
                        selected = settings.themeMode == ThemeMode.DARK,
                        onClick = {
                            onSettingsChange(settings.copy(themeMode = ThemeMode.DARK))
                        }
                    )
                }
            }

            item {
                SettingsCard(title = "Text size") {
                    OptionButton(
                        text = "Small",
                        selected = settings.textScale == TextScale.SMALL,
                        onClick = {
                            onSettingsChange(settings.copy(textScale = TextScale.SMALL))
                        }
                    )

                    OptionButton(
                        text = "Normal",
                        selected = settings.textScale == TextScale.NORMAL,
                        onClick = {
                            onSettingsChange(settings.copy(textScale = TextScale.NORMAL))
                        }
                    )

                    OptionButton(
                        text = "Large",
                        selected = settings.textScale == TextScale.LARGE,
                        onClick = {
                            onSettingsChange(settings.copy(textScale = TextScale.LARGE))
                        }
                    )

                    OptionButton(
                        text = "Extra large",
                        selected = settings.textScale == TextScale.EXTRA_LARGE,
                        onClick = {
                            onSettingsChange(settings.copy(textScale = TextScale.EXTRA_LARGE))
                        }
                    )
                }

            }

            item {
                SettingsCard(title = "Contrast") {
                    OptionButton(
                        text = "Standard contrast",
                        selected = !settings.highContrast,
                        onClick = {
                            onSettingsChange(settings.copy(highContrast = false))
                        }
                    )

                    OptionButton(
                        text = "High contrast",
                        selected = settings.highContrast,
                        onClick = {
                            onSettingsChange(settings.copy(highContrast = true))
                        }
                    )
                }
            }

            item {
                SettingsCard(title = "Color vision") {
                    OptionButton(
                        text = "Default",
                        selected = settings.colorVisionMode == ColorVisionMode.DEFAULT,
                        onClick = {
                            onSettingsChange(settings.copy(colorVisionMode = ColorVisionMode.DEFAULT))
                        }
                    )

                    OptionButton(
                        text = "Protanopia",
                        selected = settings.colorVisionMode == ColorVisionMode.PROTANOPIA,
                        onClick = {
                            onSettingsChange(settings.copy(colorVisionMode = ColorVisionMode.PROTANOPIA))
                        }
                    )

                    OptionButton(
                        text = "Deuteranopia",
                        selected = settings.colorVisionMode == ColorVisionMode.DEUTERANOPIA,
                        onClick = {
                            onSettingsChange(settings.copy(colorVisionMode = ColorVisionMode.DEUTERANOPIA))
                        }
                    )

                    OptionButton(
                        text = "Tritanopia",
                        selected = settings.colorVisionMode == ColorVisionMode.TRITANOPIA,
                        onClick = {
                            onSettingsChange(settings.copy(colorVisionMode = ColorVisionMode.TRITANOPIA))
                        }
                    )
                }
            }

            item {
                SettingsCard(title = "Neuroaccessibility") {
                    OptionButton(
                        text = if (settings.calmMode) "Calm mode: On" else "Calm mode: Off",
                        selected = settings.calmMode,
                        onClick = {
                            onSettingsChange(settings.copy(calmMode = !settings.calmMode))
                        }
                    )

                    OptionButton(
                        text = if (settings.focusMode) "Focus mode: On" else "Focus mode: Off",
                        selected = settings.focusMode,
                        onClick = {
                            onSettingsChange(settings.copy(focusMode = !settings.focusMode))
                        }
                    )

                    OptionButton(
                        text = if (settings.readingHelper) "Reading helper: On" else "Reading helper: Off",
                        selected = settings.readingHelper,
                        onClick = {
                            onSettingsChange(settings.copy(readingHelper = !settings.readingHelper))
                        }
                    )

                    OptionButton(
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

                    OptionButton(
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
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Back")
                }
            }
        }
    }
}

@Composable
private fun SettingsCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )

            content()
        }
    }
}

@Composable
private fun OptionButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(if (selected) "• $text" else text)
    }
}