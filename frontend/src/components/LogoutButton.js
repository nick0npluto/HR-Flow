// src/components/LogoutButton.js
export default function LogoutButton({ onLogout, style }) {
    const handleLogout = async () => {
      await fetch("/api/auth/logout", { method: "POST" });
      onLogout();
    };
    return <button onClick={handleLogout} style={style}>logout</button>;
  }