const BASE_URL = '';

function getToken() { return localStorage.getItem('jwt_token'); }
function getRole()  { return localStorage.getItem('user_rol') || ''; }
function getUsername() { return localStorage.getItem('username') || ''; }

function authHeaders(extraHeaders = {}) {
  return {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${getToken()}`,
    ...extraHeaders
  };
}

async function request(url, options = {}) {
  const response = await fetch(BASE_URL + url, options);
  if (response.status === 204) return undefined;
  let body;
  try { body = await response.json(); } catch { body = null; }
  if (!response.ok) {
    const err = new Error(`HTTP ${response.status}`);
    err.status = response.status;
    err.body = body;
    throw err;
  }
  return body;
}

const Api = {
  login(username, password) {
    return request('/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password })
    });
  },
  listarVisitantes() {
    return request('/api/visitantes', { method: 'GET', headers: authHeaders() });
  },
  buscarPorId(id) {
    return request(`/api/visitantes/${id}`, { method: 'GET', headers: authHeaders() });
  },
  registrarVisitante(data) {
    return request('/api/visitantes', { method: 'POST', headers: authHeaders(), body: JSON.stringify(data) });
  },
  actualizarVisitante(id, data) {
    return request(`/api/visitantes/${id}`, { method: 'PUT', headers: authHeaders(), body: JSON.stringify(data) });
  },
  registrarSalida(id) {
    return request(`/api/visitantes/${id}/salida`, { method: 'PATCH', headers: authHeaders() });
  },
  eliminarVisitante(id) {
    return request(`/api/visitantes/${id}`, { method: 'DELETE', headers: authHeaders() });
  },
  listarUsuarios() {
    return request('/api/usuarios', { method: 'GET', headers: authHeaders() });
  },
  crearUsuario(data) {
    return request('/api/usuarios', { method: 'POST', headers: authHeaders(), body: JSON.stringify(data) });
  },
  actualizarUsuario(id, data) {
    return request(`/api/usuarios/${id}`, { method: 'PUT', headers: authHeaders(), body: JSON.stringify(data) });
  },
  eliminarUsuario(id) {
    return request(`/api/usuarios/${id}`, { method: 'DELETE', headers: authHeaders() });
  },
  listarAuditLogs() {
    return request('/api/audit', { method: 'GET', headers: authHeaders() });
  }
};

function showToast(message, type = 'info', duration = 3500) {
  const container = document.getElementById('toast-container');
  if (!container) return;
  const icons = { success: 'Hecho', error: 'Error', info: 'Info' };
  const toast = document.createElement('div');
  toast.className = `toast toast-${type}`;
  toast.setAttribute('role', 'status');
  toast.innerHTML = `<span class="toast-icon" aria-hidden="true">${icons[type] ?? 'Info'}</span><span>${escapeHtmlBasic(message)}</span>`;
  container.appendChild(toast);
  setTimeout(() => {
    toast.classList.add('out');
    toast.addEventListener('animationend', () => toast.remove());
  }, duration);
}

function escapeHtmlBasic(str) {
  if (!str) return '';
  return String(str).replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;').replace(/"/g,'&quot;');
}
