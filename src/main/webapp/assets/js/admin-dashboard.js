
        // Theme Toggle (same as homepage)
        const themeSwitch = document.getElementById('theme-switch');
        const body = document.body;

        themeSwitch.addEventListener('change', function() {
            if(this.checked) {
                body.classList.add('dark-theme');
                body.classList.remove('light-theme');
            } else {
                body.classList.add('light-theme');
                body.classList.remove('dark-theme');
            }
        });

        // Set initial theme
        body.classList.add('light-theme');

        // Menu toggle for mobile
        const menuToggle = document.querySelector('.menu-toggle');
        const sidebar = document.querySelector('.sidebar');

        if (menuToggle) {
            menuToggle.addEventListener('click', () => {
                sidebar.classList.toggle('active');
            });
        }
