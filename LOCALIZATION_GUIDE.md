# Hướng Dẫn Đa Ngôn Ngữ - TicketApp

## 📋 Tổng Quan

Dự án TicketApp đã được cấu hình để hỗ trợ 2 ngôn ngữ:
- **Tiếng Anh** (mặc định) - `values/strings.xml`
- **Tiếng Việt** - `values-vi/strings.xml`

## 🗂️ Cấu Trúc Thư Mục

```
app/src/main/res/
├── values/
│   └── strings.xml          # Tiếng Anh (mặc định)
└── values-vi/
    └── strings.xml          # Tiếng Việt
```

## 🔄 Cách Hoạt Động

Android tự động chọn file strings.xml phù hợp dựa trên ngôn ngữ hệ thống của thiết bị:

- Nếu thiết bị đặt ngôn ngữ là **Tiếng Việt** → sử dụng `values-vi/strings.xml`
- Nếu thiết bị đặt ngôn ngữ khác → sử dụng `values/strings.xml` (Tiếng Anh)

## 📝 Các Thay Đổi Đã Thực Hiện

### 1. Tạo File Strings.xml

#### File Tiếng Anh (`values/strings.xml`)
- Chứa tất cả string resources bằng tiếng Anh
- Là ngôn ngữ mặc định (fallback)
- Tổng cộng: 100+ string resources

#### File Tiếng Việt (`values-vi/strings.xml`)
- Bản dịch tiếng Việt của tất cả strings
- Tự động được sử dụng khi thiết bị đặt ngôn ngữ tiếng Việt

### 2. Cập Nhật Layout Files

Tất cả hardcoded text trong các file XML đã được thay thế bằng string resources:

**Trước:**
```xml
<TextView
    android:text="Login"
    ... />
```

**Sau:**
```xml
<TextView
    android:text="@string/txt_login"
    ... />
```

### 3. Danh Sách File Layout Đã Cập Nhật

#### Authentication
- `fragment_login.xml`
- `fragment_login_with_password.xml`
- `fragment_register.xml`

#### Settings
- `fragment_settings.xml`

#### Booking & Seats
- `fragment_select_seat.xml`
- `legend_item.xml`

#### Tickets
- `fragment_upcoming_tickets.xml`
- `fragment_watched_tickets.xml`
- `item_ticket.xml`
- `item_ticket_detail.xml`

#### Payment
- `fragment_payment_method_add.xml`
- `item_payment_card.xml`

#### Movies & Cinema
- `fragment_home.xml`
- `item_movie_horizontal.xml`
- `item_cinema.xml`

#### Common
- `item_setting.xml`

## 🎯 Các Nhóm String Resources

### 1. Navigation (Điều hướng)
```xml
<string name="txt_home">Home</string>
<string name="txt_cinema">Cinemas</string>
<string name="txt_ticket">Tickets</string>
<string name="txt_profile">Profile</string>
<string name="txt_explore">Explore</string>
```

### 2. Authentication (Xác thực)
```xml
<string name="hint_username">Username</string>
<string name="hint_email">Email</string>
<string name="hint_password">Password</string>
<string name="txt_login">Login</string>
<string name="txt_register">Register</string>
```

### 3. Settings (Cài đặt)
```xml
<string name="txt_account">Account</string>
<string name="txt_personal_data">Personal Data</string>
<string name="txt_notification">Notification</string>
<string name="txt_logout">Logout</string>
```

### 4. Booking (Đặt vé)
```xml
<string name="txt_date">Date</string>
<string name="txt_city">City</string>
<string name="txt_cinema_label">Cinema</string>
<string name="txt_showtime">Showtime</string>
<string name="txt_checkout">Checkout</string>
```

### 5. Seat Selection (Chọn ghế)
```xml
<string name="txt_available">Available</string>
<string name="txt_reserved">Reserved</string>
<string name="txt_selected">Selected</string>
<string name="txt_vip">VIP</string>
<string name="txt_regular">Regular</string>
```

### 6. Payment (Thanh toán)
```xml
<string name="txt_master_card">Master Card</string>
<string name="txt_card_holder">Card Holder</string>
<string name="hint_card_number">**** **** **** ****</string>
<string name="hint_cvv">123</string>
```

### 7. Common (Chung)
```xml
<string name="txt_cancel">Cancel</string>
<string name="txt_confirm">Confirm</string>
<string name="txt_save">Save</string>
<string name="txt_delete">Delete</string>
<string name="txt_loading">Loading...</string>
```

## 🔧 Cách Sử Dụng

### Trong XML Layout
```xml
<TextView
    android:text="@string/txt_login"
    android:hint="@string/hint_email"
    android:contentDescription="@string/content_desc_facebook" />
```

### Trong Java Code
```java
// Lấy string từ resources
String loginText = getString(R.string.txt_login);

// Với tham số
String welcomeText = getString(R.string.txt_welcome, userName);

// Set text cho TextView
textView.setText(R.string.txt_login);
```

### Trong Fragment/Activity
```java
// Trong Fragment
String text = requireContext().getString(R.string.txt_login);

// Trong Activity
String text = this.getString(R.string.txt_login);
```

## ➕ Thêm String Mới

### Bước 1: Thêm vào `values/strings.xml` (Tiếng Anh)
```xml
<string name="txt_new_feature">New Feature</string>
```

### Bước 2: Thêm vào `values-vi/strings.xml` (Tiếng Việt)
```xml
<string name="txt_new_feature">Tính Năng Mới</string>
```

### Bước 3: Sử dụng trong code
```xml
<TextView android:text="@string/txt_new_feature" />
```

## 🌍 Thêm Ngôn Ngữ Mới

Để thêm ngôn ngữ mới (ví dụ: Tiếng Nhật):

### 1. Tạo thư mục mới
```
app/src/main/res/values-ja/
```

### 2. Copy file strings.xml
Copy từ `values/strings.xml` sang `values-ja/strings.xml`

### 3. Dịch nội dung
Dịch tất cả các string sang tiếng Nhật

### 4. Test
Đổi ngôn ngữ thiết bị sang tiếng Nhật để kiểm tra

## 🧪 Kiểm Tra Đa Ngôn Ngữ

### Trên Emulator/Device
1. Mở **Settings** → **System** → **Languages & input**
2. Chọn **Languages**
3. Thêm/Chọn ngôn ngữ muốn test
4. Mở lại app để xem thay đổi

### Trong Android Studio
1. Mở Layout Editor
2. Chọn locale từ dropdown (en, vi, etc.)
3. Preview sẽ hiển thị theo ngôn ngữ đã chọn

## 📌 Best Practices

### 1. Không Hardcode Text
❌ **Sai:**
```xml
<TextView android:text="Login" />
```

✅ **Đúng:**
```xml
<TextView android:text="@string/txt_login" />
```

### 2. Đặt Tên String Có Ý Nghĩa
```xml
<!-- Tốt -->
<string name="txt_login">Login</string>
<string name="hint_email">Email</string>
<string name="error_invalid_email">Invalid email</string>

<!-- Không tốt -->
<string name="text1">Login</string>
<string name="str2">Email</string>
```

### 3. Nhóm String Theo Chức Năng
```xml
<!-- Authentication -->
<string name="txt_login">Login</string>
<string name="txt_register">Register</string>

<!-- Settings -->
<string name="txt_account">Account</string>
<string name="txt_logout">Logout</string>
```

### 4. Sử Dụng Plurals Cho Số Lượng
```xml
<plurals name="number_of_tickets">
    <item quantity="one">%d ticket</item>
    <item quantity="other">%d tickets</item>
</plurals>
```

### 5. Sử Dụng String Format
```xml
<string name="welcome_message">Welcome, %s!</string>
<string name="price_format">Price: %,.0f đ</string>
```

## 🐛 Troubleshooting

### Vấn đề: String không hiển thị đúng ngôn ngữ
**Giải pháp:**
1. Kiểm tra tên file và thư mục đúng format: `values-vi`, `values-ja`
2. Đảm bảo string name giống nhau trong tất cả file
3. Clean & Rebuild project
4. Restart app

### Vấn đề: Missing string resource
**Giải pháp:**
1. Đảm bảo string tồn tại trong `values/strings.xml` (fallback)
2. Kiểm tra typo trong tên string
3. Sync Gradle files

### Vấn đề: Ký tự đặc biệt không hiển thị
**Giải pháp:**
```xml
<!-- Sử dụng CDATA cho ký tự đặc biệt -->
<string name="text_with_special"><![CDATA[Text & Special]]></string>

<!-- Hoặc escape -->
<string name="text_escaped">Text &amp; Special</string>
```

## 📊 Thống Kê

- **Tổng số strings**: 100+
- **Ngôn ngữ hỗ trợ**: 2 (English, Vietnamese)
- **File layout đã cập nhật**: 15+
- **Tỷ lệ hoàn thành**: 100%

## 🔄 Cập Nhật Trong Tương Lai

### Kế hoạch mở rộng:
1. ✅ Tiếng Anh (Hoàn thành)
2. ✅ Tiếng Việt (Hoàn thành)
3. ⏳ Tiếng Nhật (Dự kiến)
4. ⏳ Tiếng Hàn (Dự kiến)
5. ⏳ Tiếng Trung (Dự kiến)

## 📚 Tài Liệu Tham Khảo

- [Android Localization Guide](https://developer.android.com/guide/topics/resources/localization)
- [String Resources](https://developer.android.com/guide/topics/resources/string-resource)
- [Supporting Different Languages](https://developer.android.com/training/basics/supporting-devices/languages)

---

**Lưu ý**: Khi thêm string mới, luôn thêm vào CẢ HAI file (English và Vietnamese) để đảm bảo tính nhất quán.
