import React, { useState, useEffect } from 'react';
import { Routes, Route, useNavigate } from 'react-router-dom';
import { api } from '../services/api';
import { StarRatingSimple } from '../components/StarRating';
import AdminSidebar from '../components/AdminSidebar';

function AdminDashboard() {
  const [data, setData] = useState(null);
  useEffect(() => {
    api.getAdmin('dashboard').then((res) => setData(res.data)).catch(() => {});
  }, []);
  if (!data) return <div className="spinner-border text-primary" />;
  return (
    <>
      <h3 className="fw-bold mb-4">Admin Dashboard</h3>
      <div className="row">
        <div className="col-md-3 mb-4">
          <div className="card border-0 shadow-sm rounded-4 stat-card stat-blue">
            <div className="card-body p-4">
              <h6 className="text-muted">Total Users</h6>
              <h2 className="fw-bold">{data.totalUsers || 0}</h2>
            </div>
          </div>
        </div>
        <div className="col-md-3 mb-4">
          <div className="card border-0 shadow-sm rounded-4 stat-card stat-green">
            <div className="card-body p-4">
              <h6 className="text-muted">Total Applications</h6>
              <h2 className="fw-bold">{data.totalApps || 0}</h2>
            </div>
          </div>
        </div>
        <div className="col-md-3 mb-4">
          <div className="card border-0 shadow-sm rounded-4 stat-card stat-gold">
            <div className="card-body p-4">
              <h6 className="text-muted">Total Ratings</h6>
              <h2 className="fw-bold">{data.totalRatings || 0}</h2>
            </div>
          </div>
        </div>
        <div className="col-md-3 mb-4">
          <div className="card border-0 shadow-sm rounded-4 stat-card stat-purple">
            <div className="card-body p-4">
              <h6 className="text-muted">Avg Rating</h6>
              <h2 className="fw-bold">{data.overallAvg || 0}</h2>
            </div>
          </div>
        </div>
      </div>
      <div className="card border-0 shadow-sm rounded-4">
        <div className="card-body p-4 text-center">
          <h5 className="text-muted">Welcome to Admin Dashboard</h5>
          <p>Manage applications, users, and monitor ratings</p>
        </div>
      </div>
    </>
  );
}

function AdminApps() {
  const [apps, setApps] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [form, setForm] = useState({ appName: '', developerName: '', category: '', version: '', description: '', releaseDate: '' });
  const [message, setMessage] = useState('');

  const loadApps = () => api.getAdmin('applications').then((res) => setApps(res.data.apps || [])).catch(() => {});
  useEffect(() => { loadApps(); }, []);

  const handleAdd = async (e) => {
    e.preventDefault();
    try {
      await api.adminAddApp(form.appName, form.developerName, form.category, form.version, form.description, form.releaseDate);
      setMessage('Application added!');
      setShowModal(false);
      setForm({ appName: '', developerName: '', category: '', version: '', description: '', releaseDate: '' });
      loadApps();
    } catch (err) { setMessage(err.message); }
  };

  const handleDelete = async (appId) => {
    if (!confirm('Delete this application?')) return;
    try {
      await api.adminDeleteApp(appId);
      setMessage('Application deleted!');
      loadApps();
    } catch (err) { setMessage(err.message); }
  };

  return (
    <>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h3 className="fw-bold mb-0">Manage Applications</h3>
        <button className="btn btn-gradient-primary" onClick={() => setShowModal(true)}>+ Add Application</button>
      </div>

      {message && <div className="alert alert-info alert-dismissible"><button type="button" className="btn-close" onClick={() => setMessage('')}></button>{message}</div>}

      <div className="card border-0 shadow-sm rounded-4">
        <div className="card-body p-0">
          <div className="table-responsive">
            <table className="table table-hover mb-0">
              <thead className="table-light">
                <tr><th className="ps-4">ID</th><th>Name</th><th>Developer</th><th>Category</th><th>Version</th><th>Status</th><th className="pe-4">Actions</th></tr>
              </thead>
              <tbody>
                {apps.length > 0 ? apps.map((a) => (
                  <tr key={a.appId}>
                    <td className="ps-4">{a.appId}</td>
                    <td><strong>{a.appName}</strong></td>
                    <td>{a.developerName}</td>
                    <td><span className="badge bg-primary">{a.category}</span></td>
                    <td>v{a.version}</td>
                    <td><span className={`badge bg-${a.status === 'active' ? 'success' : 'secondary'}`}>{a.status}</span></td>
                    <td className="pe-4">
                      <button className="btn btn-sm btn-outline-danger" onClick={() => handleDelete(a.appId)}>Delete</button>
                    </td>
                  </tr>
                )) : (
                  <tr><td colSpan="7" className="text-center py-4 text-muted">No applications</td></tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      {showModal && (
        <div className="modal fade show d-block" tabIndex="-1" style={{ backgroundColor: 'rgba(0,0,0,0.5)' }}>
          <div className="modal-dialog modal-lg">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title">Add New Application</h5>
                <button type="button" className="btn-close" onClick={() => setShowModal(false)}></button>
              </div>
              <form onSubmit={handleAdd}>
                <div className="modal-body">
                  <div className="row">
                    <div className="col-md-6 mb-3">
                      <label className="form-label">App Name</label>
                      <input type="text" className="form-control" required value={form.appName}
                        onChange={(e) => setForm({ ...form, appName: e.target.value })} />
                    </div>
                    <div className="col-md-6 mb-3">
                      <label className="form-label">Developer Name</label>
                      <input type="text" className="form-control" required value={form.developerName}
                        onChange={(e) => setForm({ ...form, developerName: e.target.value })} />
                    </div>
                  </div>
                  <div className="row">
                    <div className="col-md-4 mb-3">
                      <label className="form-label">Category</label>
                      <select className="form-control" required value={form.category}
                        onChange={(e) => setForm({ ...form, category: e.target.value })}>
                        <option value="">Select...</option>
                        <option>Communication</option><option>Social Media</option><option>Music</option>
                        <option>Navigation</option><option>Video</option><option>Productivity</option>
                        <option>Entertainment</option><option>Shopping</option><option>Education</option><option>Health</option>
                      </select>
                    </div>
                    <div className="col-md-4 mb-3">
                      <label className="form-label">Version</label>
                      <input type="text" className="form-control" required value={form.version}
                        onChange={(e) => setForm({ ...form, version: e.target.value })} />
                    </div>
                    <div className="col-md-4 mb-3">
                      <label className="form-label">Release Date</label>
                      <input type="date" className="form-control" value={form.releaseDate}
                        onChange={(e) => setForm({ ...form, releaseDate: e.target.value })} />
                    </div>
                  </div>
                  <div className="mb-3">
                    <label className="form-label">Description</label>
                    <textarea className="form-control" rows="3" value={form.description}
                      onChange={(e) => setForm({ ...form, description: e.target.value })}></textarea>
                  </div>
                </div>
                <div className="modal-footer">
                  <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>Cancel</button>
                  <button type="submit" className="btn btn-gradient-primary">Add Application</button>
                </div>
              </form>
            </div>
          </div>
        </div>
      )}
    </>
  );
}

function AdminUsers() {
  const [users, setUsers] = useState([]);
  useEffect(() => {
    api.getAdmin('users').then((res) => setUsers(res.data.users || [])).catch(() => {});
  }, []);
  return (
    <>
      <h3 className="fw-bold mb-4">Manage Users</h3>
      <div className="card border-0 shadow-sm rounded-4">
        <div className="card-body p-0">
          <div className="table-responsive">
            <table className="table table-hover mb-0">
              <thead className="table-light">
                <tr><th className="ps-4">ID</th><th>Username</th><th>Email</th><th>Role</th><th>Joined</th></tr>
              </thead>
              <tbody>
                {users.length > 0 ? users.map((u) => (
                  <tr key={u.userId}>
                    <td className="ps-4">{u.userId}</td>
                    <td><strong>{u.username}</strong></td>
                    <td>{u.email}</td>
                    <td><span className={`badge bg-${u.role === 'admin' ? 'danger' : 'info'}`}>{u.role}</span></td>
                    <td className="text-muted">{u.createdAt}</td>
                  </tr>
                )) : (
                  <tr><td colSpan="5" className="text-center py-4 text-muted">No users</td></tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </>
  );
}

function AdminRatings() {
  const [ratings, setRatings] = useState([]);
  const [message, setMessage] = useState('');

  const loadRatings = () => api.getAdmin('ratings').then((res) => setRatings(res.data.ratings || [])).catch(() => {});
  useEffect(() => { loadRatings(); }, []);

  const handleDeleteRating = async (ratingId) => {
    if (!confirm('Delete this rating?')) return;
    try {
      await api.adminDeleteRating(ratingId);
      setMessage('Rating deleted!');
      loadRatings();
    } catch (err) { setMessage(err.message); }
  };

  return (
    <>
      <h3 className="fw-bold mb-4">All Ratings</h3>
      {message && <div className="alert alert-info alert-dismissible"><button type="button" className="btn-close" onClick={() => setMessage('')}></button>{message}</div>}
      <div className="card border-0 shadow-sm rounded-4">
        <div className="card-body p-0">
          <div className="table-responsive">
            <table className="table table-hover mb-0">
              <thead className="table-light">
                <tr><th className="ps-4">ID</th><th>User</th><th>Application</th><th>Rating</th><th>Review</th><th>Date</th><th className="pe-4">Actions</th></tr>
              </thead>
              <tbody>
                {ratings.length > 0 ? ratings.map((r) => (
                  <tr key={r.ratingId}>
                    <td className="ps-4">{r.ratingId}</td>
                    <td>{r.username}</td>
                    <td><strong>{r.appName}</strong></td>
                    <td><StarRatingSimple rating={r.ratingValue} /></td>
                    <td className="text-muted">{r.reviewComment || '-'}</td>
                    <td className="small text-muted">{r.ratedOn}</td>
                    <td className="pe-4">
                      <button className="btn btn-sm btn-outline-danger" onClick={() => handleDeleteRating(r.ratingId)}>Delete</button>
                    </td>
                  </tr>
                )) : (
                  <tr><td colSpan="7" className="text-center py-4 text-muted">No ratings</td></tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </>
  );
}

export default function Admin({ user }) {
  return (
    <div className="container-fluid">
      <div className="row">
        <AdminSidebar />
        <div className="col-md-10 p-4">
          <Routes>
            <Route index element={<AdminDashboard />} />
            <Route path="apps" element={<AdminApps />} />
            <Route path="users" element={<AdminUsers />} />
            <Route path="ratings" element={<AdminRatings />} />
          </Routes>
        </div>
      </div>
    </div>
  );
}
