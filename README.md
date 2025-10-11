# Calculator Android App - Midterm Project
Course: CSCI-C 323: Mobile App Development

Semester: Fall 2025

Group Number: 7

# Group Contribution

Member 1: Prabhav Gudapati
Was responsible for all XML layout design and resource file creation. Designed and implemented the user interface for both the main calculator screen and the settings screen, ensuring a clean and intuitive layout that meets all project requirements. The work included creating activity_main.xml with a ConstraintLayout for the upper section and GridLayout for the calculator buttons, as well as activity_setting.xml for the settings interface with theme selection, font size options, and color customization. Additionally, they created and configured all resource files including themes.xml for both light and dark modes, colors.xml for consistent color schemes throughout the app, strings.xml for text resources, and attrs.xml for custom theme attributes. Ensured the UI was responsive across different screen orientations and sizes, implemented proper button sizing and spacing in the GridLayout, and created a cohesive visual design that works well in both light and dark themes. Also collaborated on the README documentation

Member 2: Mihir Sisodiya
Handled all Kotlin programming and application logic implementation. Developed MainActivity.kt which contains the complete calculator functionality including all arithmetic operations (addition, subtraction, multiplication, division), special functions (square root, decimal point, backspace, clear with confirmation), input validation to enforce the 5-digit limit, decimal precision rounding to 5 places, and proper activity lifecycle management to reset the calculator when the app becomes invisible. Also created SettingsActivity.kt to manage user preferences, implementing SharedPreferences for persistent storage of user settings, theme switching functionality between Light and Dark modes, font size and color customization options, proper loading and saving of settings, and communication between activities using startActivityForResult. Additionally, implemented error handling for edge cases like division by zero and negative square roots, ensured smooth theme transitions and UI updates, and thoroughly tested all calculator functions and edge cases. Also contributed significantly to the README file.
