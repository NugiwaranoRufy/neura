package com.app.neura.ui.component

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.app.neura.ui.screen.NeuraDestinations

data class NeuraBottomBarItem(
    val label: String,
    val icon: String,
    val route: String
)

@Composable
fun NeuraBottomBar(
    currentRoute: String?,
    calmMode: Boolean,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        NeuraBottomBarItem(
            label = "Home",
            icon = if (calmMode) "Home" else "🏠",
            route = NeuraDestinations.Home.route
        ),
        NeuraBottomBarItem(
            label = "Discover",
            icon = if (calmMode) "Discover" else "🌍",
            route = NeuraDestinations.Discover.route
        ),
        NeuraBottomBarItem(
            label = "Missions",
            icon = if (calmMode) "Missions" else "✅",
            route = NeuraDestinations.Missions.route
        ),
        NeuraBottomBarItem(
            label = "Activity",
            icon = if (calmMode) "Activity" else "📰",
            route = NeuraDestinations.Activity.route
        ),
        NeuraBottomBarItem(
            label = "Profile",
            icon = if (calmMode) "Profile" else "👤",
            route = NeuraDestinations.Profile.route
        )
    )

    NavigationBar(
        modifier = Modifier.navigationBarsPadding(),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route

            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        onNavigate(item.route)
                    }
                },
                icon = {
                    Text(
                        text = item.icon,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = NavigationBarItemDefaults.colors()
            )
        }
    }
}