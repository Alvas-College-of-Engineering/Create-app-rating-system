import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { api } from '../services/api';
import { StarRatingDisplay, StarRatingSimple } from '../components/StarRating';

export default function AppDetails({ user }) {
  const { appId } = useParams();
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.getAppDetails(appId)
      .then((res) => setData(res.data))
      .catch(() => {})
      .finally(() => setLoading(false));
  }, [appId]);

  if (loading) {
    return <div className="d-flex justify-content-center p-5"><div className="spinner-border text-primary" /></div>;
  }

  if (!data || !data.app) {
    return <div className="container p-4"><p>Application not found.</p></div>;
  }

  const { app, ratings, avgRating, userRating } = data;

  return (
    <div className="container-fluid p-4">
      <div className="row">
        <div className="col-md-8">
          <div className="card border-0 shadow-sm rounded-4 mb-4">
            <div className="card-body p-4">
              <div className="d-flex align-items-center mb-3">
                <div className="app-icon-lg me-3"><span>&#9733;</span></div>
                <div>
                  <h3 className="fw-bold mb-0">{app.appName}</h3>
                  <p className="text-muted mb-0">{app.developerName}</p>
                </div>
              </div>
              <div className="mb-3">
                <span className="badge bg-primary me-2">{app.category}</span>
                <span className="badge bg-secondary me-2">v{app.version}</span>
                <span className={`badge bg-${app.status === 'active' ? 'success' : 'secondary'}`}>{app.status}</span>
              </div>
              <StarRatingDisplay rating={avgRating || 0} />
              <p className="mt-3">{app.description || 'No description available.'}</p>
              {app.releaseDate && <small className="text-muted">Released: {app.releaseDate}</small>}
            </div>
          </div>

          <div className="card border-0 shadow-sm rounded-4">
            <div className="card-header bg-transparent border-0 pt-4 px-4">
              <h5 className="fw-bold mb-0">Reviews & Ratings ({ratings ? ratings.length : 0})</h5>
            </div>
            <div className="card-body p-4">
              {ratings && ratings.length > 0 ? ratings.map((r) => (
                <div key={r.ratingId} className="d-flex mb-4 pb-3 border-bottom">
                  <div className="review-avatar me-3">
                    {r.username ? r.username.charAt(0).toUpperCase() : '?'}
                  </div>
                  <div className="flex-grow-1">
                    <div className="d-flex justify-content-between">
                      <strong>{r.username}</strong>
                      <small className="text-muted">{r.ratedOn}</small>
                    </div>
                    <StarRatingSimple rating={r.ratingValue} />
                    {r.reviewComment && <p className="mt-1 mb-0 text-muted">{r.reviewComment}</p>}
                  </div>
                </div>
              )) : (
                <p className="text-muted">No reviews yet. Be the first to rate!</p>
              )}
            </div>
          </div>
        </div>

        <div className="col-md-4">
          <div className="card border-0 shadow-sm rounded-4 mb-4">
            <div className="card-body p-4 text-center">
              <h6 className="fw-bold">Average Rating</h6>
              <h1 className="text-gradient fw-bold">{avgRating || 0}</h1>
              <StarRatingDisplay rating={avgRating || 0} />
              <p className="text-muted mt-2">Based on {ratings ? ratings.length : 0} ratings</p>
            </div>
          </div>

          <div className="card border-0 shadow-sm rounded-4">
            <div className="card-body p-4 text-center">
              <h6 className="fw-bold">Your Rating</h6>
              {userRating ? (
                <>
                  <StarRatingSimple rating={userRating.ratingValue} />
                  <p className="text-muted mt-1">You rated: {userRating.ratingValue}/5</p>
                  <Link to={`/rate/${appId}`} className="btn btn-outline-primary btn-sm me-2">Edit</Link>
                </>
              ) : (
                <>
                  <p className="text-muted">You haven't rated this app yet</p>
                  <Link to={`/rate/${appId}`} className="btn btn-gradient-primary btn-sm">Rate Now</Link>
                </>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
