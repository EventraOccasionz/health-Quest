package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.navigation.Screen
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.HealthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Retrieve the repository from the application class
        val app = application as HealthQuestApplication
        val repository = app.repository
        
        // Create our ViewModel
        val viewModel = ViewModelProvider(
            this, 
            HealthViewModel.Factory(repository)
        )[HealthViewModel::class.java]

        enableEdgeToEdge()
        
        setContent {
            MyApplicationTheme {
                MainAppContainer(viewModel = viewModel)
            }
        }
    }
}

data class BottomNavItem(
    val title: String,
    val route: String,
    val icon: ImageVector,
    val tag: String
)

@Composable
fun MainAppContainer(viewModel: HealthViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Define home level screens where Bottom Bar should be shown
    val bottomNavItems = listOf(
        BottomNavItem("Home", Screen.Home.route, Icons.Default.Home, "nav_home"),
        BottomNavItem("Book", Screen.BookAppointment.route, Icons.Default.Event, "nav_book"),
        BottomNavItem("Reports", Screen.Reports.route, Icons.Default.Description, "nav_reports"),
        BottomNavItem("Profile", Screen.Profile.route, Icons.Default.Person, "nav_profile")
    )

    val showBottomBar = bottomNavItems.any { it.route == currentRoute }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isWide = maxWidth >= 600.dp

        if (isWide) {
            // Wide screen adaptive layout (tablet/foldable)
            Row(modifier = Modifier.fillMaxSize().background(Color(0xFFFCF8FA))) {
                if (showBottomBar) {
                    NavigationRail(
                        containerColor = Color.White,
                        header = {
                            Box(
                                modifier = Modifier
                                    .padding(vertical = 24.dp)
                                    .size(48.dp)
                                    .background(Color(0xFFFFF0F5), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = "Logo",
                                    tint = Color(0xFFE91E63),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        },
                        modifier = Modifier.fillMaxHeight().testTag("side_nav_rail")
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        bottomNavItems.forEach { item ->
                            val selected = currentRoute == item.route
                            val activeColor = Color(0xFFE91E63)
                            val inactiveColor = Color(0xFF8A90A6)
                            val tintColor = if (selected) activeColor else inactiveColor

                            NavigationRailItem(
                                selected = selected,
                                onClick = {
                                    if (currentRoute != item.route) {
                                        navController.navigate(item.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.title,
                                        tint = tintColor,
                                        modifier = Modifier.size(24.dp)
                                    )
                                },
                                label = {
                                    Text(
                                        text = item.title,
                                        color = tintColor,
                                        fontSize = 12.sp,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                                    )
                                },
                                alwaysShowLabel = true,
                                colors = NavigationRailItemDefaults.colors(
                                    indicatorColor = Color(0xFFFFF0F5)
                                ),
                                modifier = Modifier.testTag(item.tag).padding(vertical = 8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    val contentMaxWidth = if (showBottomBar) 960.dp else 520.dp
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .widthIn(max = contentMaxWidth)
                    ) {
                        AppNavHost(
                            navController = navController,
                            viewModel = viewModel,
                            innerPadding = PaddingValues(0.dp)
                        )
                    }
                }
            }
        } else {
            // Standard compact mobile layout
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    if (showBottomBar) {
                        Surface(
                            color = Color.White,
                            tonalElevation = 8.dp,
                            shadowElevation = 8.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("bottom_nav_bar")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .navigationBarsPadding()
                                    .padding(vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                bottomNavItems.forEach { item ->
                                    val selected = currentRoute == item.route
                                    val activeColor = Color(0xFFE91E63)
                                    val inactiveColor = Color(0xFF8A90A6)
                                    val tintColor = if (selected) activeColor else inactiveColor
                                    
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .clickable {
                                                if (currentRoute != item.route) {
                                                    navController.navigate(item.route) {
                                                        popUpTo(navController.graph.findStartDestination().id) {
                                                            saveState = true
                                                        }
                                                        launchSingleTop = true
                                                        restoreState = true
                                                    }
                                                }
                                            }
                                            .padding(horizontal = 12.dp)
                                            .testTag(item.tag)
                                    ) {
                                        Icon(
                                            imageVector = item.icon,
                                            contentDescription = item.title,
                                            tint = tintColor,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = item.title,
                                            color = tintColor,
                                            fontSize = 12.sp,
                                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                                        )
                                        if (selected) {
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Box(
                                                modifier = Modifier
                                                    .size(5.dp)
                                                    .background(activeColor, CircleShape)
                                            )
                                        } else {
                                            Spacer(modifier = Modifier.height(7.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            ) { innerPadding ->
                AppNavHost(
                    navController = navController,
                    viewModel = viewModel,
                    innerPadding = innerPadding
                )
            }
        }
    }
}

@Composable
fun AppNavHost(
    navController: androidx.navigation.NavHostController,
    viewModel: HealthViewModel,
    innerPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = Modifier.padding(innerPadding)
    ) {
        // 1. Splash Screen
        composable(Screen.Splash.route) {
            SplashScreen(
                onTimeout = {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // 2. Welcome Screen
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onNext = {
                    navController.navigate(Screen.GetStarted.route)
                }
            )
        }

        // 3. Get Started Screen
        composable(Screen.GetStarted.route) {
            GetStartedScreen(
                onGetStarted = {
                    navController.navigate(Screen.SignIn.route)
                }
            )
        }

        // 4. Sign In Screen
        composable(Screen.SignIn.route) {
            SignInScreen(
                viewModel = viewModel,
                onSignInSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        // 5. Home Screen
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateTo = { route -> navController.navigate(route) }
            )
        }

        // 6. Book Appointment Screen
        composable(Screen.BookAppointment.route) {
            BookAppointmentScreen(
                viewModel = viewModel
            )
        }

        // 7. Reports Screen
        composable(Screen.Reports.route) {
            ReportsScreen(
                viewModel = viewModel
            )
        }

        // 8. Health Packages Screen
        composable(Screen.HealthPackages.route) {
            HealthPackagesScreen(
                viewModel = viewModel
            )
        }

        // 9. Profile Screen
        composable(Screen.Profile.route) {
            ProfileScreen(
                viewModel = viewModel,
                onLogout = {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateTo = { route -> navController.navigate(route) }
            )
        }

        // 10. Support Screen
        composable(Screen.Support.route) {
            SupportScreen(
                viewModel = viewModel
            )
        }

        // 11. Notifications Screen
        composable(Screen.Notifications.route) {
            NotificationsScreen(
                viewModel = viewModel
            )
        }

        // 12. Wallet Screen
        composable(Screen.Wallet.route) {
            WalletScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onNotificationClick = { navController.navigate(Screen.Notifications.route) }
            )
        }
    }
}
