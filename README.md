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

|      feature       |         screen              |          viewModel                  |
|________________________________________________________________________________________|
| App Entry/Routing  | SplashScreen.kt              | SplashScreenViewModel.kt           |
| Authentication     | LoginScreen.kt               | LoginScreenViewModel.kt            |
| Authentication     | SignUpScreen.kt              | SignUpScreenViewModel.kt           |
| Email Verification | EmailVerificationScreen.kt   | SignUpScreenViewModel.kt           |
| Profile Setup      | ProfileSetupScreen.kt        | ProfileSetupScreenViewModel.kt     |
| Home               | HomeScreen.kt                | HomeScreenViewModel.kt             |
| User Complaints    | UserAllComplaintsScreen.kt   | UserAllComplaintsScreenViewModel.kt|
| User Profile       | ProfileScreen.kt             | ProfileScreenViewModel.kt          |
| Complaint type     | ComplaintTypeScreen.kt       |                                    |
| ComplaintOutdoor Preview  | ComplaintPreviewOutdoorScreen.kt    | ComplaintPreviewScreenViewModel.kt |
| ComplaintIndoor Preview   | ComplaintPreviewIndoorScreen.kt     | ComplaintPreviewScreenViewModel.kt |
```


## ViewModel Dependencies
```
|     ViewModel                    |       Dependencies            |
|__________________________________________________________________|
| SplashScreenViewModel            |  FirebaseAuth                 |
| LoginScreenViewModel             |  UserAuthRepository           |
| SignUpScreenViewModel            |  UserAuthRepository           |
| ProfileSetupScreenViewModel      |  UserRepository               |
| UserAllComplaintsScreenViewModel |  UserComplaintsReadRepository |
| ProfileScreenViewModel           |  ProfileRepository            |
| ComplaintPreviewScreenViewModel  |  ComplaintSubmissionRepository,UserComplaintsReadRepository,LocationFetcher,LocationValidator |
```

## Bottom bar and Floating Action Button
NavigationBottomBarAndFAB.kt





## Navigation Graph
```
Parent Navigation Graph ->  MainNavGraphSetup.kt

Child Navigation Graph for specif screen
| Screens                |  child Nav graphs             |
|________________________________________________________|
| Home                   | ChildHomeNavigationGraph      |
| Profile Screen         | ChildProfileNavigationGraph   |
| User Complaints Screen | ChildUserComplaintsGraph      |
| Floating Action Button | ChildAddReportNavigationGraph |
```


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