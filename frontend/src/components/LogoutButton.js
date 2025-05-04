// src/components/LogoutButton.js
export default function LogoutButton({ onLogout }) {
    const handleLogout = async () => {
      await fetch("/api/auth/logout", { method: "POST" });
      onLogout();
    };
    return <button onClick={handleLogout}>Logout</button>;
  }