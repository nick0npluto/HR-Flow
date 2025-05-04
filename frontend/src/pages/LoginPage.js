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
    <div style={{
      width: '100%',
      minHeight: '100vh',
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      flexDirection: 'column',
      backgroundColor: '#000',
      fontFamily: 'Arial, sans-serif'
    }}>
      {/* HR Flow Title */}
      <h1 style={{
        color: '#4ee0c2',
        marginBottom: '30px',
        fontSize: '48px',
        textShadow: '0 0 10px #4ee0c2, 0 0 20px #4ee0c2',
      }}>
        HR Flow
      </h1>

      {/* Login Form */}
      <form onSubmit={handleLogin} style={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        width: '300px',
      }}>
        <h2 style={{ color: '#4ee0c2', marginBottom: '20px' }}>Login</h2>

        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          style={{
            width: '100%',
            padding: '12px',
            marginBottom: '20px',
            border: '2px solid #4ee0c2',
            backgroundColor: 'transparent',
            color: '#4ee0c2',
            borderRadius: '10px',
            outline: 'none',
            boxShadow: '0 0 10px #4ee0c2',
          }}
          required
        />

        <div style={{ width: '100%', position: 'relative', marginBottom: '25px' }}>
          <input
            type={showPassword ? "text" : "password"}
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            style={{
              width: '100%',
              padding: '12px',
              border: '2px solid #4ee0c2',
              backgroundColor: 'transparent',
              color: '#4ee0c2',
              borderRadius: '10px',
              outline: 'none',
              boxShadow: '0 0 10px #4ee0c2',
            }}
            required
          />
          <button
            type="button"
            onClick={() => setShowPassword(v => !v)}
            style={{
              position: 'absolute',
              right: 0,
              top: 0,
              height: '100%',
              padding: '0 12px',
              background: 'none',
              border: 'none',
              color: '#4ee0c2',
              fontWeight: 'bold',
              cursor: 'pointer',
              borderRadius: '0 10px 10px 0',
              outline: 'none'
            }}
          >
            {showPassword ? "Hide" : "Show"}
          </button>
        </div>

        {error && (
          <p style={{ color: 'red', marginBottom: '15px', textAlign: 'center' }}>{error}</p>
        )}

        <button type="submit" style={{
          width: '100%',
          padding: '12px',
          backgroundColor: '#4ee0c2',
          color: '#000',
          fontWeight: 'bold',
          border: 'none',
          borderRadius: '10px',
          cursor: 'pointer',
          boxShadow: '0 0 15px #4ee0c2, 0 0 30px #4ee0c2',
          transition: '0.3s',
        }}>
          Login
        </button>
      </form>
    </div>
  );
}