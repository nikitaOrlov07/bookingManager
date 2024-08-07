function hideAlert(alertId) {
    setTimeout(function() {
        let alertElement = document.getElementById(alertId);
        if (alertElement) {
            alertElement.style.display = 'none'; <!--display property is set to none -> the element will be hidden from the page -->
        }
    }, 5000); // after 5 seconds -> hide alert
}
function updateCharCount() {
    const textarea = document.getElementById('description');
    const charCount = textarea.value.length;
    document.getElementById('charCount').textContent = charCount;
}