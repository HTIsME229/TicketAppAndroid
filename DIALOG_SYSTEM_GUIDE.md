# Hướng Dẫn Hệ Thống Dialog Thông Báo

## 📋 Tổng Quan

Dự án TicketApp đã được tích hợp hệ thống dialog thông báo tự động cho các trạng thái Loading, Success, và Error khi gọi ViewModel.

## 🛠️ Components

### DialogHelper.java
Utility class cung cấp các phương thức hiển thị dialog:

```java
// Loading Dialog
DialogHelper.showLoadingDialog(context);
DialogHelper.showLoadingDialog(context, "Custom message");
DialogHelper.hideLoadingDialog();

// Success Dialog
DialogHelper.showSuccessDialog(context, "Success message");
DialogHelper.showSuccessDialog(context, "Title", "Message", onDismissCallback);

// Error Dialog
DialogHelper.showErrorDialog(context, "Error message");
DialogHelper.showErrorDialog(context, "Title", "Message", onDismissCallback);

// Confirmation Dialog
DialogHelper.showConfirmDialog(context, "Title", "Message", onConfirm, onCancel);

// Info Dialog
DialogHelper.showInfoDialog(context, "Title", "Message");
```

## 📝 Cách Sử Dụng

### Sử Dụng DialogHelper Trong Fragment

**Ví dụ trong PaymentMethod.java:**

```java
bookingViewModel.bookingTicket(bookingData)
    .observe(getViewLifecycleOwner(), resource -> {
        if (resource != null) {
            switch (resource.getStatus()) {
                case LOADING:
                    DialogHelper.showLoadingDialog(requireContext(), 
                        getString(R.string.msg_processing));
                    break;
                    
                case SUCCESS:
                    DialogHelper.hideLoadingDialog();
                    // Xử lý success
                    break;
                    
                case ERROR:
                    DialogHelper.hideLoadingDialog();
                    String errorMessage = resource.getMessage() != null 
                        ? resource.getMessage() 
                        : getString(R.string.msg_operation_failed);
                    DialogHelper.showErrorDialog(requireContext(), errorMessage);
                    break;
            }
        }
    });
```

### Ví Dụ Trong HomeFragment.java

**Xử lý loading movies:**

```java
movieViewModel.movies.observe(getViewLifecycleOwner(), resource -> {
    switch (resource.getStatus()) {
        case LOADING:
            binding.swipeRefreshLayout.setRefreshing(true);
            break;
            
        case SUCCESS:
            binding.swipeRefreshLayout.setRefreshing(false);
            // Cập nhật UI với data
            break;
            
        case ERROR:
            binding.swipeRefreshLayout.setRefreshing(false);
            DialogHelper.showErrorDialog(requireContext(), 
                resource.getMessage() != null ? resource.getMessage() 
                    : getString(R.string.msg_network_error));
            break;
    }
});
```

## 🎨 Các Loại Dialog

### 1. Loading Dialog
- Hiển thị khi đang xử lý request
- Không thể cancel bằng cách tap outside
- Tự động ẩn khi có response

### 2. Success Dialog
- Hiển thị khi operation thành công
- Có nút OK để dismiss
- Có thể thêm callback khi dismiss

### 3. Error Dialog
- Hiển thị khi có lỗi
- Hiển thị error message
- Có nút OK để dismiss

### 4. Confirmation Dialog
- Yêu cầu xác nhận từ user
- Có nút Confirm và Cancel
- Có callback cho cả 2 actions

### 5. Info Dialog
- Hiển thị thông tin
- Chỉ có nút OK

## 📊 Files Đã Cập Nhật

### New Files
1. `DialogHelper.java` - Utility class cho dialogs

### Updated Files
1. `HomeFragment.java` - Thêm error dialog
2. `PaymentMethod.java` - Thêm loading và error dialog
3. `values/strings.xml` - Thêm dialog strings
4. `values-vi/strings.xml` - Thêm dialog strings (Vietnamese)

## 🌐 Strings Resources

### English (`values/strings.xml`)
```xml
<string name="txt_ok">OK</string>
<string name="txt_yes">Yes</string>
<string name="txt_no">No</string>

<string name="msg_loading">Loading, please wait...</string>
<string name="msg_loading_data">Loading data...</string>
<string name="msg_processing">Processing...</string>
<string name="msg_please_wait">Please wait...</string>
<string name="msg_operation_success">Operation completed successfully</string>
<string name="msg_operation_failed">Operation failed</string>
<string name="msg_network_error">Network error. Please check your connection.</string>
<string name="msg_unknown_error">An unknown error occurred</string>
<string name="msg_confirm_action">Are you sure you want to proceed?</string>
```

### Vietnamese (`values-vi/strings.xml`)
```xml
<string name="txt_ok">OK</string>
<string name="txt_yes">Có</string>
<string name="txt_no">Không</string>

<string name="msg_loading">Đang tải, vui lòng đợi...</string>
<string name="msg_loading_data">Đang tải dữ liệu...</string>
<string name="msg_processing">Đang xử lý...</string>
<string name="msg_please_wait">Vui lòng đợi...</string>
<string name="msg_operation_success">Thao tác hoàn tất thành công</string>
<string name="msg_operation_failed">Thao tác thất bại</string>
<string name="msg_network_error">Lỗi mạng. Vui lòng kiểm tra kết nối.</string>
<string name="msg_unknown_error">Đã xảy ra lỗi không xác định</string>
<string name="msg_confirm_action">Bạn có chắc chắn muốn tiếp tục?</string>
```

## 💡 Best Practices

### 1. Luôn Ẩn Loading Dialog
```java
case SUCCESS:
case ERROR:
    DialogHelper.hideLoadingDialog(); // Luôn gọi trước
    // Xử lý tiếp
    break;
```

### 2. Sử Dụng String Resources
```java
// ✅ Đúng
DialogHelper.showLoadingDialog(context, getString(R.string.msg_processing));

// ❌ Sai
DialogHelper.showLoadingDialog(context, "Processing...");
```

### 3. Xử Lý Null Message
```java
String errorMessage = resource.getMessage() != null 
    ? resource.getMessage() 
    : getString(R.string.msg_unknown_error);
DialogHelper.showErrorDialog(context, errorMessage);
```

### 4. Sử Dụng Callback Khi Cần
```java
DialogHelper.showSuccessDialog(context, "Success", "Data saved", () -> {
    // Navigate back hoặc refresh data
    navController.navigateUp();
});
```

### 5. Confirmation Trước Action Quan Trọng
```java
DialogHelper.showConfirmDialog(
    context,
    getString(R.string.txt_delete),
    getString(R.string.msg_confirm_delete),
    () -> {
        // User confirmed - thực hiện delete
        viewModel.deleteItem(itemId);
    },
    () -> {
        // User cancelled - không làm gì
    }
);
```

## 🔄 Migration Guide

### Trước (Old Code)
```java
movieViewModel.movies.observe(getViewLifecycleOwner(), resource -> {
    switch (resource.getStatus()) {
        case LOADING:
            progressBar.setVisibility(View.VISIBLE);
            break;
        case SUCCESS:
            progressBar.setVisibility(View.GONE);
            adapter.updateData(resource.getData());
            break;
        case ERROR:
            progressBar.setVisibility(View.GONE);
            Toast.makeText(context, "Error: " + resource.getMessage(), 
                Toast.LENGTH_SHORT).show();
            break;
    }
});
```

### Sau (New Code)
```java
movieViewModel.movies.observe(getViewLifecycleOwner(), resource -> {
    switch (resource.getStatus()) {
        case LOADING:
            DialogHelper.showLoadingDialog(requireContext());
            break;
        case SUCCESS:
            DialogHelper.hideLoadingDialog();
            adapter.updateData(resource.getData());
            break;
        case ERROR:
            DialogHelper.hideLoadingDialog();
            DialogHelper.showErrorDialog(requireContext(), 
                resource.getMessage() != null ? resource.getMessage() 
                    : getString(R.string.msg_network_error));
            break;
    }
});
```



## 📋 Checklist Áp Dụng

Để áp dụng dialog system cho một Fragment/Activity:

- [ ] Import DialogHelper
- [ ] Thay thế ProgressBar/Toast bằng DialogHelper
- [ ] Xử lý LOADING state với showLoadingDialog()
- [ ] Xử lý SUCCESS state với hideLoadingDialog()
- [ ] Xử lý ERROR state với hideLoadingDialog() + showErrorDialog()
- [ ] Sử dụng string resources cho messages
- [ ] Test với cả 2 ngôn ngữ (EN/VI)

## 🎯 Các Fragment Cần Cập Nhật

### Đã Cập Nhật
- ✅ HomeFragment.java
- ✅ PaymentMethod.java

### Cần Cập Nhật
- ⏳ SelectSeatFragment.java
- ⏳ UpcomingTicketsFragment.java
- ⏳ WatchedTicketsFragment.java
- ⏳ MovieListFragment.java
- ⏳ LoginWithPasswordFragment.java
- ⏳ RegisterFragment.java
- ⏳ Splash.java

## 🚀 Tính Năng Tương Lai

1. ⏳ Custom dialog layouts với Material Design 3
2. ⏳ Animation cho dialog transitions
3. ⏳ Snackbar option thay vì dialog
4. ⏳ Retry button trong error dialog
5. ⏳ Progress percentage trong loading dialog

---

**Tạo bởi**: Kiro AI Assistant  
**Ngày tạo**: 17/11/2025  
**Phiên bản**: 1.0
