import React, { useState } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import FAQPage from "./pages/FAQPage";
import AdminDashboard from "./pages/AdminDashboard";
import EmployeeDashboard from "./pages/EmployeeDashboard";
import Header from "./components/Header";
import LogoutButton from "./components/LogoutButton";

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userRole, setUserRole] = useState(null);

  const handleLogin = (username, role) => {
    setIsLoggedIn(true);
    setUserRole(role);
  };

  const handleLogout = () => {
    setIsLoggedIn(false);
    setUserRole(null);
  };

  return (
    <BrowserRouter>
      <Header />
      {isLoggedIn && <LogoutButton onLogout={handleLogout} />}
      <Routes>
        <Route 
          path="/login" 
          element={
            isLoggedIn ? 
              <Navigate to={userRole === "ADMIN" ? "/admin" : "/employee"} replace /> : 
              <LoginPage onLogin={handleLogin} />
          } 
        />
        <Route 
          path="/admin" 
          element={
            isLoggedIn && userRole === "ADMIN" ? 
              <AdminDashboard /> : 
              <Navigate to="/login" replace />
          } 
        />
        <Route 
          path="/employee" 
          element={
            isLoggedIn && userRole === "EMPLOYEE" ? 
              <EmployeeDashboard /> : 
              <Navigate to="/login" replace />
          } 
        />
        <Route 
          path="/faq" 
          element={
            isLoggedIn ? 
              <FAQPage /> : 
              <Navigate to="/login" replace />
          } 
        />
        <Route 
          path="*" 
          element={
            isLoggedIn ? 
              <Navigate to={userRole === "ADMIN" ? "/admin" : "/employee"} replace /> : 
              <Navigate to="/login" replace />
          } 
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
