const toggleSwitch = document.querySelector('.theme-switch input[type="checkbox"]');
const currentTheme = localStorage.getItem('theme');
const deleteButton=document.getElementById('deleteBtn')
let dropDownBtn=document.getElementById('myDropdown')


if (currentTheme) {
    document.documentElement.setAttribute('data-theme', currentTheme);

    if (currentTheme === 'dark') {
        toggleSwitch.checked = true;
    }
}

function switchTheme(e) {
    if (e.target.checked) {
        document.documentElement.setAttribute('data-theme', 'dark');
        localStorage.setItem('theme', 'dark');

    }
    else {
          document.documentElement.setAttribute('data-theme', 'light');
          localStorage.setItem('theme', 'light');

    }    
}

toggleSwitch.addEventListener('change', switchTheme, false);


