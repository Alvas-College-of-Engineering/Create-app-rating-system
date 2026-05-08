import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { api } from '../services/api';
import { StarRatingDisplay } from '../components/StarRating';

function StatCard({ title, value, icon, color }) {
  return (
    <div className={`card border-0 shadow-sm rounded-4 stat-card ${color} mb-3`}>
      <div className="card-body d-flex align-items-center p-4">
        <div className="stat-icon me-3">{icon}</div>
        <div>
          <h6 className="text-muted mb-1">{title}</h6>
          <h2 className="fw-bold mb-0">{value}</h2>
        </div>
      </div>
    </div>
  );
}

function AppCard({ app }) {
  return (
    <div className="col-md-6 col-lg-4 mb-4">
      <div className="card border-0 shadow-sm rounded-4 app-card h-100">
        <div className="card-body p-4">
          <div className="d-flex align-items-center mb-3">
            <div className="app-icon me-3">
              <span>&#9733;</span>
            </div>
            <div>
              <h5 className="fw-bold mb-0">{app.appName}</h5>
              <small className="text-muted">{app.developerName}</small>
            </div>
          </div>
          <div className="mb-2">
            <span className="badge bg-primary me-2">{app.category}</span>
            <span className="badge bg-secondary">v{app.version}</span>
          </div>
          <StarRatingDisplay rating={app.averageRating || 0} />
          <div><small className="text-muted">{app.totalRatings || 0} rating(s)</small></div>
          <p className="card-text mt-2 text-muted small">
            {app.description ? app.description.substring(0, 80) + '...' : 'No description'}
          </p>
          <div className="d-flex gap-2 mt-3">
            <Link to={`/apps/${app.appId}`} className="btn btn-outline-primary btn-sm flex-fill">View</Link>
            <Link to={`/rate/${app.appId}`} className="btn btn-primary btn-sm flex-fill">Rate</Link>
            <Link to={`/apps/${app.appId}`} className="btn btn-outline-secondary btn-sm flex-fill">Reviews</Link>
          </div>
        </div>
      </div>
    </div>
  );
}

export default function Dashboard({ user }) {
  const [data, setData] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    api.getDashboard().then((res) => setData(res.data)).catch(() => {});
  }, []);

  if (!data) {
    return (
      <div className="d-flex justify-content-center align-items-center p-5">
        <div className="spinner-border text-primary" role="status" />
      </div>
    );
  }

  const filteredApps = data.apps.filter(
    (app) => app.appName && app.appName.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="container-fluid p-4">
      <div className="row mb-4">
        <div className="col">
          <h3 className="fw-bold">Welcome, {user.username}!</h3>
          <p className="text-muted">Explore and rate your favorite applications</p>
        </div>
      </div>

      <div className="row mb-4">
        <div className="col-md-3"><StatCard title="Total Apps" value={data.totalApps} icon="&#128187;" color="stat-blue" /></div>
        <div className="col-md-3"><StatCard title="Total Ratings" value={data.totalRatings} icon="&#9733;" color="stat-gold" /></div>
        <div className="col-md-3"><StatCard title="Avg Rating" value={data.overallAvg} icon="&#128200;" color="stat-green" /></div>
        <div className="col-md-3"><StatCard title="Total Users" value={data.totalUsers} icon="&#128101;" color="stat-purple" /></div>
      </div>

      <div className="row mb-4 align-items-center">
        <div className="col-md-4">
          <h5 className="fw-bold mb-0">Available Applications</h5>
        </div>
        <div className="col-md-4 ms-auto">
          <input
            type="text"
            className="form-control"
            placeholder="Search apps..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
      </div>

      <div className="row">
        {filteredApps.length > 0 ? filteredApps.map((app) => (
          <AppCard key={app.appId} app={app} />
        )) : (
          <div className="col text-center py-5">
            <p className="text-muted">No applications found</p>
          </div>
        )}
      </div>
    </div>
  );
}
