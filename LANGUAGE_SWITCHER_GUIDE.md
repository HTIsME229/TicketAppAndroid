# Hướng Dẫn Tính Năng Chuyển Đổi Ngôn Ngữ

## 📋 Tổng Quan

Ứng dụng TicketApp đã được tích hợp tính năng chuyển đổi ngôn ngữ trong Settings, cho phép người dùng thay đổi ngôn ngữ hiển thị mà không cần thay đổi cài đặt hệ thống.

## ✨ Tính Năng

### 1. Chuyển Đổi Ngôn Ngữ Trong App
- Người dùng có thể chọn ngôn ngữ từ Settings
- Hỗ trợ 2 ngôn ngữ: **English** và **Tiếng Việt**
- Lưu lựa chọn ngôn ngữ vào SharedPreferences
- Tự động áp dụng ngôn ngữ khi khởi động lại app

### 2. Giao Diện Thân Thiện
- Dialog chọn ngôn ngữ với RadioButton
- Hiển thị ngôn ngữ hiện tại
- Xác nhận trước khi thay đổi
- Thông báo yêu cầu khởi động lại app

## 🗂️ Cấu Trúc File

### 1. Layout Files

#### `dialog_language_selector.xml`
Dialog để chọn ngôn ngữ với:
- Tiêu đề "Select Language"
- RadioGroup với 2 options: English và Tiếng Việt
- Nút Cancel và Confirm

#### `fragment_settings.xml` (Updated)
Thêm mục "Language" trong phần "App Settings"

### 2. Java Classes

#### `LocaleHelper.java`
Utility class quản lý ngôn ngữ:
```java
// Lưu ngôn ngữ đã chọn
LocaleHelper.setLocale(context, "vi");

// Lấy ngôn ngữ hiện tại
String lang = LocaleHelper.getCurrentLanguage(context);

// Lấy ngôn ngữ đã lưu
String lang = LocaleHelper.getPersistedLanguage(context);
```

**Chức năng:**
- `setLocale()` - Đặt và lưu ngôn ngữ
- `getPersistedLanguage()` - Lấy ngôn ngữ đã lưu
- `getCurrentLanguage()` - Lấy ngôn ngữ hiện tại
- `getLanguageName()` - Lấy tên hiển thị của ngôn ngữ
- `updateResources()` - Cập nhật resources theo ngôn ngữ

#### `SettingsFragment.java` (Updated)
Thêm chức năng:
- Click listener cho mục Language
- `showLanguageDialog()` - Hiển thị dialog chọn ngôn ngữ
- `showRestartDialog()` - Hiển thị dialog yêu cầu restart

### 3. Activities (Updated)

Tất cả Activities đã được cập nhật để hỗ trợ ngôn ngữ:

```java
@Override
protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(LocaleHelper.setLocale(newBase, 
        LocaleHelper.getPersistedLanguage(newBase)));
}
```

**Danh sách Activities đã cập nhật:**
- ✅ `MainActivity.java`
- ✅ `AuthenticationActivity.java`
- ✅ `Splash.java`
- ✅ `OnBoarding.java`

### 4. String Resources (Updated)

#### Tiếng Anh (`values/strings.xml`)
```xml
<string name="txt_language">Language</string>
<string name="txt_app_settings">App Settings</string>
<string name="txt_english">English</string>
<string name="txt_vietnamese">Tiếng Việt</string>
<string name="txt_select_language">Select Language</string>
<string name="txt_language_changed">Language changed. Please restart the app.</string>
<string name="txt_restart_app">Restart App</string>
<string name="txt_restart_later">Later</string>
```

#### Tiếng Việt (`values-vi/strings.xml`)
```xml
<string name="txt_language">Ngôn Ngữ</string>
<string name="txt_app_settings">Cài Đặt Ứng Dụng</string>
<string name="txt_english">English</string>
<string name="txt_vietnamese">Tiếng Việt</string>
<string name="txt_select_language">Chọn Ngôn Ngữ</string>
<string name="txt_language_changed">Đã đổi ngôn ngữ. Vui lòng khởi động lại ứng dụng.</string>
<string name="txt_restart_app">Khởi Động Lại</string>
<string name="txt_restart_later">Để Sau</string>
```

## 🎯 Cách Sử Dụng

### Cho Người Dùng

1. **Mở Settings**
   - Tap vào tab "Profile" ở bottom navigation
   - Scroll xuống phần "App Settings"

2. **Chọn Language**
   - Tap vào mục "Language"
   - Dialog sẽ hiển thị với 2 lựa chọn

3. **Chọn Ngôn Ngữ**
   - Chọn "English" hoặc "Tiếng Việt"
   - Tap "Confirm"

4. **Khởi Động Lại**
   - Dialog xác nhận sẽ hiển thị
   - Tap "Restart App" để áp dụng ngay
   - Hoặc "Later" để áp dụng lần sau

### Cho Developer

#### 1. Thêm Ngôn Ngữ Mới

**Bước 1:** Tạo thư mục resources
```
app/src/main/res/values-ja/  # Tiếng Nhật
app/src/main/res/values-ko/  # Tiếng Hàn
app/src/main/res/values-zh/  # Tiếng Trung
```

**Bước 2:** Copy và dịch strings.xml

**Bước 3:** Cập nhật `dialog_language_selector.xml`
```xml
<RadioButton
    android:id="@+id/radioJapanese"
    android:text="日本語" />
```

**Bước 4:** Cập nhật `SettingsFragment.java`
```java
String selectedLanguage;
if (radioJapanese.isChecked()) {
    selectedLanguage = "ja";
} else if (radioVietnamese.isChecked()) {
    selectedLanguage = "vi";
} else {
    selectedLanguage = "en";
}
```

**Bước 5:** Cập nhật `LocaleHelper.java`
```java
public static String getLanguageName(Context context, String languageCode) {
    switch (languageCode) {
        case "vi": return "Tiếng Việt";
        case "ja": return "日本語";
        case "ko": return "한국어";
        default: return "English";
    }
}
```

#### 2. Lấy Ngôn Ngữ Hiện Tại

```java
// Trong Activity/Fragment
String currentLang = LocaleHelper.getCurrentLanguage(requireContext());

// Kiểm tra ngôn ngữ
if ("vi".equals(currentLang)) {
    // Tiếng Việt
} else {
    // Tiếng Anh
}
```

#### 3. Thay Đổi Ngôn Ngữ Programmatically

```java
// Đổi sang tiếng Việt
LocaleHelper.setLocale(context, "vi");

// Đổi sang tiếng Anh
LocaleHelper.setLocale(context, "en");

// Restart Activity để áp dụng
Intent intent = new Intent(context, MainActivity.class);
intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
startActivity(intent);
finish();
```

## 🔧 Cách Hoạt Động

### 1. Lưu Trữ Ngôn Ngữ

Ngôn ngữ được lưu trong SharedPreferences:
```
Key: "Locale.Helper.Selected.Language"
Value: "en" hoặc "vi"
File: "app_preferences"
```

### 2. Áp Dụng Ngôn Ngữ

Khi Activity được tạo:
```
onCreate() 
  ↓
attachBaseContext()
  ↓
LocaleHelper.setLocale()
  ↓
updateResources()
  ↓
Locale.setDefault()
  ↓
Configuration.setLocale()
  ↓
createConfigurationContext()
```

### 3. Flow Chuyển Đổi Ngôn Ngữ

```
User tap "Language"
  ↓
Show Language Dialog
  ↓
User select language
  ↓
User tap "Confirm"
  ↓
Save to SharedPreferences
  ↓
Show Restart Dialog
  ↓
User tap "Restart App"
  ↓
Restart MainActivity
  ↓
attachBaseContext() loads saved language
  ↓
App displays in new language
```

## 📱 Screenshots Flow

### 1. Settings Screen
```
┌─────────────────────┐
│  Profile            │
├─────────────────────┤
│  Account            │
│  • Personal Data    │
│  • Email & Payment  │
│                     │
│  App Settings       │
│  • Language    →    │  ← New Option
│                     │
│  Privacy & Policy   │
│  • Notification     │
│  • Your Ticket      │
│  • Logout           │
└─────────────────────┘
```

### 2. Language Dialog
```
┌─────────────────────┐
│  Select Language    │
├─────────────────────┤
│  ○ English          │
│  ● Tiếng Việt       │
├─────────────────────┤
│     Cancel  Confirm │
└─────────────────────┘
```

### 3. Restart Dialog
```
┌─────────────────────┐
│  Language           │
├─────────────────────┤
│  Language changed.  │
│  Please restart     │
│  the app.           │
├─────────────────────┤
│  Later  Restart App │
└─────────────────────┘
```

## 🐛 Troubleshooting

### Vấn đề 1: Ngôn ngữ không thay đổi
**Nguyên nhân:** App chưa được restart
**Giải pháp:** Đảm bảo restart app sau khi thay đổi ngôn ngữ

### Vấn đề 2: Một số text vẫn hiển thị sai ngôn ngữ
**Nguyên nhân:** Text bị hardcode trong code Java
**Giải pháp:** Sử dụng `getString(R.string.xxx)` thay vì hardcode

### Vấn đề 3: Ngôn ngữ reset về mặc định
**Nguyên nhân:** SharedPreferences bị xóa
**Giải pháp:** Kiểm tra không xóa SharedPreferences khi clear data

### Vấn đề 4: Crash khi chuyển ngôn ngữ
**Nguyên nhân:** Context null hoặc Activity đã destroyed
**Giải pháp:** Kiểm tra lifecycle và context trước khi thay đổi

## 📊 Thống Kê

### Files Đã Tạo/Cập Nhật

| File | Loại | Mô tả |
|------|------|-------|
| `LocaleHelper.java` | New | Utility quản lý ngôn ngữ |
| `dialog_language_selector.xml` | New | Dialog chọn ngôn ngữ |
| `fragment_settings.xml` | Updated | Thêm mục Language |
| `SettingsFragment.java` | Updated | Thêm logic chọn ngôn ngữ |
| `MainActivity.java` | Updated | Thêm attachBaseContext |
| `AuthenticationActivity.java` | Updated | Thêm attachBaseContext |
| `Splash.java` | Updated | Thêm attachBaseContext |
| `OnBoarding.java` | Updated | Thêm attachBaseContext |
| `values/strings.xml` | Updated | Thêm 8 strings mới |
| `values-vi/strings.xml` | Updated | Thêm 8 strings mới |

**Tổng cộng:** 10 files (2 new, 8 updated)

## ✅ Checklist Hoàn Thành

- ✅ Tạo LocaleHelper utility class
- ✅ Tạo dialog chọn ngôn ngữ
- ✅ Cập nhật SettingsFragment
- ✅ Cập nhật tất cả Activities
- ✅ Thêm strings cho 2 ngôn ngữ
- ✅ Test chuyển đổi ngôn ngữ
- ✅ Lưu lựa chọn vào SharedPreferences
- ✅ Hiển thị dialog restart
- ✅ Áp dụng ngôn ngữ khi khởi động

## 🚀 Tính Năng Tương Lai

### Có thể mở rộng:
1. ⏳ Thêm nhiều ngôn ngữ (Nhật, Hàn, Trung)
2. ⏳ Tự động phát hiện ngôn ngữ hệ thống lần đầu
3. ⏳ Thay đổi ngôn ngữ không cần restart
4. ⏳ Preview ngôn ngữ trước khi áp dụng
5. ⏳ Tải ngôn ngữ động từ server

## 📚 Tài Liệu Liên Quan

- `LOCALIZATION_GUIDE.md` - Hướng dẫn đa ngôn ngữ tổng quan
- `LOCALIZATION_SUMMARY.md` - Tóm tắt công việc localization
- `README.md` - Tài liệu dự án

---

**Hoàn thành bởi**: Kiro AI Assistant  
**Ngày hoàn thành**: 17/11/2025  
**Phiên bản**: 1.0
