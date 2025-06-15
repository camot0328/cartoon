function confirmPassword() {
    const input = prompt("비밀번호를 입력하세요:");
    if (!input) return false;
    document.getElementById("passwordInput").value = input;
    return true;
}