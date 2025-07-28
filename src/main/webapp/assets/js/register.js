// Theme toggle (already working)
const themeSwitch = document.getElementById('theme-switch');
const body = document.body;

themeSwitch.addEventListener('change', function () {
    if (this.checked) {
        body.classList.add('dark-theme');
        body.classList.remove('light-theme');
        localStorage.setItem('theme', 'dark');
    } else {
        body.classList.add('light-theme');
        body.classList.remove('dark-theme');
        localStorage.setItem('theme', 'light');
    }
});

const savedTheme = localStorage.getItem('theme') || 'light';
if (savedTheme === 'dark') {
    body.classList.add('dark-theme');
    themeSwitch.checked = true;
} else {
    body.classList.add('light-theme');
}

// Password validation
const passwordInput = document.getElementById('password');
const confirmPasswordInput = document.getElementById('confirm-password');
const passwordError = document.getElementById('passwordError');
const strengthFill = document.getElementById('strengthFill');
const strengthLabel = document.getElementById('strengthLabel');

function checkStrength(password) {
    let score = 0;
    const length = password.length >= 8;
    const upper = /[A-Z]/.test(password);
    const number = /\d/.test(password);
    const special = /[!@#$%^&*(),.?":{}|<>]/.test(password);

    if (length) score++;
    if (upper) score++;
    if (number) score++;
    if (special) score++;

    // Strength visuals
    const colors = ['#ff4d4d', '#ff944d', '#ffd633', '#4cd137'];
    const faLabels = [
        '<i class="fas fa-times-circle" style="color:red;"></i> Very Weak',
        '<i class="fas fa-exclamation-circle" style="color:orange;"></i> Weak',
        '<i class="fas fa-exclamation-triangle" style="color:goldenrod;"></i> Moderate',
        '<i class="fas fa-check-circle" style="color:green;"></i> Strong'
    ];

    strengthFill.style.width = `${(score / 4) * 100}%`;
    strengthFill.style.backgroundColor = colors[score - 1] || colors[0];
    strengthLabel.innerHTML = faLabels[score - 1] || faLabels[0];

    return score === 4;
}

passwordInput.addEventListener('input', () => {
    const valid = checkStrength(passwordInput.value);
    passwordError.style.display = valid ? "none" : "block";
    if (!valid) {
        passwordError.innerHTML = '<i class="fas fa-times-circle" style="color:red;"></i> Password not strong enough.';
    }
});


passwordInput.addEventListener('input', () => {
    const valid = checkStrength(passwordInput.value);
    passwordError.style.display = valid ? "none" : "block";
    if (!valid) {
        passwordError.innerHTML = '<i class="fas fa-times-circle" style="color:red;"></i> Password not strong enough.';

    }
});

document.querySelector('.register-form').addEventListener('submit', function (e) {
    const pass = passwordInput.value;
    const confirm = confirmPasswordInput.value;
    const valid = checkStrength(pass);

    if (!valid) {
        e.preventDefault();
        alert("Password does not meet strength requirements.");
    } else if (pass !== confirm) {
        e.preventDefault();
        alert("Passwords do not match.");
    }
});
