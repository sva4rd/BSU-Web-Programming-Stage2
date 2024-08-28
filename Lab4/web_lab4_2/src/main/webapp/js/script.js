document.addEventListener('DOMContentLoaded', function() {
    // Проверка первого списка
    const carListEmpty = document.querySelectorAll('.list-container:first-child .list-item').length === 0;
    const inputs = document.querySelectorAll('.request-container:first-child input');
    if (carListEmpty) {
        inputs.forEach(input => input.disabled = true);
    } else {
        for (let i = 0; i < inputs.length; i++) {
            if(inputs[i].id === 'payBillButton')
                continue;
            inputs[i].disabled = false;
        }
    }

    // Проверка второго списка
    const carList2Empty = document.querySelectorAll('.list-container:last-child .list-item').length === 0;
    if (carList2Empty) {
        inputs.forEach(input => input.disabled = true);
    } else {
        inputs.forEach(input => input.disabled = false);
    }
});