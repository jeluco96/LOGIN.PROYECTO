// Implementación de tooltips personalizados
document.addEventListener('DOMContentLoaded', function() {
    // Esta función garantiza que los tooltips se muestren correctamente 
    // sin salirse de la pantalla
    const elements = document.querySelectorAll('[title]');

    elements.forEach(element => {
        element.addEventListener('mouseenter', function(e) {
            const title = this.getAttribute('title');
            this.setAttribute('data-tooltip', title);
            this.removeAttribute('title');

            const tooltip = document.createElement('div');
            tooltip.className = 'custom-tooltip';
            tooltip.innerText = title;
            document.body.appendChild(tooltip);

            const rect = this.getBoundingClientRect();
            const tooltipRect = tooltip.getBoundingClientRect();

            // Posiciona el tooltip encima del elemento
            let top = rect.top - tooltipRect.height - 10;
            let left = rect.left + (rect.width / 2) - (tooltipRect.width / 2);

            // Ajusta si está fuera de la pantalla
            if (top < 0) top = rect.bottom + 10;
            if (left < 0) left = 0;
            if (left + tooltipRect.width > window.innerWidth) {
                left = window.innerWidth - tooltipRect.width;
            }

            tooltip.style.top = `${top}px`;
            tooltip.style.left = `${left}px`;

            this.tooltip = tooltip;
        });

        element.addEventListener('mouseleave', function() {
            if (this.tooltip) {
                document.body.removeChild(this.tooltip);
                this.setAttribute('title', this.getAttribute('data-tooltip'));
                this.removeAttribute('data-tooltip');
                this.tooltip = null;
            }
        });
    });
});
