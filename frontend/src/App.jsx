import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { api } from './services/api';
import Navbar from './components/Navbar';
import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './pages/Dashboard';
import BrowseApps from './pages/BrowseApps';
import AppDetails from './pages/AppDetails';
import RateApp from './pages/RateApp';
import History from './pages/History';
import Admin from './pages/Admin';

export default function App() {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.getSession()
      .then((res) => setUser(res.data))
      .catch(() => setUser(null))
      .finally(() => setLoading(false));
  }, []);

  const handleLogin = (userData) => setUser(userData);
  const handleLogout = () => {
    api.logout().catch(() => {});
    setUser(null);
  };

  if (loading) {
    return (
      <div className="d-flex justify-content-center align-items-center min-vh-100">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
      </div>
    );
  }

  return (
    <Router>
      <div className="d-flex flex-column min-vh-100">
        <Navbar user={user} onLogout={handleLogout} />
        <div className="flex-grow-1">
          <Routes>
            <Route path="/login" element={user ? <Navigate to="/dashboard" /> : <Login onLogin={handleLogin} />} />
            <Route path="/register" element={user ? <Navigate to="/dashboard" /> : <Register />} />
            <Route path="/dashboard" element={user ? <Dashboard user={user} /> : <Navigate to="/login" />} />
            <Route path="/apps" element={user ? <BrowseApps user={user} /> : <Navigate to="/login" />} />
            <Route path="/apps/:appId" element={user ? <AppDetails user={user} /> : <Navigate to="/login" />} />
            <Route path="/rate/:appId" element={user ? <RateApp user={user} /> : <Navigate to="/login" />} />
            <Route path="/reviews/:appId" element={user ? <AppDetails user={user} /> : <Navigate to="/login" />} />
            <Route path="/history" element={user ? <History user={user} /> : <Navigate to="/login" />} />
            <Route path="/admin/*" element={user && user.role === 'admin' ? <Admin user={user} /> : <Navigate to="/dashboard" />} />
            <Route path="/" element={<Navigate to={user ? '/dashboard' : '/login'} />} />
            <Route path="*" element={<Navigate to={user ? '/dashboard' : '/login'} />} />
          </Routes>
        </div>
      </div>
    </Router>
  );
}
