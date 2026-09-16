import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import ProtectedRoute from "./components/common/ProtectedRoute";
import Login from "./pages/Login/Login";
import Register from "./pages/Register/Register";
import { useAuth } from "./hooks/useAuth";

function Home() {
  return (
    <main>
      <h1>EventPilot</h1>
      <p>Event discovery platform.</p>
    </main>
  );
}

function Dashboard() {
  const { user, logout } = useAuth();

  return (
    <main>
      <h1>Dashboard</h1>

      <p>Welcome, {user.name}.</p>
      <p>Email: {user.email}</p>
      <p>Role: {user.role}</p>

      <button type="button" onClick={logout}>
        Logout
      </button>
    </main>
  );
}

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />

        <Route path="/login" element={<Login />} />

        <Route path="/register" element={<Register />} />

        <Route element={<ProtectedRoute />}>
          <Route path="/dashboard" element={<Dashboard />} />
        </Route>

        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;