import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { api } from '../services/api';

export default function Navbar({ user, onLogout }) {
  const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      await api.logout();
    } catch (e) {}
    onLogout();
    navigate('/login');
  };

  return (
    <nav className="navbar navbar-expand-lg navbar-dark" style={{ background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' }}>
      <div className="container-fluid">
        <Link className="navbar-brand d-flex align-items-center" to={user ? '/dashboard' : '/login'}>
          <span style={{ fontSize: 24, color: '#ffd700' }}>&#9733;</span>
          <span className="ms-2 fw-bold">AppRating</span>
        </Link>
        <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className="collapse navbar-collapse" id="navbarNav">
          {user && (
            <ul className="navbar-nav me-auto">
              {user.role === 'admin' ? (
                <>
                  <li className="nav-item"><Link className="nav-link" to="/admin">Dashboard</Link></li>
                  <li className="nav-item"><Link className="nav-link" to="/admin/apps">Applications</Link></li>
                  <li className="nav-item"><Link className="nav-link" to="/admin/users">Users</Link></li>
                  <li className="nav-item"><Link className="nav-link" to="/admin/ratings">Ratings</Link></li>
                </>
              ) : (
                <>
                  <li className="nav-item"><Link className="nav-link" to="/dashboard">Dashboard</Link></li>
                  <li className="nav-item"><Link className="nav-link" to="/apps">Browse Apps</Link></li>
                  <li className="nav-item"><Link className="nav-link" to="/history">My History</Link></li>
                </>
              )}
            </ul>
          )}
          <ul className="navbar-nav">
            {user ? (
              <li className="nav-item dropdown">
                <a className="nav-link dropdown-toggle" href="#" data-bs-toggle="dropdown">
                  &#128100; {user.username}
                </a>
                <ul className="dropdown-menu dropdown-menu-end">
                  <li><span className="dropdown-item-text text-muted">Role: {user.role}</span></li>
                  <li><hr className="dropdown-divider" /></li>
                  <li><button className="dropdown-item" onClick={handleLogout}>Logout</button></li>
                </ul>
              </li>
            ) : (
              <>
                <li className="nav-item"><Link className="nav-link" to="/login">Login</Link></li>
                <li className="nav-item"><Link className="nav-link" to="/register">Register</Link></li>
              </>
            )}
          </ul>
        </div>
      </div>
    </nav>
  );
}
