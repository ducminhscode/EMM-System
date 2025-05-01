function deleteApi(endpoint, id) {
    if (confirm("Bạn chắc chắn xóa không?") === true) {
        fetch(endpoint + id, {
            method: "delete"
        }).then(res => {
            if (res.status === 204) {
                alert("Xóa thành công!");
                location.reload();
            } else
                alert("Có lỗi xảy ra!");
        });
    }
}

document.addEventListener('DOMContentLoaded', () => {
    // Cache các phần tử
    const form = document.getElementById('userForm');
    const passwordInput = document.getElementById('password');
    const confirmInput = document.getElementById('confirmPassword');
    const confirmError = document.getElementById('confirmPasswordError');

    if (!form || !passwordInput || !confirmInput || !confirmError) {
        console.error('Missing required elements');
        return;
    }

    // Hàm kiểm tra mật khẩu
    const checkPasswordMatch = () => {
        const password = passwordInput.value;
        const confirmPassword = confirmInput.value;
        
        const isMatch = password === confirmPassword;
        confirmError.textContent = isMatch ? '' : 'Mật khẩu không khớp';
        return isMatch;
    };

    // Xử lý sự kiện real-time
    passwordInput.addEventListener('input', checkPasswordMatch);
    confirmInput.addEventListener('input', checkPasswordMatch);

    // Xử lý submit form
    form.addEventListener('submit', (e) => {
        if (!checkPasswordMatch()) {
            e.preventDefault();
            passwordInput.focus();
        }
    });
});