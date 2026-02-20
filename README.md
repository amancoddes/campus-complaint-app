# campus-complaint-app
An Android application for campus students to register and track complaints digitally using Kotlin and Jetpack Compose.




## Authentication Flow
```
App Launch
↓
SplashScreen
↓
Check Authentication State
├── If user already logged in → HomeScreen
└── If not logged in
↓
LoginScreen
├── Existing user → Login → HomeScreen
└── New user → SignUpScreen
                    ↓
        EmailVerificationScreen
                    ↓
                Is Verified -> ProfileSetUpScreen -> HomeScreen

```
## Home Screen Structure
```
HomeScreen
├── Bottom Navigation Bar
│      ├── Home Icon → HomeScreen
│      ├── User Complaints → UserAllComplaintsScreen
│      └── Profile Icon → ProfileScreen
│
└── Floating Action Button (FAB)
↓
Open Camera

```
## Complaint Reporting flow
```

FAB Click
↓
Camera Open
↓
Picture Captured
↓
Complaint Type Selection Screen
├── Indoor Complaint
└── Outdoor Complaint
↓
Specific Preview Screen
↓
Send Complaint
↓
├── If New Complaint → SuccessScreen
└── If Already Exists → PriorityIncreaseScreen
```

## Presentation Layer
```

|      feature       |         screen              |          viewModel file             |
|________________________________________________________________________________________|
| App Entry/Routing  | SplashScreen.kt              | SplashScreenViewModel.kt           |
| Authentication     | LoginScreen.kt               | LoginScreenViewModel.kt            |
| Authentication     | SignUpScreen.kt              | SignUpScreenViewModel.kt           |
| Email Verification | EmailVerificationScreen.kt   | SignUpScreenViewModel. kt          |
| Profile Setup      | ProfileSetupScreen.kt        | ProfileSetupScreenViewModel.kt     |
| Home               | HomeScreen.kt                | HomeScreenViewModel.kt             |
| User Complaints    | UserAllComplaintsScreen.kt   | UserComplaintsViewModel.kt         |
| Profile            | ProfileScreen.kt             | ProfileScreenViewModel.kt          |
| Complaint type     | ComplaintTypeScreen.kt       |                                    |
| Complaint Preview  | ComplaintPreviewScreen.kt    | ComplaintPreviewScreenViewModel.kt |
```



## Bottom bar and Floating Action Button
NavigationBottomBarAndFAB.kt

## Navigation Graph
Parent Navigation Graph | MainNavGraphSetup.kt

## Child Navigation Graph
## Specific Screen Navigation Graph
```

| Screens                |  child Nav graphs               |
|_________________________________________________________|
| Home                   | ChildHomeNavigationGraph.kt    |
| Profile Screen         | ChildProfileNavigationGraph.kt |
| User Complaints Screen | ChildUserComplaintsGraph.kt    |
```

## Send complaints Sub Navigation Graph
ChildAddReportNavigationGraph.kt










## Features
- User Authentication (Firebase)
- Location-based complaint validation
- Room database caching
- MVVM Architecture
- Unit Testing with MockK and Coroutines

## Tech Stack
- Kotlin
- Jetpack Compose
- Coroutines
- Flow
- Room
- Firebase
- MockK (Unit Testing)

## Testing
Comprehensive ViewModel testing using:
- MockK
- Coroutine Test Dispatcher
- StateFlow & SharedFlow validation
- Success & Failure path coverage