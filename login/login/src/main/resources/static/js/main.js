// Script principal
document.addEventListener('DOMContentLoaded', function() {
    // Añadir botón de toggle para menú en dispositivos móviles
    const body = document.body;
    const sideMenu = document.querySelector('.side-menu');

    if (window.innerWidth <= 768) {
        // Crear botón de toggle
        const toggleButton = document.createElement('button');
        toggleButton.className = 'menu-toggle';
        toggleButton.innerHTML = '<i class="fas fa-bars"></i>';
        toggleButton.setAttribute('title', 'Abrir menú');

        // Añadir funcionalidad
        toggleButton.addEventListener('click', function() {
            sideMenu.classList.toggle('open');
            if (sideMenu.classList.contains('open')) {
                this.innerHTML = '<i class="fas fa-times"></i>';
                this.setAttribute('title', 'Cerrar menú');
            } else {
                this.innerHTML = '<i class="fas fa-bars"></i>';
                this.setAttribute('title', 'Abrir menú');
            }
        });

        // Añadir al DOM
        body.appendChild(toggleButton);
    }
});

// Función para volver atrás en el historial
function goBack() {
    window.history.back();
}
