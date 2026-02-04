import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import Header from './components/Header';
import Footer from './components/Footer';
import PrivateRoute from './components/PrivateRoute';
import AdminRoute from './components/AdminRoute';
import AdminLayout from './components/AdminLayout';
import Home from './pages/Home';
import Tours from './pages/Tours';
import Login from './pages/Login';
import Register from './pages/Register';
import Profile from './pages/Profile';
import AdminDashboard from './pages/admin/AdminDashboard';
import TourList from './pages/admin/TourList';
import TourForm from './pages/admin/TourForm';
import './styles/global.css';

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          {/* Public routes with Header/Footer */}
          <Route path="/" element={
            <div className="app">
              <Header />
              <main>
                <Home />
              </main>
              <Footer />
            </div>
          } />
          <Route path="/login" element={
            <div className="app">
              <Header />
              <main>
                <Login />
              </main>
              <Footer />
            </div>
          } />
          <Route path="/register" element={
            <div className="app">
              <Header />
              <main>
                <Register />
              </main>
              <Footer />
            </div>
          } />
          <Route path="/profile" element={
            <div className="app">
              <Header />
              <main>
                <PrivateRoute>
                  <Profile />
                </PrivateRoute>
              </main>
              <Footer />
            </div>
          } />
          
          {/* Placeholder routes */}
          <Route path="/tours" element={
            <div className="app">
              <Header />
              <main>
                <Tours />
              </main>
              <Footer />
            </div>
          } />
          <Route path="/hotels" element={
            <div className="app">
              <Header />
              <main>
                <div style={{paddingTop: '10rem', textAlign: 'center'}}><h1>Hotels (Coming soon)</h1></div>
              </main>
              <Footer />
            </div>
          } />
          <Route path="/restaurants" element={
            <div className="app">
              <Header />
              <main>
                <div style={{paddingTop: '10rem', textAlign: 'center'}}><h1>Restaurants (Coming soon)</h1></div>
              </main>
              <Footer />
            </div>
          } />
          <Route path="/transportation" element={
            <div className="app">
              <Header />
              <main>
                <div style={{paddingTop: '10rem', textAlign: 'center'}}><h1>Transportation (Coming soon)</h1></div>
              </main>
              <Footer />
            </div>
          } />

          {/* Admin routes without Header/Footer */}
          <Route path="/admin" element={
            <AdminRoute>
              <AdminLayout />
            </AdminRoute>
          }>
            <Route path="dashboard" element={<AdminDashboard />} />
            <Route path="tours" element={<TourList />} />
            <Route path="tours/create" element={<TourForm />} />
            <Route path="tours/edit/:id" element={<TourForm />} />
            <Route path="users" element={<div style={{padding: '20px'}}><h1>Quản lý người dùng (Coming soon)</h1></div>} />
            <Route path="bookings" element={<div style={{padding: '20px'}}><h1>Quản lý đơn hàng (Coming soon)</h1></div>} />
            <Route path="reports" element={<div style={{padding: '20px'}}><h1>Báo cáo doanh thu (Coming soon)</h1></div>} />
          </Route>
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;
