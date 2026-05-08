import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { api } from '../services/api';
import { StarRatingSimple } from '../components/StarRating';

export default function History({ user }) {
  const [ratings, setRatings] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.getHistory()
      .then((res) => setRatings(res.data.ratings || []))
      .catch(() => {})
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return <div className="d-flex justify-content-center p-5"><div className="spinner-border text-primary" /></div>;
  }

  return (
    <div className="container-fluid p-4">
      <div className="row mb-4">
        <div className="col">
          <h3 className="fw-bold">My Rating History</h3>
          <p className="text-muted">Track all your ratings and reviews</p>
        </div>
      </div>

      <div className="card border-0 shadow-sm rounded-4">
        <div className="card-body p-0">
          <div className="table-responsive">
            <table className="table table-hover mb-0">
              <thead className="table-light">
                <tr>
                  <th className="ps-4">#</th>
                  <th>Application</th>
                  <th>Rating</th>
                  <th>Review</th>
                  <th>Date</th>
                  <th className="pe-4">Actions</th>
                </tr>
              </thead>
              <tbody>
                {ratings.length > 0 ? ratings.map((r, i) => (
                  <tr key={r.ratingId}>
                    <td className="ps-4">{i + 1}</td>
                    <td><strong>{r.appName}</strong></td>
                    <td><StarRatingSimple rating={r.ratingValue} /></td>
                    <td className="text-muted">{r.reviewComment || '-'}</td>
                    <td className="small text-muted">{r.ratedOn}</td>
                    <td className="pe-4">
                      <Link to={`/rate/${r.appId}`} className="btn btn-sm btn-outline-primary me-1">Edit</Link>
                      <Link to={`/apps/${r.appId}`} className="btn btn-sm btn-outline-secondary">View</Link>
                    </td>
                  </tr>
                )) : (
                  <tr><td colSpan="6" className="text-center py-4 text-muted">No ratings yet. Start rating apps!</td></tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
}
