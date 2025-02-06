function logout(e) {
  e.preventDefault();
  e.stopPropagation();

  window.location.href = '/auth/google/logout';
}