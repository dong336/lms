function logout(e) {
  e.preventDefault();
  e.stopPropagation();

  window.location.href = '/auth/logout';
}

function checkCookie(cookieName) {
    const cookieString = document.cookie;
    const cookieArray = cookieString.split(';');

    for (let i = 0; i < cookieArray.length; i++) {
        const cookie = cookieArray[i].trim();

        if (cookie.startsWith(cookieName + '=')) {
            return true;
        }
    }

    return false;
}