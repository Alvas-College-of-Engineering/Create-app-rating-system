import React from 'react';

export function StarRatingDisplay({ rating, size }) {
  const numRating = parseFloat(rating) || 0;
  const fullStars = Math.floor(numRating);
  const hasHalf = numRating - fullStars >= 0.5;
  const cls = size === 'small' ? 'star small' : 'star';

  const stars = [];
  for (let i = 1; i <= 5; i++) {
    if (i <= fullStars) stars.push(<span key={i} className={`${cls} filled`}>&#9733;</span>);
    else if (i === fullStars + 1 && hasHalf) stars.push(<span key={i} className={`${cls} filled`}>&#9733;</span>);
    else stars.push(<span key={i} className={`${cls} empty`}>&#9734;</span>);
  }

  return (
    <div className="star-rating d-inline-flex align-items-center">
      {stars}
      <span className="ms-2 text-muted">({numRating.toFixed(1)})</span>
    </div>
  );
}

export function StarRatingInput({ value, onChange }) {
  return (
    <div className="d-inline-flex align-items-center">
      {[1, 2, 3, 4, 5].map((star) => (
        <button
          key={star}
          type="button"
          className={`star-btn ${star <= value ? 'filled' : 'empty'}`}
          onClick={() => onChange(star)}
        >
          {star <= value ? '\u2605' : '\u2606'}
        </button>
      ))}
    </div>
  );
}

export function StarRatingSimple({ rating }) {
  const numRating = parseFloat(rating) || 0;
  const fullStars = Math.floor(numRating);
  const stars = [];
  for (let i = 1; i <= 5; i++) {
    stars.push(
      <span key={i} className={`star small ${i <= fullStars ? 'filled' : 'empty'}`}>
        {i <= fullStars ? '\u2605' : '\u2606'}
      </span>
    );
  }
  return <div className="d-inline-flex">{stars}</div>;
}
