# ERNO Android Frontend — Architecture Design & Workflow Document

> **Project**: ERNO Android Application  
> **Tech Stack**: Kotlin, Jetpack Compose (BOM 2024.09), Material Design 3, Navigation Compose, ViewModel & StateFlow, Retrofit API Client  
> **Architecture Pattern**: MVVM (Model-View-ViewModel) + Unidirectional Data Flow (UDF)

---

## 🏛️ 1. High-Level Frontend Architecture Design

The ERNO Android application frontend is built using 100% declarative **Jetpack Compose** following the **Modern Android Architecture (MVVM)** recommended by Google.

```
┌────────────────────────────────────────────────────────────────────────┐
│                        Jetpack Compose UI Layer                        │
│  (Screens, Components, Theme, Composables, Navigation Compose Host)   │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │ Unidirectional Data Flow (UDF)
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│                          State Holder / ViewModel                      │
│  (ShopkeeperViewModel, WorkerViewModel, StateFlow<UIState>)           │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │ Repository Requests / Flow Result
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│                             Data Layer                                 │
│  (AuthRepository, ErnoApiService / Retrofit Client, Models & DTOs)    │
└────────────────────────────────────────────────────────────────────────┘
```

### Key Architectural Principles:
1. **Unidirectional Data Flow (UDF)**:
   - Views (Composables) emit user events (e.g. `onPostJobClick`, `onSendOTP`, `onJobSelect`).
   - ViewModels process events, update `StateFlow<UIState>`, and emit immutable state back to views.
2. **Modular Screen Packaging**:
   - Organized by feature area (`auth`, `role`, `shopkeeper`, `worker`, `payment`, `splash`, `components`).
3. **Multi-Role Flow Support**:
   - Dynamic UI states for **Shop Owner (Shopkeeper)** and **Worker (Gig Employee)**.
4. **Adaptive & Responsive Layouts**:
   - Screen layouts use max-width bounds (`widthIn(max = 840.dp)`) and scrollable containers to ensure responsiveness across phones, tablets, and landscape orientation.

---

## 📁 2. Frontend Project Directory Structure

```text
app/src/main/java/com/erno/app/
├── data/
│   ├── model/                  # Core Data Models & DTOs
│   │   ├── AuthModels.kt       # SendOtpRequest, VerifyOtpRequest, AuthResponse
│   │   └── JobModels.kt        # JobPost, WorkerJob, DurationPay, EarningRecord
│   ├── remote/
│   │   └── ErnoApiService.kt   # Retrofit REST API Client Interface
│   └── repository/
│       ├── AuthRepository.kt   # Authentication & OTP Data Repository
│       └── AnalysisRepository.kt
├── navigation/
│   ├── NavGraph.kt             # Navigation Host & Route Composables
│   └── Screen.kt               # Sealed Class defining all Route URIs
└── ui/
    ├── components/             # Reusable UI Components
    │   ├── ErnoBranding.kt     # Official ERNO Logo composable
    │   ├── ErnoTopBar.kt       # Application Top Bar
    │   ├── GoogleSignInButton.kt # Custom Google Sign-In Brand Button
    │   ├── ShopkeeperBottomNavigation.kt # 4-Tab Shopkeeper Nav Bar
    │   ├── WorkerBottomNavigation.kt     # 4-Tab Worker Nav Bar
    │   └── WorkerLocationMapView.kt      # Interactive Canvas Location Route Map
    ├── screens/
    │   ├── auth/
    │   │   ├── RegisterScreen.kt         # Full Name, Mobile, Role Registration
    │   │   └── VerifyOTPScreen.kt        # 6-Box Auto-Advance PIN & 30s Countdown
    │   ├── payment/
    │   │   ├── PaymentCheckoutScreen.kt  # UPI, Cards, Net Banking Settlement
    │   │   └── PaymentSuccessScreen.kt   # Payment Receipt & Confirmation
    │   ├── role/
    │   │   └── RoleSelectionScreen.kt    # Dual Role Toggle (Shopkeeper vs Worker)
    │   ├── shopkeeper/
    │   │   ├── JobSuccessScreen.kt
    │   │   ├── PayStructureScreen.kt     # Hourly Pay Tier Editor
    │   │   ├── PostJobScreen.kt          # Job Posting Wizard with Map Pin
    │   │   ├── PreviewJobScreen.kt       # Job Post Verification Preview
    │   │   ├── ShopkeeperApplicationsScreen.kt # Applicant Review & Hiring
    │   │   ├── ShopkeeperDashboardScreen.kt    # Active Jobs & Metrics
    │   │   ├── ShopkeeperLoginScreen.kt
    │   │   ├── ShopkeeperMyJobsScreen.kt
    │   │   ├── ShopkeeperProfileScreen.kt
    │   │   └── ShopkeeperViewModel.kt     # Shopkeeper State Manager
    │   ├── splash/
    │   │   └── SplashScreen.kt           # Entry Animation & Brand Splash
    │   └── worker/
    │       ├── WorkerAcceptingJobScreen.kt
    │       ├── WorkerEarningsScreen.kt   # Earnings Breakdown & UPI Withdrawal
    │       ├── WorkerHomeScreen.kt       # Location Map & Available Shifts/Deliveries
    │       ├── WorkerJobDetailsScreen.kt # Duration Selector & Map Directions
    │       ├── WorkerLoginScreen.kt
    │       ├── WorkerMyJobsScreen.kt     # Upcoming/Ongoing Jobs with Inline Map
    │       ├── WorkerProfileScreen.kt
    │       └── WorkerViewModel.kt        # Worker State Manager
    └── theme/
        ├── Color.kt                  # ErnoGreen (#11382A), ErnoLime (#80C342)
        ├── Theme.kt                  # Material 3 Theme Wrapper
        └── Type.kt                   # Typography Hierarchy
```

---

## 🎨 3. Design System Tokens: Color Palette, Theme & Typography

### A. Color Palette (`ui/theme/Color.kt`)

| Color Token Name | Hex Code | Visual Swatch | Primary Role / Usage |
| :--- | :--- | :--- | :--- |
| **`ErnoGreen`** | `#11382A` / `#5B9600` | 🟢 Dark Green | Primary Action Buttons, Selected State Borders, Top Bars |
| **`ErnoLime`** | `#A4E637` | 🟢 Bright Lime | Secondary Accent Highlights, Role Selection Cards, Active Tab Indicators |
| **`ErnoGreenLight`** | `#EAF5D8` | 🟢 Light Mint | Badge Backgrounds, Success Indicators, Soft Container Tints |
| **`ErnoNavy`** | `#0B121F` | 🔵 Dark Navy | Worker Theme Backgrounds, Splash Screen Surface, Dark Top Bars |
| **`ErnoBackground`** | `#F8F9FA` | ⚪ Off-White | Application Background Color for Light Mode |
| **`ErnoSurface`** | `#FFFFFF` | ⚪ Pure White | Card Surfaces, Dialogs, Dropdown Containers, Inputs |
| **`ErnoTextPrimary`** | `#0B121F` | ⬛ Deep Charcoal | High-emphasis Headlines, Card Titles, Main Body Text |
| **`ErnoTextSecondary`** | `#6C757D` | 🩶 Cool Gray | Subtitles, Helper Notes, Placeholders, Form Labels |
| **`ErnoInfoBackground`** | `#FFF9E6` | 🟡 Soft Amber | Info Banners, Warning Notices |
| **`ErnoInfoBorder`** | `#FFEBB3` | 🟡 Light Gold | Border strokes for Alert Containers |

---

### B. Material 3 Theme Specification (`ui/theme/Theme.kt`)

The application defines a custom Material 3 Theme wrapper **`ErnoTheme`**:

```kotlin
@Composable
fun ErnoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Disabled to preserve consistent ERNO branding
    content: @Composable () -> Unit
)
```

#### Color Schemes Definition:
* **Light Color Scheme (`LightColorScheme`)**:
  - `primary`: `ErnoLime` (`#A4E637`)
  - `secondary`: `ErnoNavy` (`#0B121F`)
  - `background`: `ErnoBackground` (`#F8F9FA`)
  - `surface`: `ErnoSurface` (`#FFFFFF`)
  - `onPrimary`: `ErnoNavy` (`#0B121F`)
  - `onBackground`: `ErnoTextPrimary` (`#0B121F`)
  - `onSurface`: `ErnoTextPrimary` (`#0B121F`)

* **Dark Color Scheme (`DarkColorScheme`)**:
  - `primary`: `ErnoLime` (`#A4E637`)
  - `secondary`: `ErnoNavy` (`#0B121F`)
  - `background`: `ErnoNavy` (`#0B121F`)
  - `surface`: `ErnoNavy` (`#0B121F`)
  - `onPrimary`: `ErnoNavy` (`#0B121F`)
  - `onSecondary`: `Color.White`
  - `onBackground`: `Color.White`
  - `onSurface`: `Color.White`

---

### C. Typography & Font Specifications (`ui/theme/Type.kt`)

The typography uses clean system sans-serif scaling through Material 3 `Typography`:

| Type Scale Role | Font Weight | Font Size | Line Height | Letter Spacing | Usage Area |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Headline Large** | `Bold` (`FontWeight.W700`) | `32.sp` | `40.sp` | `0.sp` | Main Earnings Numbers, Hero Numbers |
| **Title Large** | `Bold` (`FontWeight.W700`) | `22.sp` – `24.sp` | `28.sp` – `32.sp` | `0.sp` | Screen Headers, Splash Titles |
| **Title Medium** | `Bold` (`FontWeight.W700`) | `18.sp` – `20.sp` | `24.sp` – `26.sp` | `0.15.sp` | Card Titles, Section Headers |
| **Body Large** | `Normal` (`FontWeight.W400`) | `16.sp` | `24.sp` | `0.5.sp` | Primary Text Fields, Form Inputs |
| **Body Medium** | `Medium` (`FontWeight.W500`) | `14.sp` | `20.sp` | `0.25.sp` | Secondary Body, Descriptions |
| **Label Small / Badge**| `Bold` (`FontWeight.W700`) | `11.sp` – `12.sp` | `16.sp` | `0.5.sp` | Status Badges (Active, Completed, Delivery) |

---

## 🔄 4. Frontend Workflows & Screen Transitions

### Workflow 1: Application Entry & Authentication

```
[SplashScreen] (Scale/Alpha Animation)
       │
       ▼
[RoleSelectionScreen] ─── (Choose Role or Create Account)
       │
       ├───► [ShopkeeperLoginScreen] ──┐
       │                               ├───► [VerifyOTPScreen] (30s Timer) ───► [Dashboard]
       ├───► [WorkerLoginScreen] ──────┤
       │                               │
       └───► [RegisterScreen] ─────────┘ (Optionally: Google Sign-In on all screens)
```

1. **Splash Screen**:
   - Runs scale (`0.7f → 1.0f`) and alpha (`0f → 1.0f`) entrance animation.
   - User taps **"Get Started"** to navigate to `RoleSelectionScreen`.
2. **Role Selection & Login**:
   - User chooses **"Shop Owner"** or **"Worker"**.
   - Accepts 10-digit phone number with country code `+91` or **"Continue with Google"**.
3. **Verify OTP Screen**:
   - Displays real destination phone number (`+91 98765 43210`).
   - 6 individual auto-advancing text boxes.
   - 30-second live countdown timer before enabling "Resend OTP".
   - Tapping **"Verify OTP"** routes user to role dashboard.

---

### Workflow 2: Shopkeeper Job Posting & Applicant Hiring Workflow

```
[ShopkeeperDashboard]
       │
       ▼
[PostJobScreen] ──► (Set Category, Title, Description, Shop Map Pin)
       │
       ▼
[PayStructureScreen] ──► (Configure Hourly / Fixed Time Pay Tiers)
       │
       ▼
[PreviewJobScreen] ──► (Verify Map Pin & Pay Structure)
       │
       ▼
[JobSuccessScreen] ──► Automatically Published to Worker App & Dashboard
       │
       ▼
[ShopkeeperApplicationsScreen] ──► Tap "Accept Worker" ──► (Worker Hired & Call Enabled)
```

1. **Job Creation Wizard**:
   - **PostJobScreen**: Selects category (e.g. *Delivery Partner*, *Shop Helper*, *Counter Staff*), enters shop location, and previews **Shop Map Location Pin**.
   - **PayStructureScreen**: Defines fixed duration hours (e.g. 1h = ₹149, 2h = ₹249, 4h = ₹449).
   - **PreviewJobScreen**: Verifies details and publishes job directly.
2. **Instant Worker Sync**:
   - `postJob()` publishes job to `ShopkeeperDashboard` and immediately injects job into `WorkerViewModel.availableJobs`.
3. **Worker Hiring**:
   - Shopkeeper reviews incoming applications in `ShopkeeperApplicationsScreen`.
   - Tapping **"Accept Worker"** hires applicant, locks slot, and enables direct phone calling.

---

### Workflow 3: Worker Job Discovery, Map Navigation & Application

```
[WorkerHomeScreen] (Filter Delivery / Shop Jobs, View Location Map)
       │
       ▼
[WorkerJobDetailsScreen] ──► (Select Fixed Time Duration & View Directions)
       │
       ▼
[WorkerAcceptingJobScreen] ──► (Job Confirmed & Scheduled)
       │
       ▼
[WorkerMyJobsScreen] ──► (Upcoming Shift Card with Inline Interactive Map)
```

1. **Job Discovery**:
   - `WorkerHomeScreen` lists nearby available gig shifts and delivery partner requests with blue **🚚 Delivery** badges and pickup/drop routes.
2. **Job Details & Duration**:
   - Worker views shop distance (`1.2 km away`), work description, map route, and selects fixed duration option (e.g. 2 Hours for ₹189).
3. **Map Directions on Upcoming Jobs**:
   - `WorkerMyJobsScreen` displays upcoming shifts with an expandable **WorkerLocationMapView**.
   - Worker taps **"Open in Maps"** for turn-by-turn Google Maps navigation to shop.

---

### Workflow 4: Job Completion, Settlement Checkout & Worker Payout

```
[Job Completed]
       │
       ├──────────────► [ShopkeeperMyJobsScreen]
       │                        │
       │                        ▼
       │               Tap "Pay Worker ₹X"
       │                        │
       │                        ▼
       │              [PaymentCheckoutScreen] ──► (UPI Apps, Card, Net Banking)
       │                        │
       │                        ▼
       │              [PaymentSuccessScreen] ──► (Receipt Details & Share)
       │
       └──────────────► [WorkerEarningsScreen]
                                │
                                ▼
                       Tap "Withdraw Earnings" ──► (Instant UPI Transfer)
```

1. **Completion Settlement**:
   - Payment checkout is **not forced upfront** during job posting.
   - Payment checkout triggers **only when job is completed**.
2. **Shopkeeper Payment**:
   - Shopkeeper taps **"Pay Worker ₹X (Job Completed)"** on `ShopkeeperMyJobsScreen`.
   - Opens `PaymentCheckoutScreen` with 1-Tap UPI app selection (Google Pay, PhonePe, Paytm, BHIM), card forms, and net banking options.
   - Produces receipt in `PaymentSuccessScreen`.
3. **Worker Earnings Withdrawal**:
   - Worker views accumulated balance in `WorkerEarningsScreen`.
   - Taps **"Withdraw Earnings to UPI / Bank"**, enters UPI ID, and receives instant payout settlement.

---

## 🧩 5. Common UI Components (`ui/components/`)

- **`ErnoLogo`**: Standard official brand logo element.
- **`GoogleSignInButton`**: Standard white border container button with official Google 'G' brand styling.
- **`WorkerLocationMapView`**: Custom Jetpack Compose Canvas rendering main roads, dashed route paths, start/destination pins, and Google Maps intent launcher.
- **`ShopkeeperBottomNavigation` & `WorkerBottomNavigation`**: Role-specific 4-tab bottom navigation bars.

---

## 🧪 6. Testing & Verification Summary

- **Compilation**: `gradle_build("app:compileDebugKotlin")` — **PASSED (0 Errors)**.
- **Orientation Responsiveness**: Tested in portrait, landscape, and tablet screen dimensions (`widthIn(max = 840.dp)`).
- **Git Branch Status**: All frontend architecture, theme tokens, and workflow updates pushed to branch **`Mayank`**.
