import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import Header from './components/Header';
import Footer from './components/Footer';
import PrivateRoute from './components/PrivateRoute';
import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import Profile from './pages/Profile';
import './styles/global.css';

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <div className="app">
          <Header />
          <main>
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route 
                path="/profile" 
                element={
                  <PrivateRoute>
                    <Profile />
                  </PrivateRoute>
                } 
              />
              {/* Placeholder routes */}
              <Route path="/tours" element={<div style={{paddingTop: '10rem', textAlign: 'center'}}><h1>Tours (Coming soon)</h1></div>} />
              <Route path="/hotels" element={<div style={{paddingTop: '10rem', textAlign: 'center'}}><h1>Hotels (Coming soon)</h1></div>} />
              <Route path="/restaurants" element={<div style={{paddingTop: '10rem', textAlign: 'center'}}><h1>Restaurants (Coming soon)</h1></div>} />
              <Route path="/transportation" element={<div style={{paddingTop: '10rem', textAlign: 'center'}}><h1>Transportation (Coming soon)</h1></div>} />
            </Routes>
          </main>
          <Footer />
        </div>
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;
