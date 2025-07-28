
        // Theme Toggle
        const themeSwitch = document.getElementById('theme-switch');
        const body = document.body;

        themeSwitch.addEventListener('change', function() {
            if(this.checked) {
                body.classList.add('dark-theme');
                body.classList.remove('light-theme');
                localStorage.setItem('theme', 'dark');
            } else {
                body.classList.add('light-theme');
                body.classList.remove('dark-theme');
                localStorage.setItem('theme', 'light');
            }
        });

        // Set initial theme from localStorage or default to light
        const savedTheme = localStorage.getItem('theme') || 'light';
        if (savedTheme === 'dark') {
            body.classList.add('dark-theme');
            themeSwitch.checked = true;
        } else {
            body.classList.add('light-theme');
        }

        // Form Validation
        const loginForm = document.querySelector('.login-form');
        if (loginForm) {
            loginForm.addEventListener('submit', function(e) {
                const email = document.getElementById('email').value;
                const password = document.getElementById('password').value;

                if (!email || !password) {
                    e.preventDefault();
                    alert('Please fill in both email and password fields.');
                }
            });
        }