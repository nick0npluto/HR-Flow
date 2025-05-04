// src/pages/LoginPage.js
import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

export default function LoginPage({ onLogin }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState("");
  const [submitted, setSubmitted] = useState(false);
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError("");
    try {
      const res = await fetch("/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password }),
      });
      
      if (res.ok) {
        const userData = await fetch("/api/auth/user");
        if (!userData.ok) throw new Error('Failed to fetch user data');
        const user = await userData.json();
        localStorage.setItem('userRole', user.role);
        onLogin(username, user.role);
        setSubmitted(true);
      } else {
        setError("Invalid credentials");
      }
    } catch (err) {
      setError("Login failed: " + err.message);
    }
  };

  useEffect(() => {
    if (submitted) {
      const userRole = localStorage.getItem('userRole');
      if (userRole === "ADMIN") {
        navigate('/admin', { replace: true });
      } else {
        navigate('/employee', { replace: true });
      }
    }
  }, [submitted, navigate]);

  return (
    <div className="login-container">
      <h2>Login</h2>
      <form onSubmit={handleLogin}>
        <div className="form-group">
          <input
            value={username}
            onChange={e => setUsername(e.target.value)}
            placeholder="Username"
            required
          />
        </div>
        <div className="form-group">
          <input
            type={showPassword ? "text" : "password"}
            value={password}
            onChange={e => setPassword(e.target.value)}
            placeholder="Password"
            required
          />
          <button 
            type="button" 
            onClick={() => setShowPassword(v => !v)}
            className="show-password"
          >
            {showPassword ? "Hide" : "Show"}
          </button>
        </div>
        <button type="submit" className="login-button">Login</button>
        {error && <div className="error-message">{error}</div>}
      </form>
    </div>
  );
}