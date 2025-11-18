# Template Áp Dụng Dialog System

## 📋 Template Chuẩn

### 1. Import DialogHelper

```java
import com.example.ticketapp.utils.DialogHelper;
```

### 2. Template Cho ViewModel Observer

```java
viewModel.getData().observe(getViewLifecycleOwner(), resource -> {
    if (resource == null) return;
    
    switch (resource.getStatus()) {
        case LOADING:
            // Option 1: Hiển thị loading dialog
            DialogHelper.showLoadingDialog(requireContext());
            
            // Option 2: Hiển thị loading với custom message
            DialogHelper.showLoadingDialog(requireContext(), 
                getString(R.string.msg_loading_data));
            
            // Option 3: Sử dụng ProgressBar/SwipeRefresh thay vì dialog
            binding.progressBar.setVisibility(View.VISIBLE);
            // hoặc
            binding.swipeRefreshLayout.setRefreshing(true);
            break;
            
        case SUCCESS:
            // Luôn ẩn loading trước
            DialogHelper.hideLoadingDialog();
            // hoặc
            binding.progressBar.setVisibility(View.GONE);
            binding.swipeRefreshLayout.setRefreshing(false);
            
            // Xử lý data
            if (resource.getData() != null) {
                // Cập nhật UI với data
                updateUI(resource.getData());
            }
            break;
            
        case ERROR:
            // Luôn ẩn loading trước
            DialogHelper.hideLoadingDialog();
            // hoặc
            binding.progressBar.setVisibility(View.GONE);
            binding.swipeRefreshLayout.setRefreshing(false);
            
            // Hiển thị error
            String errorMessage = resource.getMessage() != null 
                ? resource.getMessage() 
                : getString(R.string.msg_network_error);
            DialogHelper.showErrorDialog(requireContext(), errorMessage);
            break;
    }
});
```

## 🎯 Các Trường Hợp Sử Dụng

### Case 1: Loading Data (GET)
**Khi:** Load danh sách, chi tiết  
**Loading:** SwipeRefresh hoặc ProgressBar  
**Error:** Dialog

```java
movieViewModel.getMovies().observe(getViewLifecycleOwner(), resource -> {
    switch (resource.getStatus()) {
        case LOADING:
            binding.swipeRefreshLayout.setRefreshing(true);
            break;
        case SUCCESS:
            binding.swipeRefreshLayout.setRefreshing(false);
            adapter.updateData(resource.getData());
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

### Case 2: Submit Data (POST/PUT)
**Khi:** Đặt vé, thanh toán, đăng ký  
**Loading:** Dialog (blocking)  
**Success:** Navigate hoặc Success Dialog  
**Error:** Error Dialog

```java
bookingViewModel.bookTicket(data).observe(getViewLifecycleOwner(), resource -> {
    switch (resource.getStatus()) {
        case LOADING:
            DialogHelper.showLoadingDialog(requireContext(), 
                getString(R.string.msg_processing));
            break;
            
        case SUCCESS:
            DialogHelper.hideLoadingDialog();
            // Option 1: Navigate trực tiếp
            navController.navigate(R.id.action_to_success);
            
            // Option 2: Hiển thị success dialog rồi navigate
            DialogHelper.showSuccessDialog(requireContext(), 
                getString(R.string.msg_operation_success), () -> {
                    navController.navigate(R.id.action_to_success);
                });
            break;
            
        case ERROR:
            DialogHelper.hideLoadingDialog();
            DialogHelper.showErrorDialog(requireContext(), 
                resource.getMessage() != null ? resource.getMessage() 
                    : getString(R.string.msg_operation_failed));
            break;
    }
});
```

### Case 3: Delete Data (DELETE)
**Khi:** Xóa item  
**Loading:** Dialog  
**Success:** Success message + refresh  
**Error:** Error Dialog

```java
// Bước 1: Confirm trước khi delete
DialogHelper.showConfirmDialog(
    requireContext(),
    getString(R.string.txt_delete),
    getString(R.string.msg_confirm_delete),
    () -> {
        // User confirmed - thực hiện delete
        viewModel.deleteItem(itemId).observe(getViewLifecycleOwner(), resource -> {
            switch (resource.getStatus()) {
                case LOADING:
                    DialogHelper.showLoadingDialog(requireContext());
                    break;
                    
                case SUCCESS:
                    DialogHelper.hideLoadingDialog();
                    Toast.makeText(requireContext(), 
                        getString(R.string.msg_delete_success), 
                        Toast.LENGTH_SHORT).show();
                    // Refresh list
                    viewModel.refreshData();
                    break;
                    
                case ERROR:
                    DialogHelper.hideLoadingDialog();
                    DialogHelper.showErrorDialog(requireContext(), 
                        resource.getMessage());
                    break;
            }
        });
    },
    null // User cancelled - không làm gì
);
```

### Case 4: Authentication (Login/Register)
**Khi:** Đăng nhập, đăng ký  
**Loading:** Dialog  
**Success:** Navigate to main  
**Error:** Error Dialog

```java
authViewModel.login(email, password).observe(getViewLifecycleOwner(), resource -> {
    switch (resource.getStatus()) {
        case LOADING:
            DialogHelper.showLoadingDialog(requireContext(), 
                getString(R.string.msg_please_wait));
            break;
            
        case SUCCESS:
            DialogHelper.hideLoadingDialog();
            // Navigate to main screen
            startActivity(new Intent(requireActivity(), MainActivity.class));
            requireActivity().finish();
            break;
            
        case ERROR:
            DialogHelper.hideLoadingDialog();
            DialogHelper.showErrorDialog(requireContext(), 
                resource.getMessage() != null ? resource.getMessage() 
                    : getString(R.string.msg_login_failed));
            break;
    }
});
```

## 📝 Checklist Áp Dụng

Khi cập nhật một Fragment:

- [ ] Import DialogHelper
- [ ] Xác định loại operation (GET/POST/DELETE)
- [ ] Chọn loading indicator phù hợp (Dialog/ProgressBar/SwipeRefresh)
- [ ] Thêm DialogHelper.showLoadingDialog() trong LOADING case
- [ ] Thêm DialogHelper.hideLoadingDialog() trong SUCCESS và ERROR case
- [ ] Thêm DialogHelper.showErrorDialog() trong ERROR case
- [ ] Sử dụng string resources cho messages
- [ ] Xử lý null message với fallback
- [ ] Test với cả 2 ngôn ngữ

## 🚫 Những Gì KHÔNG Nên Làm

### ❌ Không ẩn loading dialog
```java
case ERROR:
    // ❌ Quên ẩn loading
    DialogHelper.showErrorDialog(context, message);
```

### ❌ Hardcode message
```java
// ❌ Sai
DialogHelper.showLoadingDialog(context, "Loading...");

// ✅ Đúng
DialogHelper.showLoadingDialog(context, getString(R.string.msg_loading));
```

### ❌ Không xử lý null message
```java
// ❌ Sai - có thể null
DialogHelper.showErrorDialog(context, resource.getMessage());

// ✅ Đúng
String message = resource.getMessage() != null 
    ? resource.getMessage() 
    : getString(R.string.msg_unknown_error);
DialogHelper.showErrorDialog(context, message);
```

### ❌ Hiển thị nhiều dialog cùng lúc
```java
case ERROR:
    DialogHelper.hideLoadingDialog();
    DialogHelper.showErrorDialog(context, message);
    // ❌ Không show thêm dialog khác ngay sau
    DialogHelper.showInfoDialog(context, "Info");
```

## 💡 Tips

### 1. Sử dụng SwipeRefresh cho list
```java
// Tốt hơn dialog cho việc refresh list
binding.swipeRefreshLayout.setRefreshing(true/false);
```

### 2. Sử dụng Dialog cho submit actions
```java
// Blocking user khi đang submit
DialogHelper.showLoadingDialog(context, getString(R.string.msg_processing));
```

### 3. Combine với Navigation
```java
DialogHelper.showSuccessDialog(context, message, () -> {
    navController.navigateUp();
});
```

### 4. Reusable Error Handler
```java
private void handleError(Resource<?> resource) {
    DialogHelper.hideLoadingDialog();
    String message = resource.getMessage() != null 
        ? resource.getMessage() 
        : getString(R.string.msg_network_error);
    DialogHelper.showErrorDialog(requireContext(), message);
}

// Sử dụng
case ERROR:
    handleError(resource);
    break;
```

---

**Tạo bởi**: Kiro AI Assistant  
**Ngày tạo**: 17/11/2025
