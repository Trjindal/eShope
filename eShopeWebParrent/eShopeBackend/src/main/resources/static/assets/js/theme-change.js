const toggleSwitch = document.querySelector('.theme-switch input[type="checkbox"]');
const currentTheme = localStorage.getItem('theme');
const deleteButton=document.getElementById('deleteBtn')


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
        deleteButton.addEventListener('click',()=>{
              document.getElementById('myModal').style.backgroundColor="black";

            })
    }
    else {        document.documentElement.setAttribute('data-theme', 'light');
          localStorage.setItem('theme', 'light');
          deleteButton.addEventListener('click',()=>{
                document.getElementById('myModal').style.backgroundColor="white";
              })
    }    
}

toggleSwitch.addEventListener('change', switchTheme, false);


