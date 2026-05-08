import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { api } from '../services/api';
import { StarRatingInput } from '../components/StarRating';

export default function RateApp({ user }) {
  const { appId } = useParams();
  const navigate = useNavigate();
  const [app, setApp] = useState(null);
  const [existingRating, setExistingRating] = useState(null);
  const [rating, setRating] = useState(0);
  const [reviewComment, setReviewComment] = useState('');
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    api.getRateApp(appId)
      .then((res) => {
        setApp(res.data.app);
        if (res.data.existingRating) {
          setExistingRating(res.data.existingRating);
          setRating(res.data.existingRating.ratingValue);
          setReviewComment(res.data.existingRating.reviewComment || '');
        }
      })
      .catch(() => setError('Failed to load'))
      .finally(() => setLoading(false));
  }, [appId]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setMessage('');
    if (rating < 1 || rating > 5) {
      setError('Please select a rating (1-5 stars)');
      return;
    }
    setSubmitting(true);
    try {
      await api.submitRating(appId, rating, reviewComment);
      setMessage('Rating submitted successfully!');
      setTimeout(() => navigate(`/apps/${appId}`), 1500);
    } catch (err) {
      setError(err.message);
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async () => {
    if (!confirm('Delete your rating?')) return;
    try {
      await api.deleteRating(appId);
      setMessage('Rating deleted!');
      setTimeout(() => navigate('/dashboard'), 1500);
    } catch (err) {
      setError(err.message);
    }
  };

  if (loading) {
    return <div className="d-flex justify-content-center p-5"><div className="spinner-border text-primary" /></div>;
  }

  if (!app) {
    return <div className="container p-4"><p>Application not found.</p></div>;
  }

  return (
    <div className="container py-4">
      <div className="row justify-content-center">
        <div className="col-md-6">
          <div className="card border-0 shadow-sm rounded-4">
            <div className="card-body p-5">
              <div className="text-center mb-4">
                <div className="app-icon-lg mx-auto mb-3"><span>&#9733;</span></div>
                <h4 className="fw-bold">{app.appName}</h4>
                <p className="text-muted">{app.developerName}</p>
              </div>

              {error && <div className="alert alert-danger">{error}</div>}
              {message && <div className="alert alert-success">{message}</div>}

              <form onSubmit={handleSubmit}>
                <div className="text-center mb-4">
                  <label className="form-label d-block fw-bold fs-5">
                    {existingRating ? 'Update Your Rating' : 'Rate this Application'}
                  </label>
                  <StarRatingInput value={rating} onChange={setRating} />
                  {rating > 0 && <p className="mt-2 text-muted">{rating}/5 stars</p>}
                </div>

                <div className="mb-4">
                  <label className="form-label">Review Comment (Optional)</label>
                  <textarea
                    className="form-control"
                    rows="4"
                    placeholder="Share your experience with this app..."
                    value={reviewComment}
                    onChange={(e) => setReviewComment(e.target.value)}
                  ></textarea>
                </div>

                <div className="d-flex gap-2">
                  <button type="submit" className="btn btn-gradient-primary flex-fill" disabled={submitting}>
                    {submitting ? 'Submitting...' : existingRating ? 'Update Rating' : 'Submit Rating'}
                  </button>
                  {existingRating && (
                    <button type="button" className="btn btn-outline-danger" onClick={handleDelete}>
                      Delete
                    </button>
                  )}
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
