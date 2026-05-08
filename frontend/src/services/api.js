const BASE_URL = '';

async function request(url, options = {}) {
  const config = {
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    ...options,
  };

  try {
    const res = await fetch(`${BASE_URL}${url}`, config);
    const data = await res.json();
    if (!data.success) {
      throw new Error(data.error || 'Request failed');
    }
    return data;
  } catch (err) {
    if (err.message === 'Failed to fetch') {
      throw new Error('Network error. Please check your connection.');
    }
    throw err;
  }
}

function toParams(obj) {
  const params = new URLSearchParams();
  for (const key in obj) {
    if (obj[key] !== undefined && obj[key] !== null) {
      params.append(key, obj[key]);
    }
  }
  return params.toString();
}

export const api = {
  login: (username, password) =>
    request('/api/login', {
      method: 'POST',
      body: toParams({ username, password }),
    }),

  register: (username, email, password, confirmPassword) =>
    request('/api/register', {
      method: 'POST',
      body: toParams({ username, email, password, confirmPassword }),
    }),

  logout: () =>
    request('/api/logout', { method: 'POST' }),

  getSession: () =>
    request('/api/session'),

  getDashboard: () =>
    request('/api/dashboard'),

  getApps: (search) =>
    request(`/api/apps${search ? `?search=${encodeURIComponent(search)}` : ''}`),

  getAppDetails: (appId) =>
    request(`/api/apps?appId=${appId}`),

  getRateApp: (appId) =>
    request(`/api/ratings?action=rate&appId=${appId}`),

  submitRating: (appId, rating, reviewComment) =>
    request('/api/ratings', {
      method: 'POST',
      body: toParams({ action: 'submit', appId, rating, reviewComment }),
    }),

  deleteRating: (appId) =>
    request('/api/ratings', {
      method: 'POST',
      body: toParams({ action: 'delete', appId }),
    }),

  getReviews: (appId) =>
    request(`/api/reviews?appId=${appId}`),

  getHistory: () =>
    request('/api/history'),

  getAdmin: (section) =>
    request(`/api/admin${section ? `?section=${section}` : ''}`),

  adminAddApp: (appName, developerName, category, version, description, releaseDate) =>
    request('/api/admin', {
      method: 'POST',
      body: toParams({ action: 'addApp', appName, developerName, category, version, description, releaseDate }),
    }),

  adminDeleteApp: (appId) =>
    request('/api/admin', {
      method: 'POST',
      body: toParams({ action: 'deleteApp', appId }),
    }),

  adminDeleteRating: (ratingId) =>
    request('/api/admin', {
      method: 'POST',
      body: toParams({ action: 'deleteRating', ratingId }),
    }),
};
