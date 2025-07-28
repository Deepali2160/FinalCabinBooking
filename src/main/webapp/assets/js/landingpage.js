
        // Theme Toggle
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

        // Testimonial Carousel
        const testimonials = document.querySelectorAll('.testimonial');
        const dots = document.querySelectorAll('.dot');
        const prevBtn = document.querySelector('.prev');
        const nextBtn = document.querySelector('.next');
        let currentIndex = 0;

        function showTestimonial(index) {
            testimonials.forEach(testimonial => testimonial.classList.remove('active'));
            dots.forEach(dot => dot.classList.remove('active'));

            testimonials[index].classList.add('active');
            dots[index].classList.add('active');
            currentIndex = index;
        }

        dots.forEach((dot, index) => {
            dot.addEventListener('click', () => {
                showTestimonial(index);
            });
        });

        prevBtn.addEventListener('click', () => {
            currentIndex = (currentIndex > 0) ? currentIndex - 1 : testimonials.length - 1;
            showTestimonial(currentIndex);
        });

        nextBtn.addEventListener('click', () => {
            currentIndex = (currentIndex < testimonials.length - 1) ? currentIndex + 1 : 0;
            showTestimonial(currentIndex);
        });

        // Auto rotate testimonials
        setInterval(() => {
            currentIndex = (currentIndex < testimonials.length - 1) ? currentIndex + 1 : 0;
            showTestimonial(currentIndex);
        }, 5000);


        // Simple Three.js scene for cabin model (placeholder)
        function initCabinScene() {
            const container = document.getElementById('cabin-container');
            if (!container) return;

            const scene = new THREE.Scene();
            const camera = new THREE.PerspectiveCamera(75, container.clientWidth / container.clientHeight, 0.1, 1000);
            const renderer = new THREE.WebGLRenderer({ alpha: true, antialias: true });

            renderer.setSize(container.clientWidth, container.clientHeight);
            container.appendChild(renderer.domElement);

            // Create a simple cabin geometry
            const geometry = new THREE.BoxGeometry(2, 1.5, 2);
            const material = new THREE.MeshBasicMaterial({
                color: 0x8B4513,
                wireframe: true
            });
            const cabin = new THREE.Mesh(geometry, material);
            scene.add(cabin);

            // Add a roof
            const roofGeometry = new THREE.ConeGeometry(1.5, 1, 4);
            const roofMaterial = new THREE.MeshBasicMaterial({ color: 0xA52A2A, wireframe: true });
            const roof = new THREE.Mesh(roofGeometry, roofMaterial);
            roof.position.y = 1.5;
            roof.rotation.y = Math.PI / 4;
            scene.add(roof);

            camera.position.z = 5;

            // Add floating animation
            function animate() {
                requestAnimationFrame(animate);

                cabin.rotation.y += 0.005;
                roof.rotation.y += 0.005;

                // Floating effect
                const time = Date.now() * 0.001;
                cabin.position.y = Math.sin(time) * 0.1;
                roof.position.y = 1.5 + Math.sin(time) * 0.1;

                renderer.render(scene, camera);
            }

            animate();

            // Handle window resize
            window.addEventListener('resize', () => {
                camera.aspect = container.clientWidth / container.clientHeight;
                camera.updateProjectionMatrix();
                renderer.setSize(container.clientWidth, container.clientHeight);
            });
        }

        // Initialize the cabin scene when the page loads
        window.addEventListener('load', initCabinScene);
