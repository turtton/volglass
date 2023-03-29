;(function initTheme() {
  const theme = localStorage.getItem('theme') || 'light'
  if (theme === 'dark') {
    const htmlHtmlElement = document.querySelector('html');
    htmlHtmlElement.classList.add('dark')
    htmlHtmlElement.classList.add("theme-dark")
  }
})()