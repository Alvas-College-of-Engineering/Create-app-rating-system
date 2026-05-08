import React from 'react';
import { NavLink } from 'react-router-dom';

export default function AdminSidebar() {
  const links = [
    { to: '/admin', label: 'Dashboard', icon: '📊', exact: true },
    { to: '/admin/apps', label: 'Applications', icon: '💻' },
    { to: '/admin/users', label: 'Users', icon: '👤' },
    { to: '/admin/ratings', label: 'Ratings', icon: '⭐' },
  ];

  return (
    <div className="col-md-2 bg-dark sidebar min-vh-100 p-3">
      <h6 className="text-white-50 text-uppercase small mb-3">Admin Panel</h6>
      <ul className="nav flex-column">
        {links.map((link) => (
          <li className="nav-item" key={link.to}>
            <NavLink
              to={link.to}
              end={link.exact}
              className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}
            >
              {link.icon} {link.label}
            </NavLink>
          </li>
        ))}
      </ul>
    </div>
  );
}
