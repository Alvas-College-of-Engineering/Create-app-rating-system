import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { api } from '../services/api';
import { StarRatingDisplay } from '../components/StarRating';

export default function BrowseApps({ user }) {
  const [apps, setApps] = useState([]);
  const [search, setSearch] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.getApps(search)
      .then((res) => setApps(res.data.apps || []))
      .catch(() => {})
      .finally(() => setLoading(false));
  }, [search]);

  const handleSearch = (e) => {
    e.preventDefault();
    setLoading(true);
    api.getApps(search)
      .then((res) => setApps(res.data.apps || []))
      .catch(() => {})
      .finally(() => setLoading(false));
  };

  if (loading) {
    return <div className="d-flex justify-content-center p-5"><div className="spinner-border text-primary" /></div>;
  }

  return (
    <div className="container-fluid p-4">
      <div className="row mb-4">
        <div className="col">
          <h3 className="fw-bold">Browse Applications</h3>
          <p className="text-muted">Discover and rate applications</p>
        </div>
        <div className="col-md-4">
          <form onSubmit={handleSearch} className="d-flex">
            <input
              type="text"
              className="form-control me-2"
              placeholder="Search by name, category, developer..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
            />
            <button type="submit" className="btn btn-gradient-primary">Search</button>
          </form>
        </div>
      </div>

      <div className="row">
        {apps.length > 0 ? apps.map((app) => (
          <div key={app.appId} className="col-md-6 col-lg-4 mb-4">
            <div className="card border-0 shadow-sm rounded-4 app-card h-100">
              <div className="card-body p-4">
                <div className="d-flex align-items-center mb-3">
                  <div className="app-icon me-3"><span>&#9733;</span></div>
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
                <div className="d-flex gap-2 mt-3">
                  <Link to={`/apps/${app.appId}`} className="btn btn-outline-primary btn-sm flex-fill">View Details</Link>
                  <Link to={`/rate/${app.appId}`} className="btn btn-primary btn-sm flex-fill">Rate</Link>
                </div>
              </div>
            </div>
          </div>
        )) : (
          <div className="col text-center py-5">
            <p className="text-muted">No applications found</p>
          </div>
        )}
      </div>
    </div>
  );
}
