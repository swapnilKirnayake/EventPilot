import { useEffect, useState } from "react";
import {
  clearToken,
  getToken,
  login as loginRequest,
  register as registerRequest,
  saveToken,
} from "../services/api";
import { AuthContext } from "./authContext";

function getStoredUser() {
  const storedUser = localStorage.getItem("eventpilot_user");

  if (!storedUser) {
    return null;
  }

  try {
    return JSON.parse(storedUser);
  } catch {
    localStorage.removeItem("eventpilot_user");
    return null;
  }
}

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => getToken());
  const [user, setUser] = useState(() => getStoredUser());

  useEffect(() => {
    if (token) {
      saveToken(token);
    }
  }, [token]);

  function storeAuthentication(response) {
    saveToken(response.token);

    const authenticatedUser = {
      userId: response.userId,
      name: response.name,
      email: response.email,
      role: response.role,
    };

    localStorage.setItem(
      "eventpilot_user",
      JSON.stringify(authenticatedUser),
    );

    setToken(response.token);
    setUser(authenticatedUser);
  }

  async function register(payload) {
    const response = await registerRequest(payload);
    storeAuthentication(response);
    return response;
  }

  async function login(payload) {
    const response = await loginRequest(payload);
    storeAuthentication(response);
    return response;
  }

  function logout() {
    clearToken();
    localStorage.removeItem("eventpilot_user");
    setToken(null);
    setUser(null);
  }

  const value = {
    token,
    user,
    isAuthenticated: Boolean(token && user),
    register,
    login,
    logout,
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
}