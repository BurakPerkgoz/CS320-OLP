window.addEventListener('DOMContentLoaded', function () {
    document.body.classList.remove('wdt-fade');
});

window.addEventListener('beforeunload', () => {
    document.body.classList.add('wdt-fade');
});