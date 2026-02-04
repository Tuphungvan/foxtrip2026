# FoxTrip Frontend

React frontend cho hệ thống đặt tour du lịch FoxTrip.

## Tech Stack

- React 18
- React Router DOM
- Axios
- Vite
- CSS3

## Setup

1. Install dependencies:
```bash
npm install
```

2. Create `.env` file:
```
VITE_API_URL=http://localhost:8081/api
```

3. Run development server:
```bash
npm run dev
```

4. Build for production:
```bash
npm run build
```

## Features

- ✅ Authentication (Login/Register/Logout)
- ✅ User Profile Management
- ✅ JWT Token với Auto Refresh
- ✅ Protected Routes
- ✅ Responsive Design
- ✅ Orange Theme (giống design cũ)

## API Endpoints

### Auth
- POST `/api/auth/login` - Đăng nhập
- POST `/api/auth/register` - Đăng ký
- POST `/api/auth/logout` - Đăng xuất
- GET `/api/auth/me` - Lấy thông tin user
- POST `/api/auth/refresh` - Refresh token

### Profile
- GET `/api/profile` - Lấy profile
- PUT `/api/profile` - Cập nhật profile

## Project Structure

```
src/
├── components/       # Reusable components
│   ├── Header.jsx
│   └── PrivateRoute.jsx
├── contexts/        # React contexts
│   └── AuthContext.jsx
├── pages/           # Page components
│   ├── Home.jsx
│   ├── Login.jsx
│   ├── Register.jsx
│   └── Profile.jsx
├── services/        # API services
│   ├── authService.js
│   └── profileService.js
├── config/          # Configuration
│   └── api.js
├── styles/          # Global styles
│   ├── global.css
│   └── variables.css
├── App.jsx
└── main.jsx
```

## Notes

- Backend phải chạy ở `http://localhost:8081`
- Keycloak phải được config đúng
- CORS phải được enable trên backend
