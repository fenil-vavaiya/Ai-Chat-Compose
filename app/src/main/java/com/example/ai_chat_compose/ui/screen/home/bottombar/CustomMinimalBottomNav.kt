package com.example.ai_chat_compose.ui.screen.home.bottombar

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ai_chat_compose.ui.theme.Grey
import com.example.ai_chat_compose.ui.theme.Nunito
import com.example.ai_chat_compose.ui.theme.Theme

@Composable
fun CustomMinimalBottomNav(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(horizontal = 5.dp, vertical = 10.dp)


        ) {
            listOf(
                Screens.Chat,
                Screens.ImageSummary,
                Screens.News,
            ).forEach { screen ->
                val isSelected = currentRoute == screen.route

                NavItem(
                    isSelected, screen, Modifier
                        .weight(1f)
                        .height(60.dp)
                ) {
                    if (!isSelected) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NavItem(isSelected: Boolean, screen: Screens, modifier: Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current
            ) { onClick() }
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Icon(
            painterResource(screen.icon),
            contentDescription = screen.label,
            tint = if (isSelected) Theme else Grey,
            modifier = Modifier.size(30.dp)
        )
        Text(
            text = screen.label,
            style = TextStyle(fontFamily = Nunito, fontWeight = FontWeight.Normal),
            fontSize = 14.sp, color = if (isSelected) Theme else Grey
        )
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(2.dp)
                    .background(color = Color.Black)
                    .align(Alignment.CenterHorizontally),
            )
        }
    }
}

