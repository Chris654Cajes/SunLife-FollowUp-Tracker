# FollowUp Tracker

A professional Android application designed to help sales professionals and business representatives manage their prospect follow-ups efficiently. The app provides a clean, intuitive interface for tracking prospects, scheduling follow-ups, and managing pre-written message templates.

## Features

- **Prospect Management**: Add, edit, and delete prospects with contact information
- **Status Tracking**: Categorize prospects as Hot, Warm, or Cold
- **Follow-up Scheduling**: Set automated reminders for follow-up activities
- **Message Templates**: Create and manage pre-written message templates
- **Notification System**: Receive timely notifications for scheduled follow-ups
- **Material Design**: Modern, clean UI following Material Design principles
- **Responsive Layout**: Optimized for various screen sizes and orientations

## Screens

### Main Screen
- List of all prospects with their current status
- Floating action button to add new prospects
- Empty state with helpful guidance

### Add/Edit Prospect
- Input fields for prospect name and contact information
- Status selection (Hot/Warm/Cold)
- Follow-up date and time scheduling

### Message Templates
- Create custom message templates with placeholders
- Edit and delete existing templates
- Support for prospect name personalization

## Technical Details

### Architecture
- **Language**: Kotlin
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34 (Android 14)
- **Build Tools**: Gradle with Android Gradle Plugin 8.2.0

### Key Components
- **MVVM Architecture**: Clean separation of concerns
- **Room Database**: Local data persistence
- **RecyclerView**: Efficient list rendering
- **Material Components**: Modern UI elements
- **AlarmManager**: Scheduled notifications
- **Broadcast Receivers**: Boot and alarm handling

### Permissions
- `POST_NOTIFICATIONS`: Display follow-up notifications
- `SCHEDULE_EXACT_ALARM`: Precise alarm scheduling
- `USE_EXACT_ALARM`: Exact alarm functionality
- `RECEIVE_BOOT_COMPLETED`: Handle device reboots

## Installation

1. Clone this repository
2. Open the project in Android Studio
3. Sync the project with Gradle files
4. Build and run the application

## Usage

1. **Adding Prospects**: Tap the floating action button (+) to add a new prospect
2. **Managing Status**: Select appropriate status (Hot/Warm/Cold) for each prospect
3. **Scheduling Follow-ups**: Set follow-up dates when adding or editing prospects
4. **Message Templates**: Access pre-written messages from the main menu
5. **Notifications**: Enable notifications to receive follow-up reminders

## Contributing

This project is maintained by SunLife development team. For feature requests and bug reports, please follow the internal contribution guidelines.

## License

Copyright © 2025 SunLife. All rights reserved.

## Support

For technical support and questions, please contact the development team through internal channels.
