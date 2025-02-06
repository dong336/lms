function logout(e) {
  e.preventDefault();
  e.stopPropagation();

  document.cookie = 'jwtToken' + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT; path=/;';
  window.location.href = '/auth/logout/google';
}